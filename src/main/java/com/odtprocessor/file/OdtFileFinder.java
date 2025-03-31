package com.odtprocessor.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static com.odtprocessor.util.PathUtils.validateDirectory;

/**
 * Recursively finds ODT files in a directory structure
 */
public class OdtFileFinder {

	/**
	 * @param rootDir Starting directory for search
	 * @return List of paths to .odt files
	 * @throws IOException If directory traversal fails
	 */
	public List<Path> findOdtFiles(Path rootDir) throws IOException {
		validateDirectory(rootDir);

		try (Stream<Path> pathStream = Files.walk(rootDir)) {
			return pathStream
					.filter(this::isOdtFile)
					.toList();
		}
	}

	private boolean isOdtFile(Path path) {
		return Files.isRegularFile(path) &&
				path.getFileName().toString().toLowerCase().endsWith(".odt");
	}
}