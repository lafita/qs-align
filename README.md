# QS-align

A scalable protein quaternary structure alignment algorithm.

### How is this tool useful to me?

Have you ever needed to compare two protein complexes? 
Would you like to know which subunits from two protein assemblies are equivalent?

Traditional quaternary structure alignment methods combinatorially evaluate all possible chain-chain mappings between the two protein complexes. 
Although this approach works for comparisons of small protein complexes (up to 3 subunits), the computing time scales exponentially with the size of the input structures and rapidly becomes prohibitively expensive. 
**QS-align** provides an 

### How does the algorithm work?

The algorithm is fully described and tested in *Chapter 4* of *Lafita A. Assessment of protein assembly prediction in CASP12 & Conformational dynamics of integrin α‐I domains. ETH Zürich. 2017;99.* (available at: https://doi.org/10.3929/ethz-a-010863273).
The original [pull request](https://github.com/biojava/biojava/pull/571) with the source code of the algorithm also contains an extensive description of the method.

### What's in this repository?

This repository contains the command-line interface (CLI) to the **QS-align** algorithm.
The source code is included in the **BioJava** library (https://github.com/biojava/biojava) from version 5.

### Download

The latest version of the **QS-align** CLI tool is available from the [releases](https://github.com/lafita/qs-align/releases).
**QS-align** only requires Java 8 or higher.

### Run QS-align

Ask for help:

```
java -jar qs-align_X.X.jar -h

usage: java -jar QsAlign.jar [options]
 -h,--help            Print usage information
 -t,--target <file>   Model of the first Structure [required]
 -q,--query <file>    Model of the second Structure [required]
 -o,--output <file>   Path to the output file [default: stdout]
```

Run the method:

```
java -jar qs-align_X.X.jar -t 1bcc -q 1kb9
```

Input protein formats and options:

### Output format

Columns:

- `Query` and `Target`: PDB codes or file names of the aligned protein structures.
- `Relation`:
- `ChainLength`:
- `Relation`:
- `Relation`:
- `Relation`:


### Build from source

To build the tool from source, clone this repository and run a Maven install.
All other Java dependencies, including **BioJava** will be downloaded and installed by Maven.

```
clone https://github.com/lafita/qs-align.git
cd qs-align
mvn install
```

The newly generated **JAR** executable will be placed in the `target` directory:

```
target/qs-align-X.X-SNAPSHOT.jar
```

Apache Maven is a software project management tool.
More information about Maven and instructions on how to install it can be found at https://maven.apache.org.
