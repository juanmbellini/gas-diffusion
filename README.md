# Self-propelled agents flock [![Build Status](https://travis-ci.org/juanmbellini/off-lattice.svg?branch=master)](https://travis-ci.org/juanmbellini/off-lattice)

Second System Simulations project: Flocks of self-propelled agents using the Off-Lattice cellular automaton

## Getting started

These instructions will install the system in your local machine.

### Prerequisites

1. Install Maven, if you haven't yet
    #### Mac OS X

    ```
    $ brew install maven
    ```

    #### Ubuntu

    ```
    $ sudo apt-get install maven
    ```

    #### Other OSes
    Check [Maven website](https://maven.apache.org/install.html).

2. Clone the repository, or download source code

	```
	$ git clone https://github.com/juanmbellini/off-lattice
	```
	or
	
	```
	$ wget https://github.com/juanmbellini/off-lattice/archive/master.zip
	```

### Installing

1. Change working directory to project root (i.e where pom.xml is located):

    ```
    $ cd <project-root>
    ```

2. Let maven resolve dependencies:

    ```
    $ mvn dependency:resolve -U
    ```

3. Create jar file

    ```
    $ mvn clean package
    ```
    **Note:** The jar file will be under ``` <project-root>/target ```


## Usage

You can run the simulation with the following command:

```
$ java -jar <path-to-jar> [arguments]
```

In order to customize the simulation, you can specify different types of parameters.

### Space Size
You can specify the space's side length with the ```--custom.system.length``` argument.
For example, if you want a length of ```150.0```, you would execute:

```
$ java -jar <path-to-jar> --custom.system.length=150.0
```
**The default value is 100.0.**

### Amount of Particles
You can specify the amount of particles in the space with the ```--particles``` argument.
For example, if you want ```2000``` particles, you would execute:

```
$ java -jar <path-to-jar> --custom.system.particles=2000
```

**The default value is 1000.**

### Interaction Radius
You can specify the particle's interaction radius with the ```--custom.system.interaction-radius``` argument.
For example, if you want a radius of ```4.5```, you would execute:

```
$ java -jar <path-to-jar> --custom.system.interaction-radius=4.5
```

**The default value is 2.5.**

### Simulation iterations
You can specify the amount of simulation iterations to be performed by the simulator with the ```--custom.simulation.iterations``` argument.
For example, if you want to perform ```2000``` iterations, you would execute:

```
$ java -jar <path-to-jar> --custom.simulation.iterations=2000
```

**The default value is 500.**

### ETA value
You can specify the eta value to be used to calculate noise when updating the particle's speed angle with the ```--custom.simulation.eta``` argument.
For example, if you want an eta value of ```3.5```, you would execute:

```
$ java -jar <path-to-jar> --custom.simulation.eta=3.5
```

**The default value is 1.5.**

### M value
You can specify the M value to be used by the cell index method with the ```--custom.simulation.M``` argument.
For example, if you want an M value of ```15```, you would execute:

```
$ java -jar <path-to-jar> --custom.simulation.M=15
```

**The default value is 10.**

### Ovito file path
You can specify the path where the Ovito file will be saved with the ```--output.ovito``` argument.
For example, if you want to save the file in the ```/tmp``` directory, you would execute:

```
$ java -jar <path-to-jar> --custom.output.ovito=/tmp
```

**There is no default value.**

## Authors

- [Juan Marcos Bellini](https://github.com/juanmbellini)
- [Matías Fraga](https://github.com/matifraga)
 