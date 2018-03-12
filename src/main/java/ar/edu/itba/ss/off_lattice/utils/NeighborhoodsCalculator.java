package ar.edu.itba.ss.off_lattice.utils;

import ar.edu.itba.ss.off_lattice.models.Particle;
import ar.edu.itba.ss.off_lattice.models.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Object in charge of obtaining the neighborhoods in a {@link Space}.
 *
 * @implNote This object computes the neighborhoods using Cell Index Method.
 */
public class NeighborhoodsCalculator {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NeighborhoodsCalculator.class);

    /**
     * The space in which the neighborhood will be computed.
     */
    private final Space space;
    /**
     * The interaction radius (i.e up to which radius a {@link Particle} is consider a neighbor of another).
     */
    private final double interactionRadius;
    /**
     * The amount of grids the {@link Space} is divided into.
     */
    private final int M;

    /**
     * A {@link GridCellFactory} used to get {@link GridCell}s (i.e used in the Cell Index Method).
     */
    private final GridCellFactory gridCellFactory;


    /**
     * Constructor.
     *
     * @param space             The space in which the neighborhood will be computed.
     * @param interactionRadius The interaction radius
     *                          (i.e up to which radius a {@link Particle} is consider a neighbor of another).
     * @param M                 The amount of grids the {@link Space} is divided into.
     */
    public NeighborhoodsCalculator(Space space, double interactionRadius, int M) {
        validateParams(space, interactionRadius, M);
        this.space = space;
        this.interactionRadius = interactionRadius;
        this.M = M;
        this.gridCellFactory = GridCellFactory.getFactory(M);

    }

    /**
     * Validates the given parameters.
     *
     * @param space             The space to be validated.
     * @param interactionRadius The interaction radius to be validated.
     * @param M                 The 'M' value to be validated.
     * @throws IllegalArgumentException In case any of the parameters in not valid.
     */
    private static void validateParams(Space space, double interactionRadius, int M) throws IllegalArgumentException {
        Assert.notNull(space, "The space must not be null");
        if (Double.compare(interactionRadius, 0) < 0) {
            // TODO: check interaction radius == 0 (particles in the exact same position)
            throw new IllegalArgumentException("The interaction radius must be positive");
        }
        if (M <= 0) {
            throw new IllegalArgumentException("There must be at least one grid per side");
        }
        if (M != 1 && Double.compare((space.getSideLength() / M), interactionRadius) <= 0) {
            throw new IllegalArgumentException("The interaction radius must be lower than " +
                    "the space side length divided by the amount of grids per side. " +
                    "Values were: L = " + space.getSideLength() + ", M = " + M + ", r = " + interactionRadius + ".");
        }
    }

    /**
     * Computes the neighborhoods in the {@link Space}.
     *
     * @return a {@link Map} holding for each {@link Particle} in the {@link Space} its neighbors.
     */
    public Map<Particle, List<Particle>> computeNeighborhoods() {

        LOGGER.debug("Splitting space into a grid....");
        // Split space particles into a grid
        final double factor = M / space.getSideLength();
        final Map<GridCell, List<Particle>> grid = space.getParticles().stream()
                .collect(Collectors.groupingBy(p -> getGridPosition(p, factor)));
        LOGGER.debug("Finished splitting space.");

        LOGGER.debug("Calculating related particles...");
        // Transform each grid cell in the List of Particles belonging to the grid,
        // mapped to the List of Particles in the nearby cells
        final Map<List<Particle>, List<Particle>> relatedParticles = grid.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, e -> nearParticles(e.getKey(), grid, M)));
        LOGGER.debug("Finished calculating related particles.");

        LOGGER.debug("Calculating neighbors...");
        final Map<Particle, List<Particle>> result = new HashMap<>();
        // For each <List,List> tuple, perform the following...
        for (Map.Entry<List<Particle>, List<Particle>> related : relatedParticles.entrySet()) {
            // A Set to save those particles in the same grid that their distances have been already calculated
            final Set<Particle> alreadyCalculated = new HashSet<>();

            // For each Particle in the Key List, perform the following...
            for (Particle particle : related.getKey()) {
                // Get the Particle's List of neighbors.
                final List<Particle> neighbors = result.getOrDefault(particle, new LinkedList<>());
                // Get the Particle's new neighbors, calculating the distance to the possible new neighbors,
                // and filtering only those whose distance is lower or equal to the interaction radius.
                final List<Particle> newNeighbors = related.getValue()
                        .stream()
                        .parallel() // Perform this operation using parallelism to increase performance
                        .filter(another -> Double.compare(particle.distanceTo(another), interactionRadius) <= 0)
                        .collect(Collectors.toList());
                // Calculate distances to the same grid's particles
                final List<Particle> sameGridNewNeighbors = related.getKey()
                        .stream()
                        .parallel() // Perform this operation using parallelism to increase performance
                        .filter(another -> !particle.equals(another))
                        .filter(another -> !alreadyCalculated.contains(another))
                        .filter(another -> Double.compare(particle.distanceTo(another), interactionRadius) <= 0)
                        .collect(Collectors.toList());
                alreadyCalculated.add(particle); // Save this particle in the already calculated set

                // Add all new neighbors.
                neighbors.addAll(newNeighbors);
                neighbors.addAll(sameGridNewNeighbors);

                // For each new Neighbor, add the Particle to their list of neighbors
                for (Particle another : newNeighbors) {
                    final List<Particle> anotherNeighbors = result.getOrDefault(another, new LinkedList<>());
                    anotherNeighbors.add(particle);
                    result.put(another, anotherNeighbors);
                }

                // Save the List in the result map
                result.put(particle, neighbors);
            }
        }
        LOGGER.debug("Finished calculating neighbors.");
        return result;
    }


    /**
     * Method that calculates to which cell a particle belongs to.
     *
     * @param particle The particle to which the calculation must be done.
     * @param factor   A factor used to calculate the position
     *                 (i.e space side length / amount of grids per side).
     * @return The {@link GridCell} to which the particle belongs to.
     * @implNote The origin of the grid is the lower left corner.
     */
    private GridCell getGridPosition(Particle particle, double factor) {
        final int row = (int) (particle.getY() * factor);
        final int column = (int) (particle.getX() * factor);

        return this.gridCellFactory.getGridCell(row, column);
    }


    /**
     * Calculates which {@link Particle}s are related to a given {@link GridCell}.
     *
     * @param grid             The {@link GridCell} to which the related {@link Particle}s will be calculated.
     * @param particlesPerCell A {@link Map} holding, for each {@link GridCell},
     *                         a {@link List} of {@link Particle} that belongs to the said {@link GridCell}.
     * @param M                Amount of {@link GridCell}s in a space side
     *                         (used to know how to take into account periodic boundary conditions).
     * @return A {@link List} holding those {@link Particle}s related to the given {@code grid}.
     */
    private List<Particle> nearParticles(GridCell grid, Map<GridCell, List<Particle>> particlesPerCell, int M) {
        final Set<GridCell> neighborCells = neighborGridCell(grid, M);
        return particlesPerCell.entrySet().stream()
                .filter(entry -> neighborCells.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Calculates which {@link GridCell}s are related (are neighbors) with the given {@code gridCell}.
     *
     * @param gridCell The {@link GridCell} to which the neighbor cells will be calculated.
     * @param M        Amount of {@link GridCell}s in a space side
     *                 (used to know how to take into account periodic boundary conditions).
     * @return A {@link Set} holding the neighbor {@link GridCell}s of the given {@code gridCell}.
     */
    private Set<GridCell> neighborGridCell(GridCell gridCell, int M) {
        // Upper grid cell
        final int upperRow = Math.floorMod(gridCell.getRow() + 1, M);
        final int upperColumn = Math.floorMod(gridCell.getColumn(), M);

        // Upper-right grid cell coordinates
        final int upperRightRow = Math.floorMod(gridCell.getRow() + 1, M);
        final int upperRightColumn = Math.floorMod(gridCell.getColumn() + 1, M);

        // Right grid cell coordinates
        final int rightRow = Math.floorMod(gridCell.getRow(), M);
        final int rightColumn = Math.floorMod(gridCell.getColumn() + 1, M);

        // Lower-right grid cell coordinates
        final int lowerRightRow = Math.floorMod(gridCell.getColumn() - 1, M);
        final int lowerRightColumn = Math.floorMod(gridCell.getColumn() + 1, M);

        return Stream.of(
                this.gridCellFactory.getGridCell(upperRow, upperColumn),
                this.gridCellFactory.getGridCell(upperRightRow, upperRightColumn),
                this.gridCellFactory.getGridCell(rightRow, rightColumn),
                this.gridCellFactory.getGridCell(lowerRightRow, lowerRightColumn)
        ).collect(Collectors.toSet());
    }


    /**
     * Bean class representing a position in the grid (i.e a corresponding cell).
     */
    private static final class GridCell {

        /**
         * The row for this cell.
         */
        private final int row;

        /**
         * The column for this cell.
         */
        private final int column;

        /**
         * Constructor.
         *
         * @param row    The row for this cell.
         * @param column The column for this cell.
         */
        private GridCell(int row, int column) {
            if (row < 0 || column < 0) {
                throw new IllegalArgumentException("Row and Column must be positive.");
            }
            this.row = row;
            this.column = column;
        }

        /**
         * @return The row for this cell.
         */
        private int getRow() {
            return row;
        }

        /**
         * @return The column for this cell.
         */
        private int getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GridCell)) {
                return false;
            }

            final GridCell gridCell = (GridCell) o;

            return row == gridCell.row && column == gridCell.column;
        }

        @Override
        public int hashCode() {
            return 31 * row + column;
        }
    }

    /**
     * Factory class for {@link GridCell}s, in order to avoid creating a new {@link GridCell} each time it is needed.
     */
    private static final class GridCellFactory {

        /**
         * {@link Map} holding, for each value of {@code M}, the corresponding {@link GridCellFactory}.
         */
        private final static Map<Integer, GridCellFactory> factories = new HashMap<>();

        /**
         * An 2-D array holding each grid cell,
         * ordered using the {@code row} and {@code column} of the stored {@link GridCell}s.
         */
        private final GridCell[][] grid;

        /**
         * The amount of cells per side.
         */
        private final int M;


        /**
         * Constructor.
         *
         * @param M The amount of cells per side.
         */
        private GridCellFactory(int M) {
            this.M = M;
            // Create all GridCells when this factory is created.
            this.grid = IntStream.range(0, M)
                    .mapToObj(row ->
                            IntStream.range(0, M)
                                    .mapToObj(column -> new GridCell(row, column))
                                    .toArray(GridCell[]::new))
                    .toArray(GridCell[][]::new);
        }

        /**
         * Retrieves the {@link GridCell} for the given {@code row} and {@code column.}
         *
         * @param row    The row of the {@link GridCell}.
         * @param column The column of the {@link GridCell}.
         * @return The {@link GridCell} with the given {@code row} and {@code column.}.
         */
        private GridCell getGridCell(int row, int column) {
            if (row < 0 || row >= M || column < 0 || column >= M) {
                throw new IllegalArgumentException("Row and Column must be values between 0 and " + M + ".");
            }
            return grid[row][column];
        }

        /**
         * Gets a {@link GridCellFactory} for the given {@code M}.
         *
         * @param M The amount of cells per side.
         * @return The {@link GridCellFactory}.
         * @implNote This is not synchronized but it does not matter because,
         * in case there are write interferences at most we will have instantiated twice the same factory,
         * which is not a problem, as the Map will override with the same value for a given key.
         */
        private static GridCellFactory getFactory(int M) {
            GridCellFactory factory = factories.get(M);
            if (factory == null) {
                factory = new GridCellFactory(M);
                factories.put(M, factory);
            }
            return factory;
        }
    }
}
