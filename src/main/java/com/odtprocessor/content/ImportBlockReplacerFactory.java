package com.odtprocessor.content;

import com.odtprocessor.file.TextReplacement;
import java.util.Map;

/**
 * Creates text replacement strategies that apply multiple import block substitutions.
 */
public class ImportBlockReplacerFactory {
	/**
	 * Creates a composite replacement that applies all mappings sequentially.
	 * @param replacements Map of old → new filenames to replace
	 * @return Replacement function that applies all mappings in iteration order
	 * @implNote Replacement order matters - earlier entries may affect later ones
	 */
	public TextReplacement createReplacer(Map<String, String> replacements) {
		return text -> {
			String modified = text;
			// Apply replacements in map iteration order
			for (Map.Entry<String, String> entry : replacements.entrySet()) {
				ImportBlockReplacer replacer = new ImportBlockReplacer(
						entry.getKey(), entry.getValue()
				);
				modified = replacer.replaceImports(modified);
			}
			return modified;
		};
	}
}