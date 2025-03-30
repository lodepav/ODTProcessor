package com.odtprocessor.report;

import com.odtprocessor.config.AppConfig.OutputFormat;

/**
 * Factory class for creating format-specific report generators.
 */
public class ReportGeneratorFactory {
	/**
	 * Creates a report generator for the specified output format.
	 * @param format Target output format (JSON or CSV)
	 * @return Configured report generator
	 * @throws IllegalArgumentException For unsupported formats
	 * @apiNote Add new formats here when implementing additional report types
	 */
	public static ReportGenerator create(OutputFormat format) {
		return switch (format) {
			case JSON -> new JsonReportGenerator();
			case CSV -> new CsvReportGenerator();
			default -> throw new IllegalArgumentException(
					"Unsupported format: " + format);
		};
	}
}