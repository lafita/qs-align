# QS-ALIGN: protein quaternary structure alignment

QS-align is a scalable protein quaternary structure alignment algorithm.



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

The output is a single row of Tab-separated values with their headers, like the one below:

```
Query	Target	Relation		ChainLength	RMSD	ResidueLength	[Aligned-Query]			[Aligned-Target]		[Query-Target:OrientationAngle]
1kb9	1bcc	PARTIAL_INCOMPLETE	8		2.25	1561		[A, B, C, D, F, G, H, I]	[A, B, C, D, H, F, G, J]	[ A-A:0.04 B-B:0.13 C-C:0.02 D-D:0.04 F-H:0.09 G-F:0.01 H-G:0.08 I-J:0.06 ]
```

The meaning of the headers for each value are:

- `Query` and `Target`: PDB codes or file names of the two aligned protein structures.
- `Relation`: specifies the outcome of the alignment and can be:
   - `EQUIVALENT`: both protein complexes have the same number of chains and they all match.
   - `PARTIAL_COMPLETE`: one complex has fewer chains than another and they all match. This means that one of the complexes is a subset of the other, and the other contains additional chains.
   - `PARTIAL_INCOMPLETE`: the number of matching chains is lower than the number of chains in either of the complexes. This means that each complex has additional chains not found in the other complex.
- `ChainLength`: the number of matching chains, included in the alignment.
- `RMSD`: the root mean square deviation (RMSD) at the residue level of the quaternary structure superposition.
- `ResidueLength`: the total number of aligned residues, summing up the residues in each chain in the alignment.
- `[Aligned-Query]`: a comma separated list of the matching chain IDs of the query structure.
- `[Aligned-Target]`: a comma separated list of the matchig chain IDs of the target structure.
- `[Query-Target:OrientationAngle]`:


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

### Contact

If you have any questions or problems, you can [post a new issue](https://github.com/lafita/qs-align/issues/new) in this repository.

### Cite this work

If you find this tool useful for your research project, please consider citing:

>Lafita A. Assessment of protein assembly prediction in CASP12 & Conformational dynamics of integrin α‐I domains. ETH Zürich. 2017;99.(available at: https://doi.org/10.3929/ethz-a-010863273)

Additionally, you can cite this repository with the URL https://github.com/lafita/qs-align. 

Thanks!

