package cli;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.stream.Collectors;

import javax.vecmath.Point3d;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIdentifier;
import org.biojava.nbio.structure.align.client.StructureName;
import org.biojava.nbio.structure.align.quaternary.QsAlign;
import org.biojava.nbio.structure.align.quaternary.QsAlignParameters;
import org.biojava.nbio.structure.align.quaternary.QsAlignResult;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.cluster.Subunit;
import org.biojava.nbio.structure.cluster.SubunitClustererParameters;
import org.biojava.nbio.structure.geometry.CalcPoint;
import org.biojava.nbio.structure.geometry.UnitQuaternions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The QsAlignMain provides a CLI interface to the {@link QsAlign} algorithm
 * for aligning protein structures.
 * 
 * @author Aleix Lafita
 * @since February 2016
 * @version 1.0
 *
 */
public class QsAlignMain implements Serializable {

	private static final long serialVersionUID = 1239186643368091857L;

	private static final Logger logger = LoggerFactory
			.getLogger(QsAlignMain.class);

	public static void main(String[] args) throws IOException,
			InterruptedException, StructureException {

		// ####################################
		// Parse the command line options

		Options options = getOptions();
		CommandLineParser parser = new DefaultParser();
		HelpFormatter help = new HelpFormatter();
		help.setOptionComparator(null);

		final CommandLine cli;
		try {
			cli = parser.parse(options, args, false);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			help.printHelp("QsAlign [options]", options);
			System.exit(1);
			return;
		}

		args = cli.getArgs();

		if (cli.hasOption("help")) {
			help.printHelp("java -jar QsAlign.jar [options]", options);
			System.exit(0);
			return;
		}

		// Select the parameters for clustering
		SubunitClustererParameters clusterParams = new SubunitClustererParameters();

		// Select parameters of the alignment
		QsAlignParameters alignParams = new QsAlignParameters();

		AtomCache cache = new AtomCache();

		Structure target = null;
		if (cli.hasOption("target")) {
			StructureIdentifier id = new StructureName(
					cli.getOptionValue("target"));
			target = cache.getStructure(id);
		} else {
			logger.error("No target Structure model specified.");
			System.exit(1);
			return;
		}

		Structure query = null;
		if (cli.hasOption("query")) {
			StructureIdentifier id = new StructureName(
					cli.getOptionValue("query"));
			query = cache.getStructure(id);
		} else {
			logger.error("No query Structure model specified.");
			System.exit(1);
			return;
		}

		PrintWriter output = null;
		if (cli.hasOption("output")) {
			output = new PrintWriter(new BufferedWriter(new FileWriter(
					cli.getOptionValue("output"))));
		} else {
			output = new PrintWriter(System.out, true);
		}

		// ##################################################
		// Do the alignment and write the results

		QsAlignResult result = QsAlign.align(query, target, clusterParams,
				alignParams);

		// Header plus values in two columns
		output.write("Query\tTarget\tRelation\tChainLength\t\tRMSD\tResidueLength\t");
		output.write("[Aligned-Query]\t[Aligned-Target]\t[Query-Target:OrientationAngle]\n");
		output.write(query.getStructureIdentifier().getIdentifier() + "\t");
		output.write(target.getStructureIdentifier().getIdentifier() + "\t");
		output.write(result.getRelation() + "\t");
		output.write(result.length() + "\t");
		output.write(String.format("%.2f\t", result.getRmsd()));
		output.write(result.getAlignment().length() + "\t");
		output.write(result.getAlignedSubunits1().stream()
				.map(Subunit::getName).collect(Collectors.toList())
				+ "\t");
		output.write(result.getAlignedSubunits2().stream()
				.map(Subunit::getName).collect(Collectors.toList())
				+ "\t");

		output.write("[ ");
		for (Integer q : result.getSubunitMap().keySet()) {

			Integer t = result.getSubunitMap().get(q);

			output.write(result.getSubunits1().get(q).getName() + "-");
			output.write(result.getSubunits2().get(t).getName() + ":");

			Point3d[] aligned1 = Calc.atomsToPoints(result
					.getAlignedAtomsForSubunits1(q));
			Point3d[] aligned2 = Calc.atomsToPoints(result
					.getAlignedAtomsForSubunits2(t));
			CalcPoint.transform(result.getAlignment().getBlockSet(0)
					.getTransformations().get(1), aligned2);
			
			double qOrient = UnitQuaternions.orientationAngle(aligned1,
					aligned2, false);
			qOrient = Math.min(Math.abs(2 * Math.PI - qOrient), qOrient);

			output.write(String.format("%.2f ", qOrient));
		}
		output.write("]\n");

		output.flush();
		output.close();

	}

	private static Options getOptions() {

		Options options = new Options();

		// Help
		options.addOption("h", "help", false, "Print usage information");

		// Files
		options.addOption(Option.builder("t").longOpt("target").hasArg(true)
				.argName("file")
				.desc("Model of the first Structure [required]").build());

		options.addOption(Option.builder("q").longOpt("query").hasArg(true)
				.argName("file")
				.desc("Model of the second Structure [required]").build());

		options.addOption(Option.builder("o").longOpt("output").hasArg(true)
				.argName("file")
				.desc("Path to the output file [default: stdout]").build());

		return options;
	}

}
