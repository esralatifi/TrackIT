TrackIT — Habit & Progress Tracker (Spring Boot REST API)

TrackIT is a small Spring Boot REST API that lets you create habits, log daily progress, set a reminder time, and view basic statistics like weekly completion % and current streak.

This is a student project built to cover lots of core backend concepts in a clean, understandable way — without turning it into a huge system.

Why this project exists
This project intentionally keeps the scope simple (no authentication, no UI), but still demonstrates:
- clean Spring Boot layering (controller → service → repository)
- DTOs (request/response models)
- validation + consistent error handling
- filtering/searching/sorting
- real business rules + calculations
- basic unit/controller tests

Tech Stack
- Java 17
- Spring Boot 4.x
- Spring WebMVC (REST API)
- Spring Data JPA (Hibernate)
- Jakarta Validation
- H2 Database (file-based, so data persists across restarts)
- Gradle
- JUnit 5 + Mockito + Spring Boot Test

Project Structure (high level)
src/main/java/com/example/demo
controller/   -> REST endpoints
service/      -> business logic + calculations
repository/   -> JPA queries
dto/          -> request/response models
model/        -> entities + relationships
exception/    -> custom exceptions + global handler

Database Model (Entities & Relationships)
Entities (5 tables):
1. User
2. Category
3. Habit
4. HabitLog
5. Reminder

Relationships:
- User 1 → N Habit
- Category 1 → N Habit
- Habit 1 → N HabitLog
- Habit 1 → 1 Reminder

Business Rules
- A habit belongs to exactly one user and exactly one category
- A habit can have many logs, but only one HabitLog per habit per date (enforced by unique constraint + service check)

Authentication
There is no authentication (not required for the rubric).
Users are created and referenced by ID. This keeps the project focused on backend fundamentals.

Reminder Feature (what it is / what it isn’t)
The Reminder is stored as a time + enabled flag for a habit.
What it does:
- stores reminder settings
- returns them via API
  What it does not do (yet):
- send notifications
- run scheduled jobs
  That would be a great future upgrade (Spring Scheduler + email/push).

How to Run (from scratch)

Requirements
- Java 17 installed
- Git (optional)
- Windows PowerShell (commands below use PowerShell)

Clone or download
Option A: Clone
git clone <your-repo-url>
cd demo

Option B: Download ZIP
Download → unzip → open folder → open terminal in the project root.

Run the API
./gradlew bootRun

Server runs on:
http://localhost:8080

Health check:
GET http://localhost:8080/api/health

H2 Console (optional but useful)
http://localhost:8080/h2-console

Use:
JDBC URL: jdbc:h2:file:./data/trackit
Username: sa
Password: (empty)

Try the API (step-by-step demo flow)

Notes about commands
In PowerShell:
- Use Invoke-WebRequest for POST/PUT/DELETE
- Use curl.exe for GET

Step 1 — Create a Category
Invoke-WebRequest -Method Post -Uri "http://localhost:8080/api/categories" -ContentType "application/json" -Body '{"name":"Health"}'

Check categories:
curl.exe -i "http://localhost:8080/api/categories"

Step 2 — Create a User
Invoke-WebRequest -Method Post -Uri "http://localhost:8080/api/users" -ContentType "application/json" -Body '{"username":"esra","email":"esra@mail.com"}'

Check users:
curl.exe -i "http://localhost:8080/api/users"

You will see IDs. In examples below:
userId = 1
categoryId = 1

Step 3 — Create a Habit (belongs to user + category)
Invoke-WebRequest -Method Post -Uri "http://localhost:8080/api/users/1/habits" -ContentType "application/json" -Body '{"name":"Drink Water","description":"2 liters/day","goalPerWeek":7,"categoryId":1}'

List habits for a user:
curl.exe -i "http://localhost:8080/api/users/1/habits"

Filtering / search / sort examples
Search by name:
curl.exe -i "http://localhost:8080/api/users/1/habits?search=water"

Filter by category:
curl.exe -i "http://localhost:8080/api/users/1/habits?categoryId=1"

Sort:
curl.exe -i "http://localhost:8080/api/users/1/habits?sort=createdAt,desc"

Step 4 — Log Progress (HabitLog)
Create a log:
Invoke-WebRequest -Method Post -Uri "http://localhost:8080/api/habits/1/logs" -ContentType "application/json" -Body '{"date":"2026-01-19","status":"COMPLETED"}'

List logs (date range):
curl.exe -i "http://localhost:8080/api/habits/1/logs?from=2026-01-01&to=2026-01-31"

Duplicate log rule (business logic)
If you try the same date again:
Invoke-WebRequest -Method Post -Uri "http://localhost:8080/api/habits/1/logs" -ContentType "application/json" -Body '{"date":"2026-01-19","status":"COMPLETED"}'

You should get a 400 Bad Request because only one log per habit per day.

Step 5 — Reminder (stored setting)
Upsert reminder:
Invoke-WebRequest -Method Put -Uri "http://localhost:8080/api/habits/1/reminder" -ContentType "application/json" -Body '{"reminderTime":"08:30","enabled":true}'

Get reminder:
curl.exe -i "http://localhost:8080/api/habits/1/reminder"

Step 6 — Stats (calculations)
Stats endpoint returns:
- current streak (consecutive COMPLETED days)
- weekly completion % (completed vs goalPerWeek)

curl.exe -i "http://localhost:8080/api/habits/1/stats"

Optional weekStart:
curl.exe -i "http://localhost:8080/api/habits/1/stats?weekStart=2026-01-19"

API Endpoints (reference list)

Users
- POST /api/users
- GET /api/users
- GET /api/users/{id}
- PUT /api/users/{id}
- DELETE /api/users/{id}

Categories
- POST /api/categories
- GET /api/categories
- GET /api/categories/{id}
- PUT /api/categories/{id}
- DELETE /api/categories/{id}

Habits
- POST /api/users/{userId}/habits
- GET /api/users/{userId}/habits (filter/search/sort)
- GET /api/habits/{habitId}
- PUT /api/habits/{habitId}
- DELETE /api/habits/{habitId}

Habit Logs
- POST /api/habits/{habitId}/logs
- GET /api/habits/{habitId}/logs (date range + optional status)
- DELETE /api/logs/{logId}

Reminder
- PUT /api/habits/{habitId}/reminder
- GET /api/habits/{habitId}/reminder
- DELETE /api/habits/{habitId}/reminder

Stats
- GET /api/habits/{habitId}/stats

Testing
Tests are included (service + controller).

Run:
./gradlew test

Future Improvements (if someone continues this project)
- Add authentication (Spring Security + JWT)
- Add scheduled reminder notifications (email/push)
- Add pagination for list endpoints
- Add weekly goals history and charts
- Add a simple frontend UI
