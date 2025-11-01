# SoftwareEngineeringProject

Project for the Software Engineering course at Université Côte d'Azur.

General information about the project, its goal and how it works, can be found in `report/Rapport.pdf`.

## Author

Daniel Carriba Nosrati

## Requirements

- Java is required <br>
Java version 21 is highly recommended, as the project was build and tested with Java 21

- Gradle Wrapper is included, no separate Gradle installation is required.

## Clone the repository

```bash
git clone https://github.com/dcarriba/SoftwareEngineeringProject.git
```

## Build and Run Instructions

Navigate into the project directory:

```bash
cd SoftwareEngineeringProject
```

### Build the project

To build the project, use the following command:

```bash
./gradlew build
``` 

### Run the project

To run the benchmarks (Time Benchmarks and Compression Ratio Benchmarks), as well as to calculate the transmission time for a latency t at which compression becomes worthwhile, for all implemented compression versions of the `Bit Packing` compression method, use the following command:

```bash
./gradlew run
```

### Run unit tests

To run the unit tests for all implemented compression versions of the `Bit Packing` compression method, use the following command:

```bash
./gradlew test
```
