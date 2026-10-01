# REST API Documentation

## Base URL

```text
http://localhost:7070
```

## Endpoints

| Method | URL | Request Body | Success Response | Errors |
|---|---|---|---|---|
| GET | `/api/health` | None | `200 OK` | `500 Internal Server Error` |
| GET | `/api/students` | None | `200 OK` | `500 Internal Server Error` |
| GET | `/api/students/{id}` | None | `200 OK` | `400 Bad Request`, `404 Not Found` |
| POST | `/api/students` | Student request | `201 Created` | `400 Bad Request`, `500 Internal Server Error` |
| PUT | `/api/students/{id}` | Student request | `200 OK` | `400 Bad Request`, `404 Not Found` |
| DELETE | `/api/students/{id}` | None | `204 No Content` | `400 Bad Request`, `404 Not Found` |
| GET | `/api/instructors` | None | `200 OK` | `500 Internal Server Error` |
| GET | `/api/instructors/{id}` | None | `200 OK` | `400 Bad Request`, `404 Not Found` |
| POST | `/api/instructors` | Instructor request | `201 Created` | `400 Bad Request`, `500 Internal Server Error` |
| PUT | `/api/instructors/{id}` | Instructor request | `200 OK` | `400 Bad Request`, `404 Not Found` |
| DELETE | `/api/instructors/{id}` | None | `204 No Content` | `400 Bad Request`, `404 Not Found` |
| GET | `/api/lessons` | None | `200 OK` | `500 Internal Server Error` |
| GET | `/api/lessons/{id}` | None | `200 OK` | `400 Bad Request`, `404 Not Found` |
| POST | `/api/lessons` | Lesson request | `201 Created` | `400 Bad Request`, `404 Not Found`, `500 Internal Server Error` |
| PUT | `/api/lessons/{id}` | Lesson request | `200 OK` | `400 Bad Request`, `404 Not Found` |
| DELETE | `/api/lessons/{id}` | None | `204 No Content` | `400 Bad Request`, `404 Not Found` |
| GET | `/api/bookings` | None | `200 OK` | `500 Internal Server Error` |
| GET | `/api/bookings/{id}` | None | `200 OK` | `400 Bad Request`, `404 Not Found` |
| POST | `/api/bookings` | Booking request | `201 Created` | `400 Bad Request`, `404 Not Found`, `500 Internal Server Error` |
| PUT | `/api/bookings/{id}` | Booking request | `200 OK` | `400 Bad Request`, `404 Not Found` |
| DELETE | `/api/bookings/{id}` | None | `204 No Content` | `400 Bad Request`, `404 Not Found` |

---

# Health Check

Checks whether the REST API is running.

## Request

```text
GET /api/health
```

## Response

**200 OK**

```json
{
  "status": "ok",
  "message": "API is running"
}
```

---

# Student API

## Get All Students

Returns all students stored in the database.

### Request

```text
GET /api/students
```

### Response

**200 OK**

Example:

```json
[
  {
    "id": 1,
    "name": "Musa Sayed",
    "email": "musa@example.com",
    "phoneNumber": "12345678"
  },
  {
    "id": 2,
    "name": "Amaan Mahomed",
    "email": "amaan@example.com",
    "phoneNumber": "87654321"
  }
]
```

---

## Get Student By ID

Returns a student with the given ID.

### Request

```text
GET /api/students/{id}
```

Example:

```text
GET /api/students/1
```

### Response

**200 OK**

```json
{
  "id": 1,
  "name": "Musa Sayed",
  "email": "musa@example.com",
  "phoneNumber": "12345678"
}
```

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if no student exists with the given ID.

```json
{
  "status": 404,
  "message": "Student not found"
}
```

---

## Create Student

Creates and stores a new student.

### Request

```text
POST /api/students
```

**Content-Type: application/json**

```json
{
  "name": "Musa Sayed",
  "email": "musa@example.com",
  "phoneNumber": "12345678"
}
```

### Response

**201 Created**

```json
{
  "id": 1,
  "name": "Musa Sayed",
  "email": "musa@example.com",
  "phoneNumber": "12345678"
}
```

### Validation

- `name` must not be empty
- `email` must not be empty
- `phoneNumber` must not be empty

Invalid input returns:

```text
400 Bad Request
```

---

## Update Student

Updates an existing student.

### Request

```text
PUT /api/students/{id}
```

Example:

```text
PUT /api/students/1
```

**Content-Type: application/json**

```json
{
  "name": "Musa Sayed",
  "email": "musa.sayed@example.com",
  "phoneNumber": "11223344"
}
```

### Response

**200 OK**

```json
{
  "id": 1,
  "name": "Musa Sayed",
  "email": "musa.sayed@example.com",
  "phoneNumber": "11223344"
}
```

### Errors

**400 Bad Request**

Returned if the ID or request body is invalid.

**404 Not Found**

Returned if the student does not exist.

```json
{
  "status": 404,
  "message": "Student not found"
}
```

---

## Delete Student

Deletes a student from the database.

### Request

```text
DELETE /api/students/{id}
```

Example:

```text
DELETE /api/students/1
```

### Response

**204 No Content**

No response body is returned.

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if the student does not exist.

```json
{
  "status": 404,
  "message": "Student not found"
}
```

---

## Student JSON Format

### Request DTO

```json
{
  "name": "Musa Sayed",
  "email": "musa@example.com",
  "phoneNumber": "12345678"
}
```

### Response DTO

```json
{
  "id": 1,
  "name": "Musa Sayed",
  "email": "musa@example.com",
  "phoneNumber": "12345678"
}
```

---

# Instructor API

## Get All Instructors

Returns all instructors stored in the database.

### Request

```text
GET /api/instructors
```

### Response

**200 OK**

Example:

```json
[
  {
    "id": 1,
    "name": "Amaan Mahomed",
    "email": "amaan@drivingschool.dk",
    "phoneNumber": "87654321"
  }
]
```

---

## Get Instructor By ID

Returns an instructor with the given ID.

### Request

```text
GET /api/instructors/{id}
```

Example:

```text
GET /api/instructors/1
```

### Response

**200 OK**

```json
{
  "id": 1,
  "name": "Amaan Mahomed",
  "email": "amaan@drivingschool.dk",
  "phoneNumber": "87654321"
}
```

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if no instructor exists with the given ID.

```json
{
  "status": 404,
  "message": "Instructor not found"
}
```

---

## Create Instructor

Creates and stores a new instructor.

### Request

```text
POST /api/instructors
```

**Content-Type: application/json**

```json
{
  "name": "Amaan Mahomed",
  "email": "amaan@drivingschool.dk",
  "phoneNumber": "87654321"
}
```

### Response

**201 Created**

```json
{
  "id": 1,
  "name": "Amaan Mahomed",
  "email": "amaan@drivingschool.dk",
  "phoneNumber": "87654321"
}
```

### Validation

- `name` must not be empty
- `email` must not be empty
- `phoneNumber` must not be empty

Invalid input returns:

```text
400 Bad Request
```

---

## Update Instructor

Updates an existing instructor.

### Request

```text
PUT /api/instructors/{id}
```

Example:

```text
PUT /api/instructors/1
```

**Content-Type: application/json**

```json
{
  "name": "Amaan Mahomed",
  "email": "amaan.mahomed@drivingschool.dk",
  "phoneNumber": "88776655"
}
```

### Response

**200 OK**

```json
{
  "id": 1,
  "name": "Amaan Mahomed",
  "email": "amaan.mahomed@drivingschool.dk",
  "phoneNumber": "88776655"
}
```

### Errors

**400 Bad Request**

Returned if the ID or request body is invalid.

**404 Not Found**

Returned if the instructor does not exist.

```json
{
  "status": 404,
  "message": "Instructor not found"
}
```

---

## Delete Instructor

Deletes an instructor from the database.

### Request

```text
DELETE /api/instructors/{id}
```

Example:

```text
DELETE /api/instructors/1
```

### Response

**204 No Content**

No response body is returned.

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if the instructor does not exist.

```json
{
  "status": 404,
  "message": "Instructor not found"
}
```

---

## Instructor JSON Format

### Request DTO

```json
{
  "name": "Amaan Mahomed",
  "email": "amaan@drivingschool.dk",
  "phoneNumber": "87654321"
}
```

### Response DTO

```json
{
  "id": 1,
  "name": "Amaan Mahomed",
  "email": "amaan@drivingschool.dk",
  "phoneNumber": "87654321"
}
```

---

# Lesson API

## Get All Lessons

Returns all lessons stored in the database.

### Request

```text
GET /api/lessons
```

### Response

**200 OK**

Example:

```json
[
  {
    "id": 1,
    "lessonTime": "2026-10-05T14:00:00",
    "durationMinutes": 45,
    "lessonType": "PRACTICAL",
    "instructorId": 1
  }
]
```

---

## Get Lesson By ID

Returns a lesson with the given ID.

### Request

```text
GET /api/lessons/{id}
```

Example:

```text
GET /api/lessons/1
```

### Response

**200 OK**

```json
{
  "id": 1,
  "lessonTime": "2026-10-05T14:00:00",
  "durationMinutes": 45,
  "lessonType": "PRACTICAL",
  "instructorId": 1
}
```

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if no lesson exists with the given ID.

```json
{
  "status": 404,
  "message": "Lesson not found"
}
```

---

## Create Lesson

Creates and stores a new lesson.

The `instructorId` must refer to an existing instructor.

### Request

```text
POST /api/lessons
```

**Content-Type: application/json**

```json
{
  "lessonTime": "2026-10-05T14:00:00",
  "durationMinutes": 45,
  "lessonType": "PRACTICAL",
  "instructorId": 1
}
```

### Response

**201 Created**

```json
{
  "id": 1,
  "lessonTime": "2026-10-05T14:00:00",
  "durationMinutes": 45,
  "lessonType": "PRACTICAL",
  "instructorId": 1
}
```

### Validation

- `lessonTime` is required
- `durationMinutes` must be greater than 0
- `lessonType` is required
- `instructorId` must be greater than 0

Invalid input returns:

```text
400 Bad Request
```

If the instructor does not exist:

```text
404 Not Found
```

```json
{
  "status": 404,
  "message": "Instructor not found"
}
```

---

## Update Lesson

Updates an existing lesson.

### Request

```text
PUT /api/lessons/{id}
```

Example:

```text
PUT /api/lessons/1
```

**Content-Type: application/json**

```json
{
  "lessonTime": "2026-10-05T15:00:00",
  "durationMinutes": 60,
  "lessonType": "PRACTICAL",
  "instructorId": 1
}
```

### Response

**200 OK**

```json
{
  "id": 1,
  "lessonTime": "2026-10-05T15:00:00",
  "durationMinutes": 60,
  "lessonType": "PRACTICAL",
  "instructorId": 1
}
```

### Errors

**400 Bad Request**

Returned if the ID or request body is invalid.

**404 Not Found**

Returned if the lesson or instructor does not exist.

```json
{
  "status": 404,
  "message": "Lesson not found"
}
```

or:

```json
{
  "status": 404,
  "message": "Instructor not found"
}
```

---

## Delete Lesson

Deletes a lesson from the database.

### Request

```text
DELETE /api/lessons/{id}
```

Example:

```text
DELETE /api/lessons/1
```

### Response

**204 No Content**

No response body is returned.

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if the lesson does not exist.

```json
{
  "status": 404,
  "message": "Lesson not found"
}
```

---

## Lesson JSON Format

### Request DTO

```json
{
  "lessonTime": "2026-10-05T14:00:00",
  "durationMinutes": 45,
  "lessonType": "PRACTICAL",
  "instructorId": 1
}
```

### Response DTO

```json
{
  "id": 1,
  "lessonTime": "2026-10-05T14:00:00",
  "durationMinutes": 45,
  "lessonType": "PRACTICAL",
  "instructorId": 1
}
```

---

# Booking API

## Get All Bookings

Returns all bookings stored in the database.

### Request

```text
GET /api/bookings
```

### Response

**200 OK**

Example:

```json
[
  {
    "id": 1,
    "bookingTime": "2026-10-02T18:00:00",
    "studentId": 1,
    "lessonId": 1
  }
]
```

---

## Get Booking By ID

Returns a booking with the given ID.

### Request

```text
GET /api/bookings/{id}
```

Example:

```text
GET /api/bookings/1
```

### Response

**200 OK**

```json
{
  "id": 1,
  "bookingTime": "2026-10-02T18:00:00",
  "studentId": 1,
  "lessonId": 1
}
```

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if no booking exists with the given ID.

```json
{
  "status": 404,
  "message": "Booking not found"
}
```

---

## Create Booking

Creates and stores a new booking.

The `studentId` must refer to an existing student, and the `lessonId` must refer to an existing lesson.

A lesson cannot be booked if it is already booked.

### Request

```text
POST /api/bookings
```

**Content-Type: application/json**

```json
{
  "bookingTime": "2026-10-02T18:00:00",
  "studentId": 1,
  "lessonId": 1
}
```

### Response

**201 Created**

```json
{
  "id": 1,
  "bookingTime": "2026-10-02T18:00:00",
  "studentId": 1,
  "lessonId": 1
}
```

### Validation

- `bookingTime` is required
- `studentId` must be greater than 0
- `lessonId` must be greater than 0

Invalid input returns:

```text
400 Bad Request
```

If the lesson is already booked:

```text
400 Bad Request
```

```json
{
  "status": 400,
  "message": "Lesson is already booked"
}
```

If the student does not exist:

```text
404 Not Found
```

```json
{
  "status": 404,
  "message": "Student not found"
}
```

If the lesson does not exist:

```text
404 Not Found
```

```json
{
  "status": 404,
  "message": "Lesson not found"
}
```

---

## Update Booking

Updates an existing booking.

### Request

```text
PUT /api/bookings/{id}
```

Example:

```text
PUT /api/bookings/1
```

**Content-Type: application/json**

```json
{
  "bookingTime": "2026-10-03T10:00:00",
  "studentId": 1,
  "lessonId": 1
}
```

### Response

**200 OK**

```json
{
  "id": 1,
  "bookingTime": "2026-10-03T10:00:00",
  "studentId": 1,
  "lessonId": 1
}
```

### Errors

**400 Bad Request**

Returned if the ID or request body is invalid.

**404 Not Found**

Returned if the booking, student, or lesson does not exist.

Example:

```json
{
  "status": 404,
  "message": "Booking not found"
}
```

---

## Delete Booking

Deletes a booking from the database.

### Request

```text
DELETE /api/bookings/{id}
```

Example:

```text
DELETE /api/bookings/1
```

### Response

**204 No Content**

No response body is returned.

### Errors

**400 Bad Request**

Returned if the ID is invalid.

**404 Not Found**

Returned if the booking does not exist.

```json
{
  "status": 404,
  "message": "Booking not found"
}
```

---

## Booking JSON Format

### Request DTO

```json
{
  "bookingTime": "2026-10-02T18:00:00",
  "studentId": 1,
  "lessonId": 1
}
```

### Response DTO

```json
{
  "id": 1,
  "bookingTime": "2026-10-02T18:00:00",
  "studentId": 1,
  "lessonId": 1
}
```

---

# Error Response Format

Errors are returned as JSON with a status code and message.

Example:

```json
{
  "status": 404,
  "message": "Student not found"
}
```

An unexpected server-side error returns:

```json
{
  "status": 500,
  "message": "Internal server error"
}
```

---

# Status Codes

| Status Code | Meaning |
|---|---|
| `200 OK` | Request completed successfully |
| `201 Created` | A new resource was created successfully |
| `204 No Content` | A resource was deleted successfully |
| `400 Bad Request` | Request data or a path parameter was invalid |
| `404 Not Found` | Requested resource could not be found |
| `500 Internal Server Error` | An unexpected server-side error occurred |