# Andulir

**Andulir** is a minimalistic, collaborative API automation testing tool designed for `Spring Boot` and `Spring Cloud`. It is tailored for small development teams and individual developers, aiming to streamline the API testing process.

## Key Features

With **Andulir**, you can:
- Easily annotate the APIs you want to test.
- Automatically generate data for testing once the project is running locally.
- View test results directly in the console.

**Andulir** uses a configuration file, `atest.xml`, to manage all test cases. If the test results are unsatisfactory, you can modify them in this file. The changes will persist, and previous test cases will re-run the next time the project is launched.

After testing an API, you can change its `tag` in `atest.xml` to ignore the test case. You can also manage the file with version control tools like `git`, allowing your teammates or front-end developers to conveniently share API test cases, reducing communication and testing costs.

Before deploying the project to production, all test cases managed by developers can be tested together, making **Andulir** a valuable part of your CI/CD pipeline.

## Workflow

### 1. Setting Up

#### 1.1 Importing Andulir

In the project root directory, clone the repository:

```bash
git clone https://github.com/Werun-backend/Andulir.git
```

In IntelliJ IDEA, add Andulir as a module:
1. Go to `File -> New -> Module From Existing Sources`.
2. Select `andulir.iml` in the "Select File or Directory to Import" window (important).
3. Refresh Maven to import the necessary dependencies.

Add Andulir as a dependency in the `pom.xml` of the module where you want to use it:

```xml
<dependency>
    <groupId>org.andulir</groupId>
    <artifactId>andulir</artifactId>
    <version>0.0.1</version>
</dependency>
```

Here's the translation into English:

### 1.2 Configuration:

Andulir focuses on a minimalist testing approach, so we aim to minimize the required configurations. The only place that needs configuration is in the project's `application.yml`.

- Configure the project location and the location of the `controller` package to determine the scope of interface scanning.
- Configure the generation location of the XML file; "root" indicates it will be generated in the project root directory.

```yaml
andulir:
  scan-package: org.andulir.controller
  file:
    path-setting: root # If not filled, it will be manually set
    path:              # Optional, fill in the absolute path
    filename:          # Optional
```


### 1.3 Usage

#### 1.3.1 Adding Annotations

Use the `@ATest` annotation to mark methods in the `Controller` layer that you want to test, specifying the number of test cases:

```java
@RestController
public class TestController {
  @ATest(2)
  public void test(List<User> users, Integer integer) {
    System.out.println("test");
  }
}
```

#### 1.3.2 Running Andulir

After marking enough APIs, you can start **Andulir** by calling `AndulirApplication.start()` in the `main` method:

```java
import org.andulir.AndulirApplication;

public class ExampleControllerTest {
    public static void main(String[] args) {
        AndulirApplication.start(args);
    }
}
```

#### 1.3.3 atest.xml


After starting, if the configuration option is set to `root`, you will find a file named `2024xxxxxxx_atest.xml` in the `/andulirTest/` folder of the project directory (it will be automatically generated if it does not exist). The XML file will be generated in the following format based on the relevant information of the annotated methods:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<aTest>
    <controllerMapping name="com.andulir.controller.TestController">
        <methodMapping name="test" status="2">
            ...
        </methodMapping>
    </controllerMapping>
</aTest>
```

The structure of the XML file includes:
- `<aTest>` as the root tag.
- `<controllerMapping>` showing the method's `Controller` class.
- `<methodMapping>` containing specific information about the method.

On the first run, **Andulir** will generate the file and test methods with `status=1`, outputting results to the console.

#### 1.3.4 Editing Test Cases

After initial testing, you may find:
1. The generated test data is suitable; change `status` to 0, marking it as complete.
2. The test data needs modification; adjust data in `atest.xml`, restart the project, and retest.

#### 1.3.5 Version Control

Manage the `atest.xml` file with other files using `git` or similar tools for unified version control.

## Architecture

Andulir comprises three core components: the **Parser**, **Generator**, and **Tester**, which interact with the `atest.xml` file.

### 2.1 Parser

The Parser analyzes annotated interfaces and generates corresponding XML tags, storing them in the file. It currently supports basic types, `List`, custom request types, and their nesting.

### 2.2 Generator

The Generator produces data based on the parsed types, using reflection and the Podam library.

### 2.3 Tester

The Tester reads the XML file, generates corresponding methods, and performs tests. Unlike conventional HTTP-based API testing, **Andulir** accesses methods directly, making it suitable for local testing.

### 2.4 Other Components
- **AndulirConfig**: Manually injects necessary beans.
- **AndulirProperty**: Supports configuration in `application.yml`.
- **Utilities**: Various utility classes required by the program.

## Future Enhancements

### 3.1 Git Diff-Based Automation

Instead of relying solely on annotations, **Andulir** could detect modified interfaces through `git diff`, facilitating fully automated testing.

### 3.2 Improved Request Parsing

Better support for nested `List<>` types and complex parameters.

### 3.3 Simplified Import Process

Uploading **Andulir** to a Maven repository for easier imports.

### 3.4 Bug Fixes & Robustness

Enhance error handling and fault tolerance.

### 3.5 CI/CD Integration

Automate interface testing before deployment with Jenkins or GitHub Actions.

### 3.6 Mocking Capabilities

Add support for mocking dependencies to further simplify testing.

## Final Thoughts

**Andulir** was born from the simple needs of everyday development. Its goal is to help small teams and individual developers build robust DevOps processes with ease.

If you'd like to contribute, please contact me at 2669184984@qq.com.

