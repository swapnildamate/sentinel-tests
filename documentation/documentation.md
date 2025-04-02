# Automation Framework (API & UI Testing)

## 🚀 Overview

This is a **Java-based Automation Framework** for testing **REST APIs** and **Web UI applications**. It uses **Selenium WebDriver** for UI automation and **RestAssured** for API testing, along with **TestNG/JUnit** for test execution and **Allure/Extent Reports** for reporting.

## 🏗️ Project Structure

```
automation-framework/
│── src/
│   ├── main/
│   │   ├── java/
│   │ utomation-framework/
│── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── project/
│   │   │   │   │   ├── base/          # Base classes for API & UI tests
│   │   │   │   │   ├── config/        # Configuration files & setup
│   │   │   │   ├── com/
│   │   │   │   ├── project/
│   │   │   │   │   ├── base/          # Base classes for API & UI tests
│   │   │   │   │   ├── config/        # Configuration files & setup
│   │   │   │   │   ├── utils/         # Utility classes (JSON parser, WebDriver utils)
│   │   │   │   │   ├── api/           # API Testing package
│   │   │   │   │   │   ├── clients/   # API Client classes (HTTP methods)
│   │   │   │   │   │   ├── models/    # Request & Response POJOs
│   │   │   │   │   │   ├── tests/     # API test cases
│   │   │   │   │   ├── ui/            # UI Testing package
│   │   │   │   │   │   ├── pages/     # Page Object Model (POM)
│   │   │   │   │   │   ├── tests/     # UI Test cases
│   │   │   │   │   │   ├── components/# Common UI components (modals, menus)
│   │   │   │   │   ├── runners/       # TestNG or JUnit test runners
│   ├── test/
│   │   ├── java/                      # Additional test files if needed
│── resources/
│   ├── testdata/                      # Test data files (JSON, CSV, Excel)
│   ├── configs/                       # Config files (env, API base URL, browser settings)
│── reports/                           # Test reports (Extent, Allure)
│── logs/                              # Log files
│── pom.xml                            # Maven dependencies
│── README.md                          # Project documentation
│── .gitignore                         # Ignoring unnecessary files
```

## 🛠️ Tech Stack

- **Java** (Programming Language)
- **Selenium WebDriver** (UI Testing)
- **RestAssured** (API Testing)
- **TestNG / JUnit** (Test Execution)
- **Maven** (Dependency Management)
- **Allure / Extent Reports** (Test Reporting)
- **Log4j** (Logging)

## 🔧 Setup & Installation

### **1️⃣ Clone the repository**

```sh
git clone https://github.com/yourusername/automation-framework.git
cd automation-framework
```

### **2️⃣ Install Dependencies**

```sh
mvn clean install
```

### **3️⃣ Configure Environment**

Modify the configuration in `src/main/resources/configs/config.properties`:

```properties
browser=chrome
baseUrl=https://example.com
apiBaseUrl=https://reqres.in/api
```

### **4️⃣ Run Tests**

#### **Run API Tests**

```sh
mvn test -Dtest=com.project.api.tests.UserAPITest
```

#### **Run UI Tests**

```sh
mvn test -Dtest=com.project.ui.tests.LoginTest
```

#### **Run All Tests**

```sh
mvn test
```

## 📊 Reporting

### **1️⃣ Generate Allure Reports**

```sh
mvn allure:serve
```

### **2️⃣ View Extent Reports**

Reports are generated in `reports/` directory.

## 🎯 CI/CD Integration

You can integrate the framework with **Jenkins, GitHub Actions, or GitLab CI/CD** by adding a `Jenkinsfile` or `.github/workflows/test.yml`.

## 🤝 Contributing

1. **Fork** the repository
2. **Create** a new branch (`git checkout -b feature-branch`)
3. **Commit** your changes (`git commit -m 'Add new feature'`)
4. **Push** to the branch (`git push origin feature-branch`)
5. **Create** a Pull Request

## 📜 License

This project is licensed under the MIT License.

---

🚀 Happy Testing! 🚀

