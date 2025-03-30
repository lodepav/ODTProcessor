package com.odtprocessor.util;

import org.w3c.dom.Node;

/**
 * Functional interface for processing XML nodes during document traversal.
 * Used for modifying or extracting information from DOM nodes.
 */
@FunctionalInterface
public interface NodeProcessor {
	void process(Node node);
}