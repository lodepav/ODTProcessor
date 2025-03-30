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
