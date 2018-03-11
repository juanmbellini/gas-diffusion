package ar.edu.itba.ss.off_lattice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class.
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    /**
     * The {@link Logger} instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     * Entry point.
     *
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Hello, System Simulations");
    }
}
