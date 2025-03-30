# ODT Document Processor

A Java application for analyzing and modifying import relationships in OpenDocument Text (ODT) files.

## Features
- Scan directories for ODT files with `[import ...]` blocks
- Generate JSON/CSV reports of document import relationships
- Bulk replace import references across documents
- Handle nested directories and complex file structures

---

## Installation

### Prerequisites
- Java 17+
- Maven 3.8+

### Build the project

```bash
# Clone the repository
git clone https://github.com/lodepav/ODTProcessor.git

# Navigate to project directory
cd ODTProcessor

# Build the project
mvn clean install
```

---

## Usage

### Basic Commands

#### Analyze Import Relationships (Generates report):
```bash
java -jar target/odt-processor.jar \
  -c analyze \
  -d /path/to/documents \
  -o report.json \
  -f json
```

#### Replace Import References:
```bash
java -jar target/odt-processor.jar \
  -c replace \
  -d /path/to/documents \
  -r old.odt new.odt
```

---

## Command Options

| Option | Long Form   | Description                           | Required For       |
|--------|------------|--------------------------------------|--------------------|
| `-c`   | `--command` | `analyze` or `replace`              | Always required    |
| `-d`   | `--directory` | Root directory of ODT files        | Always required    |
| `-o`   | `--output` | Output file path (otherwise uses Root directory by default)                     | Analyze command (optional)   |
| `-f`   | `--format` | `json` or `csv`                      | Analyze command (optional)    |
| `-r`   | `--replace` | Old and new filenames (space-separated)                | Replace command (optional)    |
| `-h`   | `--help`    | Show usage help                      |                |

---

## Third-Party Libraries

### Core Dependencies

| Library              | Version  | Justification                                             |
|----------------------|---------|---------------------------------------------------------|
| `odfdom-java`       | 0.12.0  | Apache's official ODT manipulation library              |
| `jackson-databind`  | 2.13.4  | Industry-standard JSON processing with streaming API    |
| `log4j-core`        | 2.24.3  | High-performance logging with async support             |
| `log4j-slf4j2-impl` | 2.24.3  | Bridges SLF4J API with Log4j2 implementation            |

### Test Dependencies

| Library          | Version  | Justification                                          |
|------------------|---------|------------------------------------------------------|
| `junit-jupiter` | 5.12.1  | Modern testing framework with parameterized support |
| `assertj-core`  | 3.27.3  | Fluent assertions for readable test validations     |
| `commons-csv`   | 1.9.0   | Robust CSV handling for test output validation     |

---

## Error Handling

### Exit Codes

| Code | Type             | Description                                      |
|------|----------------|--------------------------------------------------|
| 0    | Success        | Operation completed successfully                 |
| 1    | Input Error   | Invalid arguments/missing required parameters    |
| 2    | Processing Error | File I/O errors, invalid ODT structure       |

---

## Development

### Running Tests
```bash
mvn test
```

### Test Features
- 85%+ code coverage
- Temp file cleanup
- Real ODT file validation
- Parameterized edge case testing

---

## How It Works

### Analysis Workflow

1. **Directory Scanning**  
   Recursively searches the specified directory for `.odt` files using Java NIO.

2. **Content Extraction**  
   For each ODT file:
   - Unzips the ODT (which is a ZIP archive)
   - Parses `content.xml` and styles using Apache ODFDOM
   - Extracts text from body, headers, and footers

3. **Import Detection**  
   Uses regex patterns to find `[import filename.odt]` blocks:
   ```regex
   \[import\s+([^]]+\.odt)\s*\]
   ```
   Validates filenames against security constraints.

4. **Report Generation**  
   Creates structured output (JSON/CSV) showing:
   ```json
   [
     {
       "document": "path/to/main.odt",
       "imports": ["header.odt", "footer.odt"]
     }
   ]
   ```

### Replacement Workflow

1. **Pattern Matching**  
   Creates safe regex patterns for each old filename:
   ```java
   Pattern.compile("\\[import\\s*" + Pattern.quote(oldName) + "\\s*\\]", Pattern.CASE_INSENSITIVE);
   ```

2. **In-Place Modification**  
   - Modifies text nodes in ODT XML content
   - Preserves document structure/styles
   - Validates modified files can be reopened in LibreOffice

3. **File Saving**  
   Re-zips modified content while maintaining:
   - MIME type declarations
   - File structure requirements
   - XML schema validity
