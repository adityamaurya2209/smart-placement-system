# Smart Placement System

A web-based placement management system developed using Java Servlets, JSP, MySQL, Maven, and Apache Tomcat.

The system connects students and recruiters through a simple placement management workflow.

## Features

### Student Module

- Student Login
- Student Profile
- View Available Jobs
- Apply for Jobs
- View My Applications
- Track Application Status
- View Interview Schedule
- Access Online Interview Links

### Recruiter Module

- Recruiter Login
- Recruiter Dashboard
- View Job Applicants
- View Applicant Profiles
- Update Application Status
- Shortlist Applicants
- Move Applicants to Interview Stage
- Schedule Interviews
- View Applicant Count

## Application Workflow

```text
Student
   ↓
View Jobs
   ↓
Apply for Job
   ↓
APPLIED
   ↓
Recruiter Reviews Application
   ↓
SHORTLISTED
   ↓
INTERVIEW
   ↓
Interview Scheduled
   ↓
Student Views Interview Schedule
```

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 26 | Backend Development |
| Jakarta Servlets | Request Handling |
| JSP | Dynamic Web Pages |
| HTML | Frontend Structure |
| CSS | Styling |
| MySQL | Database |
| JDBC | Database Connectivity |
| Maven | Build & Dependency Management |
| Apache Tomcat 11 | Web Server |
| Git & GitHub | Version Control |

## Project Structure

```text
smart-placement-system/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── smartplacement/
│       │           ├── model/
│       │           ├── servlet/
│       │           └── util/
│       │
│       └── webapp/
│           ├── WEB-INF/
│           ├── applicants.jsp
│           ├── dashboard.html
│           ├── index.jsp
│           ├── interview-schedule.jsp
│           ├── jobs.jsp
│           ├── login.html
│           ├── my-applications.jsp
│           ├── profile.jsp
│           └── recruiter-dashboard.jsp
│
├── .gitignore
├── pom.xml
└── README.md
```

## Database

The application uses **MySQL** for storing placement-related data.

Main entities include:

- Users
- Students
- Recruiters
- Companies
- Jobs
- Applications
- Interviews

JDBC is used for communication between the Java application and MySQL database.

## Configuration

Database credentials are stored locally using environment variables.

Create a `.env` file in the project root:

```text
DB_URL=jdbc:mysql://localhost:3306/smart_placement
DB_USER=root
DB_PASSWORD=YOUR_MYSQL_PASSWORD
```

**Do not commit the `.env` file to GitHub.**

The `.env` file is excluded using `.gitignore`.

##  How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/adityamaurya2209/smart-placement-system.git
```

### 2. Create the Database

Open MySQL and create:

```sql
CREATE DATABASE smart_placement;
```

### 3. Configure Database Credentials

Create a local `.env` file:

```text
DB_URL=jdbc:mysql://localhost:3306/smart_placement
DB_USER=root
DB_PASSWORD=YOUR_MYSQL_PASSWORD
```

### 4. Build the Project

```bash
mvn clean package
```

### 5. Deploy to Apache Tomcat

Copy:

```text
target/smart-placement-system.war
```

to the Tomcat `webapps` directory.

### 6. Start Tomcat

Start Apache Tomcat 11.

### 7. Open the Application

```text
http://localhost:8080/smart-placement-system/
```

## Current Project Status

### Completed

- [x] Maven project setup
- [x] MySQL database connectivity
- [x] Student login
- [x] Student profile
- [x] Job listing
- [x] Job application
- [x] My Applications
- [x] Recruiter login
- [x] Recruiter dashboard
- [x] Applicant management
- [x] Application status updates
- [x] Applicant shortlisting
- [x] Interview scheduling
- [x] Student interview schedule
- [x] Git & GitHub setup

### Planned

- [ ] Admin Dashboard
- [ ] Admin User Management
- [ ] Admin Job Management
- [ ] Form Validation
- [ ] Improved UI/UX
- [ ] Security Improvements
- [ ] Placement Analytics

## Version Control

The project is developed incrementally using Git.

Major features are maintained as separate commits to keep the development history organized.

Example:

```text
feat: initialize Smart Placement System
docs: add project README
feat: add admin dashboard
feat: add user management
feat: improve UI
fix: resolve application issues
```

## Author

**Aditya Maurya**

GitHub:

https://github.com/adityamaurya2209