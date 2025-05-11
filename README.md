# MYTool

# Java Tools

A desktop application developed using **Java** with **Swing** for UI and requires **JDK 17**. This tool provides functionalities such as Base64 encoding/decoding, JSON formatting/minification, etc.

---

## 🧰 Features

- **Base64 Encoder/Decoder**
  - Encode and decode strings to/from Base64.
- **JSON Formatter & Minifier**
  - Format JSON for better readability or minify it for efficient data transfer.
- **Expandable**
  - Additional tools can be added based on requirements.

---

## 🛠️ Technologies Used

- **Programming Language**: Java 17
- **UI Framework**: Swing
- **Build Tool (Optional)**: Maven

---

## 📦 Requirements

- **JDK 17 or higher**
- An environment supporting Swing
- (If applicable) Maven 3.x or Gradle 7.x

---

## ▶️ How to Run

### Running via IDE

1. Import the project into IntelliJ IDEA / Eclipse / NetBeans.
2. Set the SDK to JDK 17.
3. Execute the `main` method from the main class (e.g., `Main.java`).

### Running via Command Line

```bash
# Compile the code
javac --release 17 -d out src/**/*.java

# Run the compiled program
java -cp out com.example.tools.Main