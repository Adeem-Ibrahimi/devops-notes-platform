# DevOps Notes Platform

A Spring Boot notes API taken through containerization, CI/CD and
infrastructure as code. The app began as a CodeAlpha internship task and is
upgraded here for the Progree DevOps internship tasks. The app code is the
same; the Docker, pipeline and infrastructure files around it are new work.

## Task 2: Containerization

**What was built**
- Multi-stage Dockerfile: stage 1 (JDK + Gradle) compiles the jar, stage 2
  (JRE only) runs it. Build tools never reach the final image.
- Runs as a non-root user (appuser, uid 1001).
- docker-compose.yml runs the app with Postgres 16 and a named volume.
- Settings come from a .env file through environment variables. .env is
  git-ignored and docker-ignored; .env.example is the template.
- Postgres has no published port, so only the app can reach it. The app waits
  for the database healthcheck before starting.

**Run it**

    cd notes-api
    cp .env.example .env     # then set your own password
    docker compose up --build
    curl http://localhost:8080/notes

**Results** (screenshots in proof/task2/)
- Final image: 386 MB on disk (122 MB compressed)
- App connects to PostgreSQL through environment variables
- Notes survive docker compose down and up (data lives in the volume)


## Task 3: CI/CD pipeline

GitHub Actions workflow (.github/workflows/ci.yml) runs on every push to
main and on pull requests. Steps run in order, and a failure stops the rest:

1. Pull the code (actions/checkout)
2. Set up Java 21 with a Gradle cache
3. Lint with Checkstyle (main and test code)
4. Run the unit tests
5. Build the Docker image
6. Write a job summary (commit, branch, tests run, image size)

Checkstyle is configured in notes-api/config/checkstyle/checkstyle.xml. Its
first run on this code caught two real issues (a star import and tab
characters), which were fixed before the pipeline went green.

**Results** (screenshots in proof/task3/)
- Pipeline green on GitHub: 5 steps plus summary, about 2 minutes per run
- Job summary: 2 tests run, 0 failures
- Image size: 262 MB as reported in CI (Docker Desktop reports 386 MB for the
  same image; the two tools measure size differently)
