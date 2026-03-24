# Project Management System

This is a Java console application for managing course projects for lecturers and students.

## Features

- User login with username and password
- Lecturer project creation with project profile
- Milestone definition with target dates
- Student group registration by group leaders
- Student project view with milestones and deadlines
- Lecturer course project summary with group status
- Audit log for key actions

## Demo Accounts

- Lecturer: `lect1 / pass123`
- Lecturer: `lect2 / pass123`
- Group Leader: `leader1 / pass123`
- Group Leader: `leader2 / pass123`
- Student: `stud1 / pass123`
- Student: `stud2 / pass123`

## Project Structure

```text
src/
  pms/
    App.java
    model/
    service/
```

## Compile and Run on Windows / PowerShell

### Compile

```powershell
javac -d out (Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```
### Run

```powershell
java -cp out pms.App
```
## Compile and Run on GitHub Codespaces / Linux

### Compile

```bash
find src -name "*.java" | xargs javac -d out
```
### Run

```bash
java -cp out pms.App
```
## How to Use the System

1. Run the program.
2. Log in using one of the demo accounts.
3. If logged in as a lecturer, you can:
   - Create a new project
   - Add milestones
   - View projects for a course
   - View the audit log
4. If logged in as a student, you can:
   - View assigned projects
   - Register a group if you are a group leader

## Notes

- The system is console-based.
- The data is stored in memory during program execution.
- When the program closes, the data is reset.


