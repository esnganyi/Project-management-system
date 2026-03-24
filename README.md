# Project-management-system
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

## Compile

```powershell
javac -d out (Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

## Run

```powershell
java -cp out pms.App
```

