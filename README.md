**Stock Trades API**

**Overview**
The Trade Management System is a Spring Boot-based RESTful service for managing trade operations and user authentication. It includes secure JWT-based authentication and provides APIs to perform operations like fetching, creating, and managing trades.

**Features**
User Authentication:

User registration (/auth/signup)
User login (/auth/login)
JWT-based authentication for securing endpoints.
Trade Management:

Fetch trade details by ID.
Fetch trades based on filters (type, user ID).
Create new trades.
JWT Security:

Secure APIs using JSON Web Token (JWT).
Token generation and validation for user sessions.
Exception Handling:

Proper handling of invalid inputs, resource not found, and other errors.

**Technologies Used**
Java 17
Spring Boot (Web, Security, Data JPA)
Spring Security for user authentication
JWT (JSON Web Token) for securing endpoints
H2 Database (for testing) or any relational DB
Lombok (to reduce boilerplate code)
Maven (for dependency management)
JUnit 5 and Mockito (for testing)
System Requirements
Java 17 or higher
Maven (to build the project)
Postman or any API testing tool (for testing APIs)
An IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code)

**Setup Instructions**

1. Clone the Repository
  
   git clone https://github.com/your-repository/trade-management.git
   cd trade-management
2. Configure the Application
   Update the application.yml file with your database and security configurations:

yaml

server:
port: 8080

spring:
datasource:
url: jdbc:h2:mem:testdb
driver-class-name: org.h2.Driver
username: sa
password: password
h2:
console:
enabled: true
jpa:
hibernate:
ddl-auto: update
show-sql: true

security:
jwt:
secret-key: your-secure-256-bit-key-in-base64
expiration-time: 86400000 # 1 day in milliseconds
Replace the secret-key with a valid base64-encoded 256-bit key.

3. Build the Project
   Use Maven to build the project:

    mvn clean install

4. Run the Application
   Run the application using Maven:

    mvn spring-boot:run 
The application will start on http://localhost:8080.


