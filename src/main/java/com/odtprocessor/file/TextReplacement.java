package com.odtprocessor.file;

/**
 * Defines a text transformation operation for modifying document content.
 * Implementations are typically used for replacing import declarations.
 */
@FunctionalInterface
public interface TextReplacement {
	/**
	 * Applies a transformation to the input text.
	 * @param original The original text content
	 * @return Modified text content. May return original if no changes needed.
	 */
	String apply(String original);
}