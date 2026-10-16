# Task 3 — Java Application using Gradle

A small Spring Boot Notes API, built with Gradle, containerized with Docker, and deployed automatically through the Jenkins setup from Task 2. Instead of treating this as a standalone exercise, I built it to plug into what I'd already set up — so Gradle builds the app, Jenkins runs the build and packages it into a Docker image, and the container gets redeployed automatically. All three tasks end up working as one pipeline instead of three separate demos.

## Stack
Java 21, Spring Boot (Web, Data JPA, H2 in-memory DB), Gradle (via the wrapper, so no local Gradle install needed), Docker, Jenkins.

## Endpoints
- `GET /notes` — list all notes
- `GET /notes/{id}` — get one note by id
- `POST /notes` — create a note

## Running it locally
```bash
./gradlew bootRun
curl http://localhost:8080/notes
```

## Running it in Docker
```bash
./gradlew build
docker build -t task3-notes-api .
docker run -d -p 8081:8080 --name task3-container task3-notes-api
curl http://localhost:8081/notes
```

## The CI/CD pipeline
The `Jenkinsfile` runs on the `local-agent` node from Task 2 (which I upgraded with Docker access — see the update note in [Task 2's README](../task2-jenkins-remoting/README.md)). Three stages: Gradle build, Docker image build, then stop/remove the old container and start the new one. It's set up in Jenkins as "Pipeline script from SCM," so it always builds whatever's actually on GitHub, not some local copy.

## A bug I ran into
The first time I Dockerized this, the container kept returning 404 on every route. Took a bit to realize why: I'd added the Controller and Repository classes *after* already running `./gradlew build` once, so the jar Docker was copying in was the old one from before those files existed. Rebuilding the jar (`./gradlew build` again) before rebuilding the image fixed it. Small thing, but a good reminder that Docker only ever copies whatever's already sitting in `build/libs/` — it doesn't know to regenerate it for you.

## Note on the database
Uses H2 in-memory, so data resets every time the container restarts. Fine for this task's scope, not something I'd ship to production as-is.
