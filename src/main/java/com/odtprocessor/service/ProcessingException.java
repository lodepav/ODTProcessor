package com.odtprocessor.service;

/**
 * Top-level exception for processing failures
 */
public class ProcessingException extends Exception {
	public ProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}