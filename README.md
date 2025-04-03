# Sentinel Tests

Guardian of software quality.

---

## Overview

This is a **Java-based Automation Framework** for testing **REST APIs** and **Web UI applications**. It uses **Selenium
WebDriver** for UI automation and **RestAssured** for API testing, along with **TestNG/JUnit** for test execution and *
*Allure, Excel, PDF** for reporting. Once the test execution is completed, an **Email** will be trigger.
---

## Project Structure

```
automation-framework/
│── allure-report                                    # Allure report (single consolidated report)
│── documentation                                    # Project documentation files
│── reports                                          # Folder containing various test reports
│   │── allure-results                               # Allure test execution results
│   │── excel-report                                 # Excel format test report
│   │── pdf-report                                   # PDF format test report
│── src/                                             # Source code and resources
│   ├── data-files/                                  # Input data files for tests
│   │    ├── excel                                   # Excel data sources
│   │    ├── jsons                                   # JSON data sources
│   ├── main/                                        # Main implementation code
│   │   ├── java/                                    # Java source files
│   │   │   ├── org/
│   │   │   │   ├── project/
│   │   │   │   │   ├── api/                         # API testing package
│   │   │   │   │   │   ├── collection               # API request collections
│   │   │   │   │   │   ├── utils                    # API utility classes
│   │   │   │   │   ├── base/                        # Base classes for API & UI tests
│   │   │   │   │   ├── config/                      # Configuration files & setup
│   │   │   │   │   ├── constants/                   # Constant values used in the framework
│   │   │   │   │   ├── enums/                       # Enums used in the framework
│   │   │   │   │   ├── ui/                          # UI testing package
│   │   │   │   │   │   ├── pages/                   # Page Object Model (POM) classes
│   │   │   │   │   │   ├── utils/                   # UI-specific utility classes
│   │   │   │   │   ├── utils/                       # Shared utilities for framework
│   │   │   │   │   │   ├── insights/                # Insight-based utilities (analytics/logs)
│   │   │   │   │   │   ├── log/                     # Logging utilities
│   │   │   │   │   │   ├── testng/                  # TestNG-related configurations
│   │   │   ├── resources/                           # Resource files like properties, configs
│   ├── test/                                        # Test implementation
│   │   ├── java/                                    # Java test scripts
│   │   │   ├── org/
│   │   │   │   ├── project/                         # Test cases
│   │   │   │   │   ├── api/                         # API test cases
│   │   │   │   │   │   ├── apitestclasses           # API test classes
│   │   │   │   │   ├── ui/                          # UI test cases
│   │   │   │   │   │   ├── uitestclasses            # UI test classes
│   │   │   ├── resources/                           # Test resources (extent reports, allure)
│── xml-suites/                                      # XML suite configurations for TestNG
│    ├── testngconf.xml                              # TestNG configuration file
│── .gitignore                                       # Ignoring unnecessary files for Git
│── app.log                                          # Log files for test execution
│── LICENSE.md                                       # License information
│── pom.xml                                          # Maven dependencies and configurations
│── README.md                                        # Project documentation (setup, usage)

```

---

## Tech Stack

- **Java** (Programming Language)
- **Selenium WebDriver** (UI Testing)
- **RestAssured** (API Testing)
- **TestNG** (Test Execution)
- **Maven** (Dependency Management)
- **Allure Reports** (Test Reporting)
- **Custom Logger** (Logging)

---

## Setup & Installation

1. **Java** (JDK 11 or later)

- Download and install from: [https://jdk.java.net/](https://jdk.java.net/)
- Verify installation:
  ```sh
  java -version
  ```

---

2. **Apache Maven**

- Download and install from: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
- Verify installation:
  ```sh
  mvn -version
  ```

---

3. **Allure Report**

- Install Allure for generating test reports:
  ```sh
  brew install allure  # For macOS
  scoop install allure # For Windows (using Scoop)
  sudo apt-add-repository ppa:qameta/allure && sudo apt update && sudo apt install allure # For Ubuntu
  ```
- Verify installation:
  ```sh
  allure --version
  ```

---

4. **IntelliJ IDEA** (Recommended IDE)

- Download and install from: [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)
- Install **TestNG** & **Allure Plugin** for better report visualization.

---

## **Clone the repository**

```sh
git clone https://github.com/swapnildamate/sentinel-tests.git
cd sentinel-tests
```

### **Install Dependencies**

```sh
mvn clean install
```

---

### **Configure Environment**

Modify the configuration in **src/main/resources/configs/config.properties**


---

### **Run Tests**

* Update the TestNG configuration file located in the xml-suites directory according to the requirements and save the
  changes.

```sh
mvn clean test verify
```

---

## Reporting

After the test execution, various reports are generated to provide insights into the test results. You can find these
reports in the following directory:

### How to View Reports

- **Allure Report**: Open `allure-report/index.html` in a browser to view the interactive test report.
- **Excel Report**: Open the `.xlsx` file in the `excel-report` folder to analyze test data.
- **PDF Report**: Open the `.pdf` file in the `pdf-report` folder for a summarized test execution report.
- **View Online via GitHub Pages** The reports are also hosted on **GitHub Pages** for easy access. Click the link below
  to view the latest Allure report online: *
  *[View Test Report on GitHub Pages](https://your-github-username.github.io/your-repository-name/allure-report/index.html)
  **

---

> [!TIP]
> Naviage to [GitHub Actions integration](documentation/integhaallure.md).

---

These reports provide a detailed breakdown of test results, including execution status, test failures, and overall
performance.

* Refer to the following folder to view the generated reports after test execution:

```
  automation-framework/
  │── reports                                          # Folder containing various test reports
  │   │── allure-results                               # Allure test execution results
  │   │── excel-report                                 # Excel format test report
  │   │── pdf-report                                   # PDF format test report
```

---

## CI/CD Integration

You can integrate the framework with **GitHub Actions, or GitLab CI/CD** by adding a
`.github/workflows/*.yml`.
---

## Contributing

1. **Fork** the repository
2. **Create** a new branch (`git checkout -b feature-branch`)
3. **Commit** your changes (`git commit -m 'Add new feature'`)
4. **Push** to the branch (`git push origin feature-branch`)
5. **Create** a Pull Request

---

## License

Naviage to [License](LICENSE.md).

This project is licensed under the Apache License, Version 2.0.

---

# Happy Testing!

---