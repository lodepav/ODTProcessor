package com.odtprocessor.core.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a document and its imported dependencies
 */
public class DocumentImports {
	private Path documentPath;
	private List<String> importedFiles;

	public DocumentImports(Path documentPath, List<String> importedFiles) {
		this.documentPath = documentPath;
		this.importedFiles = importedFiles;
	}

	public void addImportedFile(String filename) {
		if (filename != null && !filename.isBlank()) {
			importedFiles.add(filename.trim());
		}
	}

	// Getters and setters
	public Path getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(Path documentPath) {
		this.documentPath = documentPath;
	}

	public List<String> getImportedFiles() {
		return new ArrayList<>(importedFiles); // Return copy to prevent modification
	}

	public void setImportedFiles(List<String> importedFiles) {
		this.importedFiles = new ArrayList<>(importedFiles);
	}
}