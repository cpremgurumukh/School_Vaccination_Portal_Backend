# Vaccination Management System - Backend

## 🚀 Project Overview

This is the backend of the Vaccination Management System, built using **Spring Boot (Java)**. It provides secure REST APIs for managing students, vaccination drives, vaccination records, and user authentication. The system is designed to support school vaccination programs by allowing coordinators to schedule drives, manage student records, and track vaccination progress.

## 🛠️ Tech Stack

* **Backend Framework:** Spring Boot (Java)
* **Database:** Relational Database (e.g., MySQL/PostgreSQL)
* **ORM:** Spring Data JPA (Hibernate)
* **Authentication:** Basic Auth (for testing) / JWT (JSON Web Token) (recommended for production)

## 📁 Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.prem.vaccinationportal
│   │   │       ├── config         # Configuration classes (Security, Database)
│   │   │       ├── controllers    # REST API controllers (Student, Drive, Auth)
│   │   │       ├── models         # JPA entities (Student, VaccinationDrive, VaccinationRecord, User)
│   │   │       ├── repositories   # Spring Data JPA repositories for database access
│   │   │       ├── services       # Business logic (StudentService, VaccinationDriveService)
│   │   │       └── VaccinationPortalApplication.java  # Application entry point
│   │   └── resources
│   │       └── application.properties  # Configuration properties (DB, server port)
└── pom.xml  # Maven dependencies
└── README.md
```

## 🚀 Getting Started

### Prerequisites

* **Java:** JDK 17 or higher
* **Maven:** For dependency management and building the project
* **Database:** MySQL/PostgreSQL (or any relational database supported by Spring Data JPA)
* **Node.js and npm:** For running the frontend (if integrated)

### Installation

```bash
# Clone the repository
git clone <repository-url>

# Navigate to the project directory
cd vaccination-management-backend
```

### Dependencies

The project uses Maven for dependency management. The key dependencies are defined in `pom.xml`. Ensure you have an internet connection to download dependencies when building the project. Key dependencies include:

- `spring-boot-starter-web`: For building REST APIs
- `spring-boot-starter-data-jpa`: For database access with JPA
- `spring-boot-starter-security`: For authentication and authorization
- `mysql-connector-java` (or equivalent): For MySQL database connectivity
- `jjwt`: For JWT token generation and validation (if using JWT)

Run the following command to install dependencies:

```bash
mvn install
```

### Database Setup

1. **Set up your database**:
   - Create a MySQL/PostgreSQL database (e.g., `vaccination_db`).
   - Ensure your database server is running.

2. **Configure the database**:
   - Update the `src/main/resources/application.properties` file with your database credentials.

### Environment Configuration

* Create or update the `src/main/resources/application.properties` file with the following properties:

```properties
# Server configuration
server.port=9095

# Database configuration (example for MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/vaccination_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT configuration (if using JWT authentication)
jwt.secret=abcd123@789
jwt.expiration=86400000  # Token expiration in milliseconds (24 hours)
```

- **Notes**:
  - Replace `your_password` with your actual database password.
  - If using PostgreSQL, update the `spring.datasource.url`, `spring.datasource.driver-class-name`, and `spring.jpa.properties.hibernate.dialect` accordingly (e.g., `org.postgresql.Driver` and `org.hibernate.dialect.PostgreSQLDialect`).
  - The `jwt.secret` and `jwt.expiration` properties are only needed if you revert to JWT authentication.

### Running the Application

```bash
# Build and run the Spring Boot application
mvn spring-boot:run

# The app will be available at http://localhost:9095
```

Alternatively, if you’re using an IDE like IntelliJ IDEA or Eclipse:
1. Import the project as a Maven project.
2. Run the `VaccinationPortalApplication` class as a Java application.

## 📝 Usage

* Access the API documentation at the provided Postman link:  
  [Postman Documentation](https://documenter.getpostman.com/view/6868293/2sB2jAa7hR)

### Authentication (Current Setup - Basic Auth for Testing)

For testing purposes, the `/api/drives` endpoint uses Basic Auth with the following hardcoded credentials:

- **Username**: `cpremgurumukh@gmail.com`
- **Password**: `prem123`
- **Authorization Header**: `Basic Y3ByZW1ndXJ1bXVraEBnbWFpbC5jb206cHJlbTEyMw==`

### Authentication (Recommended for Production - JWT)

To secure the application in production:
1. Revert the Basic Auth setup in `SecurityConfig` to use JWT authentication.
2. Use the `/api/auth/login` endpoint to obtain a JWT token:

   - **Request**:
     - Method: POST
     - URI: `/api/auth/login`
     - Headers: `Content-Type: application/json`
     - Body:
       ```json
       {
         "email": "coordinator@example.com",
         "password": "password123"
       }
       ```
   - **Response**:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     }
     ```

3. Include the token in the `Authorization` header for protected endpoints (e.g., `/api/drives`):
   - Header: `Authorization: Bearer <token>`

## 🤝 Contributing

1. Fork the repository.
2. Create a new branch (`feature/new-feature`).
3. Make your changes.
4. Create a Pull Request.

## 📄 License

This project is licensed under the MIT License.
