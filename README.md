# Selenium TestNG Framework

This project is a robust automation testing framework built with Selenium WebDriver, TestNG, Java, and Log4j2, following the Page Object Model design pattern.

## Project Structure

```
test/
├── java/
│   ├── my.project.Test/
│   │   ├── DashboardTest.java
│   │   └── LoginTest.java
│   ├── pages/
│   │   ├── BaseClass.java
│   │   ├── DashboardPage.java
│   │   └── LoginPage.java
│   └── utils/
│       ├── DriverFactory.java
│       ├── LogDirectoryInitializer.java
│       ├── RetryAnalyzer.java
│       └── TestListener.java
├── resources/
│   ├── log4j2.xml
│   ├── test-config.properties
│   └── testng.xml
└── test-output/
    └── ExtentReport_*.html

.gitignore
Dockerfile
Jenkinsfile
pom.xml
```

## Key Components

### Test Classes
- `DashboardTest.java`: Contains test methods for dashboard functionality.
- `LoginTest.java`: Contains test methods for login functionality.

### Page Objects
- `BaseClass.java`: Base class for all page objects, containing common methods and WebDriver initialization.
- `DashboardPage.java`: Page object for the dashboard page.
- `LoginPage.java`: Page object for the login page.

### Utility Classes
- `DriverFactory.java`: Manages WebDriver instance creation.
- `LogDirectoryInitializer.java`: Initializes log directories.
- `RetryAnalyzer.java`: Implements retry logic for failed tests.
- `TestListener.java`: TestNG listener for logging and reporting.

### Configuration Files
- `log4j2.xml`: Log4j2 configuration file.
- `test-config.properties`: Test configuration properties.
- `testng.xml`: TestNG suite configuration file.

## Running Tests

To run the tests, use the following Maven command:

```
mvn clean test
```

This command will execute the TestNG suite defined in `src/test/resources/testng.xml`.

## Jenkins Configuration

The `Jenkinsfile` in the project root defines the CI/CD pipeline. Here's a brief overview of the stages:

1. **Checkout**: Retrieves the latest code from the repository.
2. **Build**: Compiles the project using Maven.
3. **Run Tests**: Executes tests for different browsers (Firefox and Chrome).
4. **Publish Test Results**: Publishes TestNG results.
5. **Publish Extent Report**: Generates and publishes the Extent Report.

## Dependencies

Major dependencies include:

- Selenium WebDriver (4.24.0)
- TestNG (7.10.2)
- WebDriverManager (5.9.2)
- ExtentReports (5.0.9)
- Log4j2 (2.24.0)
- Lombok (1.18.34)

For a complete list of dependencies, refer to the `pom.xml` file.

## Configuration

### Browser Configuration
Browser selection is done through the `testng.xml` file. Each test can be configured to run on different browsers:

```xml
<test name="Login Tests - Firefox">
    <parameter name="browser" value="firefox"/>
    <!-- ... -->
</test>
```

### Base URL Configuration
The base URL is set in `test-config.properties`:

```properties
base.url=https://www.saucedemo.com
```

## Logging

Logging is configured in `log4j2.xml`. Logs are stored in the `logs` directory, separated by browser type.

## Reporting

The framework uses ExtentReports for generating detailed HTML reports. Reports are stored in the `test-output` directory.

## Docker Support

A `Dockerfile` is provided for containerized execution of tests. Build and run the Docker image to execute tests in a containerized environment.

## Contributing

Please read `CONTRIBUTING.md` for details on our code of conduct, and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the `LICENSE.md` file for details.

## Jenkins Configuration

The `Jenkinsfile` in the project root defines the CI/CD pipeline. Here's a detailed breakdown of the pipeline stages:

1. **Checkout**:
    - Retrieves the latest code from the source control repository.

2. **Build**:
    - Uses Maven to compile the project.
    - Command: `mvn clean compile`

3. **Run Tests - Firefox**:
    - Executes tests specifically for Firefox browser.
    - Command: `mvn test -Dbrowser=firefox -DbaseUrl=https://www.saucedemo.com/`

4. **Run Tests - Chrome**:
    - Executes tests specifically for Chrome browser.
    - Command: `mvn test -Dbrowser=chrome -DbaseUrl=https://www.saucedemo.com/`

5. **Publish Test Results**:
    - Publishes TestNG results using the JUnit plugin.
    - Looks for test results in: `**/target/surefire-reports/testng-results.xml`

6. **Publish Extent Report**:
    - Generates and publishes the Extent Report as an HTML report.
    - Uses the HTML Publisher plugin.
    - Report directory: `test-output`
    - Report files: `ExtentReport.html`
    - Report name: 'Extent Report'

### Jenkinsfile Content

```groovy
pipeline {
    agent any

    tools {
        maven 'Maven 3.8.4'
        jdk 'JDK 17'
    }

    environment {
        FIREFOX_BINARY = '/usr/bin/firefox'
        CHROME_BINARY = '/usr/bin/google-chrome-stable'
        GECKODRIVER_PATH = '/usr/local/bin/geckodriver'
        CHROMEDRIVER_PATH = '/usr/local/bin/chromedriver'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests - Firefox') {
            steps {
                sh 'mvn test -Dbrowser=firefox -DbaseUrl=https://www.saucedemo.com/'
            }
        }

        stage('Run Tests - Chrome') {
            steps {
                sh 'mvn test -Dbrowser=chrome -DbaseUrl=https://www.saucedemo.com/'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/testng-results.xml'
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report'
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'logs/**/*.log', allowEmptyArchive: true
            cleanWs()
        }
    }
}
```

### Running the Pipeline in Jenkins

To run this pipeline in Jenkins:

1. Ensure Jenkins is set up with the necessary plugins:
    - Git plugin
    - Maven Integration plugin
    - HTML Publisher plugin

2. Create a new Jenkins Pipeline job:
    - In Jenkins, click "New Item"
    - Choose "Pipeline" as the job type
    - Give your job a name and click "OK"

3. Configure the pipeline:
    - In the job configuration, scroll to the "Pipeline" section
    - Choose "Pipeline script from SCM" in the "Definition" dropdown
    - Select "Git" as the SCM
    - Enter your repository URL
    - Specify the branch to build (e.g., `*/main`)
    - Set the "Script Path" to `Jenkinsfile`

4. Save the job configuration

5. Run the pipeline:
    - Click "Build Now" to manually trigger the pipeline
    - The pipeline will execute all stages defined in the Jenkinsfile

6. View Results:
    - After the pipeline completes, you can view the test results and Extent Report from the job's page
    - Click on the build number and look for "Test Result" and "Extent Report" links

### Continuous Integration

For continuous integration:

1. Set up webhooks in your Git repository to trigger the Jenkins job on each commit or pull request.
2. Configure the Jenkins job to poll SCM or use Git plugin's post-commit hook for automatic triggering.

This setup ensures that your tests are automatically run whenever changes are pushed to the repository, providing quick feedback on the health of your codebase.

[The rest of the README content remains the same...]
