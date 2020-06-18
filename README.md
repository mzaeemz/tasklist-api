# Task List API

*API for making a task list.*

## Database Setup
```bash
docker-compose up -d
```
You can connect to database through credentials being used in `./docker-compose.yml`

## Project Setup
```
Usage: ./scripts/run.sh [-c] [ -p PORT ]  
    -c to the project
    -p PORT to specify port (defaulted to 7850)

Example:
    ./scripts/run.sh -c -p 3456
```
You can also check by specifying -h flag
```
./scripts/run.sh -h
```

## Running Project
You can run project using:
```bash
sbt -Dhttp=7850 run
```
