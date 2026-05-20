# Library Management System CLI

A SOLID-compliant Java CLI application for managing library books, members, and loans.

## 🏗 Architecture
The project follows a layered architecture:
- **Service Layer**: Business logic and pattern orchestration.
- **Repository Layer**: Data access using JDBC.
- **Model Layer**: Immutable data structures (Java Records).

## 🛠 Design Patterns Implemented
1. **Factory Method**: Used in `BookRepository.create(Connection)` to decouple service from implementation.
2. **Observer Pattern**: `LoanObserver` in `LibraryService` notifies external systems on checkout.
3. **Strategy Pattern**: `OutputStrategy` allows flexible formatting of book lists.
4. **Repository Pattern**: Abstracted data access layer.

## 🧪 Testing
- **JUnit 5** and **Mockito** for unit testing.
- Service layer coverage: >80%.
- Run tests: `./gradlew test`

## 🚀 Getting Started
1. The project is configured to use an in-memory H2 database for demonstration purposes.
2. Run the application using Gradle:
   ```bash
   ./gradlew :library-cli:run
   ```
3. (Optional) To use PostgreSQL, update `DatabaseConfig.java` with your credentials and re-enable the PostgreSQL driver in `build.gradle`.
