package com.odtprocessor.cli;

import com.odtprocessor.config.AppConfig;

import org.apache.commons.cli.*;
import java.nio.file.Path;

/**
 * Handles command-line argument parsing and validation using Apache Commons CLI.
 * Translates CLI inputs into application configuration parameters.
 */
public class CommandLineInterface {
	// CLI option definitions using builder pattern
	private static final Options OPTIONS = new Options();

	static {
		// Required arguments
		OPTIONS.addOption(Option.builder("c")
				.longOpt("command")
				.hasArg()
				.argName("COMMAND")
				.desc("Command to execute (analyze|replace)")
				.required()
				.build());

		OPTIONS.addOption(Option.builder("d")
				.longOpt("directory")
				.hasArg()
				.argName("PATH")
				.desc("Root directory containing ODT files")
				.required()
				.build());

		// Optional arguments
		OPTIONS.addOption(Option.builder("o")
				.longOpt("output")
				.hasArg()
				.argName("FILE")
				.desc("Output file path")
				.build());

		OPTIONS.addOption(Option.builder("f")
				.longOpt("format")
				.hasArg()
				.argName("FORMAT")
				.desc("Output format (json|csv)")
				.build());

		// Replacement parameters (requires two values)
		OPTIONS.addOption(Option.builder("r")
				.longOpt("replace")
				.numberOfArgs(2)
				.argName("OLD NEW")
				.desc("File names to replace")
				.build());

		// Help command
		OPTIONS.addOption(Option.builder("h")
				.longOpt("help")
				.desc("Show help")
				.build());
	}

	/**
	 * Parses command-line arguments into application configuration.
	 * @param args Command-line arguments
	 * @return Configured AppConfig with validated parameters
	 * @throws ParseException For invalid/missing arguments
	 */
	public AppConfig parseArguments(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(OPTIONS, args);
		AppConfig config = new AppConfig();

		// Parse command
		config.setCommand(parseCommand(cmd.getOptionValue("c")));

		// Parse directory
		config.setRootDirectory(Path.of(cmd.getOptionValue("d")));

		// Parse output settings
		if (cmd.hasOption("o")) {
			config.setOutputPath(Path.of(cmd.getOptionValue("o")));
		} else { // otherwise use Root directory
			config.setOutputPath(Path.of(cmd.getOptionValue("d")));
		}

		if (cmd.hasOption("f")) {
			config.setOutputFormat(AppConfig.OutputFormat.valueOf(
					cmd.getOptionValue("f").toUpperCase()
			));
		}

		// Parse replacement parameters
		if (cmd.hasOption("r")) {
			String[] replaceArgs = cmd.getOptionValues("r");
			config.setOldFileName(replaceArgs[0]);
			config.setNewFileName(replaceArgs[1]);
		}

		return config;
	}

	public void printHelp() {
		new HelpFormatter().printHelp("odt-processor", OPTIONS);
	}

	private AppConfig.CliCommand parseCommand(String command) {
		return switch (command.toLowerCase()) {
			case "analyze" -> AppConfig.CliCommand.ANALYZE;
			case "replace" -> AppConfig.CliCommand.REPLACE;
			default -> throw new IllegalArgumentException("Invalid command");
		};
	}
}