> [!TIP]
> Naviage to [Project Main Document](../README.md)

# Project Documentation

## Overview
This project is a UI automation framework using Selenium, TestNG, Maven, and Allure for reporting. The framework supports automated testing with Java 18 and integrates with cloud testing platforms like LambdaTest.

## Features
- Java 18-based automation framework
- TestNG for test execution and assertions
- Maven for build and dependency management
- Allure Reports for test result visualization
- Selenium WebDriver for browser automation
- Cloud execution support (e.g., LambdaTest)

## Prerequisites
Ensure you have the following installed:
- [Java 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [Allure](https://docs.qameta.io/allure/)
- [Git](https://git-scm.com/downloads)

## Installation
Clone the repository:
```sh
git clone https://github.com/yourusername/your-repo.git
cd your-repo
```

## Configuration
Modify the `config.properties` file to set:
- `baseURL` for the application under test
- `browser` for execution (Chrome, Firefox, etc.)
- `gridURL` if running on a cloud/grid environment

## Running Tests
### Using Maven
To execute all tests:
```sh
mvn clean test
```

To run tests from a specific suite:
```sh
mvn test -DsuiteXmlFile=testng.xml
```

### Generating Reports
After test execution, generate Allure reports:
```sh
allure serve allure-results
```

## Project Structure
```
|-- src
|   |-- main
|   |   |-- java (utility classes)
|   |-- test
|   |   |-- java (test cases)
|-- pom.xml (Maven dependencies)
|-- testng.xml (Test suite configuration)
|-- config.properties (Test configurations)
```

## Dependencies (Maven)
```xml
<dependencies>
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.6.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.5.0</version>
    </dependency>
    <dependency>
        <groupId>io.qameta.allure</groupId>
        <artifactId>allure-testng</artifactId>
        <version>2.21.0</version>
    </dependency>
</dependencies>
```

## Contribution Guidelines
1. Fork the repository.
2. Create a new branch for your feature.
3. Commit and push changes.
4. Create a Pull Request for review.

## License
This project is licensed under the MIT License. See `LICENSE` for details.

## Contact
For issues or feature requests, open a GitHub Issue or reach out at [your-email@example.com].

