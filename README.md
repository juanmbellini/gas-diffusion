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
$ java -jar <path-to-jar>
```

## Authors

- [Juan Marcos Bellini](https://github.com/juanmbellini)
- [Matías Fraga](https://github.com/matifraga)
 