package com.odtprocessor;

import com.odtprocessor.cli.CliProcessor;
import com.odtprocessor.cli.CommandLineInterface;
import com.odtprocessor.config.AppConfig;
import com.odtprocessor.content.ImportBlockDetector;
import com.odtprocessor.content.ImportBlockReplacerFactory;
import com.odtprocessor.file.OdtFileFinder;
import com.odtprocessor.file.OdtFileHandler;
import com.odtprocessor.service.OdtProcessorService;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for ODT document processing application.
 * Handles CLI arguments and coordinates document processing workflow.
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * Application entry point. Parses arguments and executes processing.
	 * @param args Command-line arguments in format defined by {@link CommandLineInterface}
	 */
	public static void main(String[] args) {
		CommandLineInterface cli = new CommandLineInterface();
		try {
			logger.info("Starting ODT Processor");
			AppConfig config = cli.parseArguments(args);

			CliProcessor processor = new CliProcessor(initializeProcessorService());
			processor.process(config);

			logger.info("Processing completed successfully");
		} catch (ParseException e) {
			logger.error("Invalid arguments: {}", e.getMessage());
			cli.printHelp();
			System.exit(1);  // Input error
		} catch (Exception e) {
			logger.error("Processing failed", e);
			System.exit(2);  // Execution error
		}
	}

	/** Initializes core service with required dependencies */
	private static OdtProcessorService initializeProcessorService() {
		return new OdtProcessorService(
				new OdtFileFinder(),
				new OdtFileHandler(),
				new ImportBlockDetector(),
				new ImportBlockReplacerFactory()
		);
	}
}