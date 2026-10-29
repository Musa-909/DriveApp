### Vision

Our goal with DriveApp was to create a backend system for a driving school that makes it easier to manage students, instructors, lessons, and bookings.

Students can register, log in, view available lessons, and make bookings, while instructors can manage students, lessons, and bookings.

We also integrated an AI driving assistant using Google Gemini to help students with questions about driving theory and traffic rules.

DriveApp was developed as our third-semester backend project to apply what we have learned about Java, REST APIs, databases, security, testing, and deployment.

---

### Project Links

| Resource | Description | Link |
|---|---|---|
| **GitHub Repository** | Complete source code, documentation, and project development. | [DriveApp Repository](https://github.com/Musa-909/DriveApp) |
| **Live Application** | Deployed REST API running on our production server. | [https://driveapp.website](https://driveapp.website) |
| **API Health Check** | Endpoint used to verify that the backend is running. | [GET /api/health](https://driveapp.website/api/health) |

---

### Group Project

DriveApp is a collaborative third-semester backend examination project developed by **Amaan Mahomed** and **Musa Sayed**. We worked together on designing, implementing, testing, documenting, and deploying the application.

| Team Member | Project Contribution | GitHub | Portfolio |
|---|---|---|---|
| **Amaan Mahomed** | Backend development, testing, documentation, and deployment | [@AmaanM3](https://github.com/AmaanM3) | [View Portfolio](https://amaanm3.github.io/portfolio2026/) |
| **Musa Sayed** | Backend development, testing, documentation, and deployment | [@Musa-909](https://github.com/Musa-909) | [View Portfolio](https://musa-909.github.io/portfolio2026/exam-portfolio/) |

---

### Relevant Course Topics

During our third semester, we have worked with different backend topics that we have used in DriveApp.

The table below shows the main course topics and how we have applied them in our project.

| Course Topic | How We Used It in DriveApp | Course Material |
|---|---|---|
| **01 JPA Basics** | We used Hibernate/JPA and DAO classes to store and manage data in PostgreSQL. | [JPA Basics](https://3sem.kursusmaterialer.dk/backend/jpa-basics/) |
| **02 JPA Relations** | We created relationships between students, instructors, lessons, and bookings. | [JPA Relations](https://3sem.kursusmaterialer.dk/backend/jpa-relations/) |
| **03 Data Integration** | We used DTOs, JSON, and Java HttpClient to exchange data and communicate with Google Gemini. | [Data Integration](https://3sem.kursusmaterialer.dk/backend/data-integration/) |
| **04 Concurrency** | We used `CompletableFuture` to handle asynchronous HTTP requests to Google Gemini. | [Concurrency](https://3sem.kursusmaterialer.dk/backend/concurrency/) |
| **05 REST API Intro** | We built our REST API using Javalin, routes, controllers, and HTTP methods. | [REST API Intro](https://3sem.kursusmaterialer.dk/backend/rest-intro/) |
| **06 REST & Test** | We used JUnit and REST Assured to test our API endpoints and functionality. | [REST & Test](https://3sem.kursusmaterialer.dk/backend/rest-test/) |
| **07 Security** | We implemented JWT authentication, BCrypt password hashing, and role-based authorization. | [Security](https://3sem.kursusmaterialer.dk/backend/security/) |
| **08 Deployment & DevOps** | We used GitHub Actions, Docker, Docker Hub, and DigitalOcean to deploy our backend. | [Deployment & DevOps](https://3sem.kursusmaterialer.dk/backend/deployment/) |

These topics helped us apply what we learned during the semester to a working backend application.

---

### Architecture

#### System Overview

DriveApp is built as a layered Java backend using Javalin, Hibernate/JPA, and PostgreSQL. We have separated the application into different layers to make the code easier to understand, maintain, and work with.

Javalin handles the HTTP requests and connects them to the correct routes and controllers. The controllers handle incoming data, validate requests, and manage the application logic. We use DAO classes to communicate with the database through Hibernate/JPA, while PostgreSQL stores information about students, instructors, lessons, and bookings.

We have also implemented JWT authentication and role-based authorization to protect our endpoints and control which features students and instructors can access.

The application includes an AI driving assistant, where students can ask questions about driving theory and traffic rules. These requests are handled by `DrivingAiService`, which communicates with the Google Gemini API.

### Technologies

- Java 17
- Maven
- Javalin
- Hibernate / JPA
- PostgreSQL
- HikariCP
- Jackson
- Lombok
- JWT & BCrypt
- Google Gemini API
- JUnit & REST Assured
- Testcontainers
- Docker & Docker Hub
- GitHub Actions
- DigitalOcean
- Caddy & Watchtower
- DanDomain (Domain & DNS)

---

### Architecture Diagram

The architecture diagram shows how the different parts of DriveApp work together. It gives an overview of how HTTP requests are handled, how data is stored in the database, and how our AI assistant connects to Google Gemini.

![DriveApp Architecture Diagram](DOCS/architecture.png)

We use Javalin to handle HTTP requests, controllers to manage application logic, and DAO classes with Hibernate/JPA to communicate with PostgreSQL.

JWT authentication protects our endpoints. Registration, login, and health checks are public, while the other endpoints require a valid token. Our AI assistant is handled through `DrivingAiService`, which communicates with the Google Gemini API.


---

### Key Design Decisions

#### Layered Architecture and DTOs

We separated our code into routes, controllers, and DAO classes to keep the project organized. We also use DTOs to control what data is sent and received through our API, so sensitive information like passwords is not returned.

#### Authentication and Roles

We use BCrypt to hash passwords and JWT for authentication. Students and instructors have different roles, which determine what they are allowed to access. Students can only manage their own profile, while instructors have access to administrative features.

#### Bookings and Availability

Students can book available driving lessons. Before creating a booking, we check that the student and lesson exist and that the lesson has not already been booked. Available lessons are found by checking which lessons have no bookings.

#### AI Integration

We integrated Google Gemini through our `DrivingAiService` class. We use Java HttpClient and `CompletableFuture` to send questions to Gemini and receive answers. The API waits for the AI response before returning it to the user.

#### Error Handling and Logging

We use HTTP status codes, validation, and JSON error messages to handle invalid requests. We also use logging to keep track of requests and unexpected errors.

---

### Data Model

### Entity Relationship Diagram (ERD)

The ERD shows how our PostgreSQL database is structured, including the tables, attributes, and relationships between students, instructors, lessons, and bookings.

![DriveApp ER Diagram](DOCS/ERD.png)

### Domain Model

The domain model shows the main concepts in our driving school system and how they are connected, without focusing on database details.

![DriveApp Domain Model](DOCS/domain-model.png)

### Important Entities

| Entity | Purpose | Relationships |
|---|---|---|
| **Student** | Stores student information, login details, and contact information. | Can have an instructor and multiple bookings. |
| **Instructor** | Stores instructor information and login details. | Can have multiple students and lessons. |
| **Lesson** | Represents a driving lesson with a time, duration, and lesson type. | Can have an instructor and be connected to bookings. |
| **Booking** | Connects a student to a driving lesson. | Must reference a student and a lesson. |

---

### API Documentation

DriveApp provides a REST API for managing students, instructors, driving lessons, and bookings. The API also includes an AI driving assistant that can answer questions about driving theory and traffic rules.

**Local URL:** `http://localhost:7070`

**Production URL:** `https://driveapp.website`

#### Endpoint Overview

| Area | Main Endpoints | Access |
|---|---|---|
| **Health** | `GET /api/health` | Public |
| **Registration & Login** | `POST /api/students`, `POST /api/students/login`, `POST /api/instructors/login` | Public |
| **Students** | `GET /api/students`, `GET/PUT/DELETE /api/students/{id}` | JWT, role and ownership checks |
| **Instructors** | `GET/POST /api/instructors`, `GET/PUT/DELETE /api/instructors/{id}` | Instructor |
| **Lessons** | `GET/POST /api/lessons`, `GET/PUT/DELETE /api/lessons/{id}`, `GET /api/lessons/available` | JWT, instructor required for changes |
| **Bookings** | `GET/POST /api/bookings`, `GET/PUT/DELETE /api/bookings/{id}` | JWT, role and ownership checks |
| **AI Assistant** | `POST /api/ai/driving-advice` | JWT |

We use JWT authentication to protect our endpoints. Students can view and update their own profiles, while instructors can manage student information, lessons, and bookings.

Students can also view available driving lessons and create bookings for themselves. An instructor account is required to create, update, or delete driving lessons.

#### Example Endpoints

Below are some examples of how we use the main endpoints in DriveApp.

#### 1. Student Login

Students can log in using their email and password. If the login is successful, the API returns a JWT token.

**Request:**

```http
POST /api/students/login
Content-Type: application/json
```

```json
{
  "email": "student@example.com",
  "password": "your-password"
}
```

**Response – 200 OK:**

```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

The token must be included in the header when accessing protected endpoints:

```http
Authorization: Bearer <token>
```

An incorrect email or password returns `401 Unauthorized`.

#### 2. View Available Lessons

Students can see which driving lessons are available before making a booking.

**Request:**

```http
GET /api/lessons/available
Authorization: Bearer <token>
```

**Response – 200 OK**

The API returns a JSON list of lessons that have not been booked yet.

#### 3. Book a Driving Lesson

A student can book an available driving lesson by sending a booking request.

**Request:**

```http
POST /api/bookings
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "bookingTime": "2026-11-10T10:00:00",
  "studentId": 1,
  "lessonId": 2
}
```

**Response – 201 Created:**

```json
{
  "id": 1,
  "bookingTime": "2026-11-10T10:00:00",
  "studentId": 1,
  "lessonId": 2
}
```

Before saving the booking, the API checks that the student and lesson exist and that the lesson is not already booked.

If the lesson has already been booked, the API returns `400 Bad Request`.

#### 4. Ask the AI Driving Assistant

Students can ask the AI assistant questions about driving theory, traffic rules, and road safety.

**Request:**

```http
POST /api/ai/driving-advice
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "question": "What does a triangular traffic sign mean?"
}
```

**Response – 200 OK:**

```json
{
  "answer": "A triangular traffic sign is usually a warning sign that alerts drivers to possible dangers ahead."
}
```

The question is sent to Google Gemini through our `DrivingAiService`, and the generated answer is returned as JSON.

---

### HTTP Status Codes

We use HTTP status codes to indicate whether a request was successful or if something went wrong.

| Status Code | How We Use It in DriveApp |
|---|---|
| `200 OK` | Returned when data is retrieved or updated successfully, or when a user logs in. |
| `201 Created` | Returned when a student, instructor, lesson, or booking is created. |
| `204 No Content` | Returned when a student, instructor, lesson, or booking is deleted. |
| `400 Bad Request` | Returned when a request contains invalid data, an AI question is empty, or a lesson is already booked. |
| `401 Unauthorized` | Returned when login credentials are incorrect or a required JWT is missing or invalid. |
| `403 Forbidden` | Returned when a user does not have permission to perform an action or access another student's information. |
| `404 Not Found` | Returned when a requested student, instructor, lesson, or booking does not exist. |
| `500 Internal Server Error` | Returned when an unexpected error occurs in the application. |

---

### User Stories

We created 10 user stories to describe the main features of DriveApp from the perspective of students and instructors.

The user stories helped us plan the project and decide which features to focus on. We prioritized the most important functionality, such as registration, login, driving lessons, and bookings, while also including profile management and our AI driving assistant.

| ID | User Story |
|---|---|
| US-01 | As a student, I want to register an account, so I can access the driving school's services. |
| US-02 | As a student or instructor, I want to log in securely, so I can access protected features of the application. |
| US-03 | As a student, I want to view available driving lessons, so I can find a lesson that fits my schedule. |
| US-04 | As a student, I want to book a driving lesson, so I can reserve my preferred time. |
| US-05 | As a student, I want to view and update my own profile securely, so I can keep my personal information accurate and protected. |
| US-06 | As an instructor, I want to view, update and delete student information, so I can keep student records organized and up to date. |
| US-07 | As an instructor, I want to create, update and delete driving lessons, so I can organize the driving school's lessons. |
| US-08 | As an instructor, I want to view and manage student bookings, so I can keep track of scheduled driving lessons. |
| US-09 | As an instructor, I want to view and manage instructor information, so I can keep instructor records up to date. |
| US-10 | As a student, I want to ask an AI assistant questions about driving theory and traffic rules, so I can receive relevant driving advice. |

---

### Development Notes

#### Development and Database Setup

DriveApp uses Java 17, Maven, PostgreSQL, and Docker.

Our project uses two main database environments:

1. **Production Database:** Our deployed application uses a PostgreSQL database configured through environment variables on our production server.
2. **Automated Testing:** Our DAO and API tests use temporary PostgreSQL 16 databases created automatically through Testcontainers.

This setup allows us to test our application without manually creating or maintaining a local test database.

#### Requirements

To run the automated tests, we need:

1. JDK 17
2. Maven
3. Docker Desktop or another compatible Docker environment
4. IntelliJ IDEA
5. 

#### Running the Application Locally (Optional)

DriveApp can also be started locally using `Main.java` in IntelliJ IDEA or by runnning the application through Maven.

This requires a running local PostgreSQL database configured through `src/main/resources/config.properties`:

```properties
DB_NAME=driveapp_test
DB_USERNAME=postgres
DB_PASSWORD=your-local-password
```

The database must exist, and the credentials must match the local PostgreSQL setup.

The application also requires `JWT_SECRET` for authentication and `GEMINI_API_KEY` when using the AI assistant.

The `DEPLOYED` environment variable should not be set when running the application locally, as this would make the application use the deployed database configuration.

We can build and start the application using:

```bash
mvn clean package -DskipTests
java -jar target/app.jar
```

The application runs on:

`http://localhost:7070`

We can check if the API is running through:

`http://localhost:7070/api/health`

This setup is only needed if you want to run and test the application locally using `config.properties`. It is not required for automated tests, since Testcontainers handles the test databases automatically.

#### Production Environment

Our application is deployed using Docker on a DigitalOcean server.

The production database connection is configured through environment variables, including `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, and `CONNECTION_STR`.

JWT authentication and our Gemini integration use `JWT_SECRET` and `GEMINI_API_KEY`.

The deployed REST API is available at:

https://driveapp.website

We can verify that the application is running through:

https://driveapp.website/api/health

#### Creating the First Instructor

Creating an instructor through `POST /api/instructors` requires an existing instructor account with a valid JWT token.

Therefore, the first instructor must be added directly to the database with a BCrypt-hashed password.

Once the account has been created, the instructor can log in through `POST /api/instructors/login` and create additional instructor accounts through the API.

---

### Tests

We use **JUnit, REST Assured, and Testcontainers** to test DriveApp and verify that our backend works as expected.

Our tests cover different parts of the application:

- **API Tests:** Testing endpoints for students, instructors, lessons, bookings, and our AI assistant. The tests start Javalin automatically on an available port and create their own test users.
- **DAO Tests:** Testing database operations such as creating, retrieving, updating, and deleting data against temporary PostgreSQL databases.
- **Security Tests:** Testing JWT authentication, BCrypt password hashing, and role-based authorization.
- **AI Tests:** `AiApiTest` uses a fake AI response to test the endpoint without calling Gemini. `DrivingAiServiceTest` calls the real Gemini API.

`HibernateTestConfig` configures the container database, `DatabaseTest` starts and stops PostgreSQL, and `ApiTest` starts and stops Javalin for the standard API test classes. `AiApiTest` starts Javalin separately to inject its fake AI service.

#### Improvements to Our Testing Setup

At the beginning of the project, our API tests depended on a manually started Javalin server running on localhost:7070 and an existing PostgreSQL database. We also used environment variables such as `TEST_INSTRUCTOR_EMAIL`, `TEST_INSTRUCTOR_PASSWORD`, `TEST_STUDENT_EMAIL`, and `TEST_STUDENT_PASSWORD` to log in with predefined test accounts.

This setup required more manual configuration and made it harder to run the tests on different computers.

Later, we improved our testing setup by integrating Testcontainers into our automated tests. Our DAO and API tests now use temporary PostgreSQL databases that are automatically started through Docker. We also created shared `DatabaseTest` and `ApiTest` classes to manage the database and start Javalin automatically on an available port.

The tests now create their own test users, so we no longer need predefined accounts or their environment variables.

These changes made our tests more independent, reliable, and easier to run. They also reduced the amount of manual setup and made it easier to run the complete test suite locally and through GitHub Actions.

#### Automated Testing with GitHub Actions

We use GitHub Actions to automatically build and test DriveApp whenever changes are pushed to the `main` branch. The workflow can also be started manually.

Our testing workflow:

1. Checks out the project and sets up Java 17.
2. Runs `mvn clean test` with `JWT_SECRET` and `GEMINI_API_KEY` provided through GitHub Actions secrets.
3. Testcontainers starts temporary PostgreSQL 16 databases, and the API tests start and stop their own Javalin servers and create test users.
4. If the tests pass, Maven builds the JAR and GitHub Actions builds and pushes the Docker image to Docker Hub.

The temporary test databases are separate from production, so automated tests do not affect real application data.

#### Running Tests Locally

To run the tests locally, install Java 17, Maven, and Docker, and make sure Docker Desktop (or another compatible Docker daemon) is running. You do **not** need to start PostgreSQL, create test users, or start `Main.java` on port `7070`.

For the complete test suite, set these environment variables with your own values:

```env
JWT_SECRET=<test-jwt-secret>
GEMINI_API_KEY=<gemini-api-key>
```

Then run:

```bash
mvn test
```

`JWT_SECRET` is required for JWT and API tests; `GEMINI_API_KEY` is only required by `DrivingAiServiceTest`, which calls the real Gemini API. DAO tests and `BcryptTest` need neither variable. Do not use production database credentials or the old `TEST_INSTRUCTOR_*` / `TEST_STUDENT_*` variables for these tests. Never commit real secrets to Git.

This allows us to test our application locally before pushing changes to GitHub.

By using automated testing in our CI/CD pipeline, we can verify that our backend works correctly before a new Docker image is pushed to Docker Hub and deployed through Watchtower.

---

### CI/CD and Deployment

We use GitHub Actions, Docker, Docker Hub, and DigitalOcean to build, test, and deploy DriveApp.

#### CI/CD Pipeline

When we push changes to `main`, GitHub Actions:

1. Sets up Java 17 and runs `mvn clean test`.
2. Lets Testcontainers create temporary PostgreSQL databases and the API tests launch Javalin automatically.
3. Builds the JAR and pushes the Docker image to Docker Hub (`amaanm3/driveapp:latest`) only if the tests pass.

---

### Deployment

Our backend runs in Docker on a DigitalOcean server. Watchtower automatically checks Docker Hub for new images and updates the application.

We use **DanDomain** for our domain name and DNS settings, connecting `driveapp.website` to our server. **Caddy** provides HTTPS for secure access to the REST API.

**Live API:** https://driveapp.website

**Health Check:** https://driveapp.website/api/health

Passwords, database credentials, and API keys are configured through environment variables and GitHub Secrets.

---

### Lessons Learned

During the development of DriveApp, we gained experience with different backend technologies and working together as a team.

- **Backend Development:** Structuring our API with Javalin, controllers, DTOs, and DAOs.
- **Database:** Working with PostgreSQL, Hibernate/JPA, and entity relationships.
- **Security:** Implementing JWT authentication, BCrypt, and role-based authorization.
- **Testing:** Writing isolated API and database tests using JUnit, REST Assured, and Testcontainers.
- **Deployment:** Setting up CI/CD with GitHub Actions, Docker, and DigitalOcean.
- **AI Integration:** Connecting our backend to Google Gemini.
- **Teamwork:** Collaborating through GitHub and solving problems together.

Overall, the project helped us understand how to build, test, and deploy a complete backend application.
