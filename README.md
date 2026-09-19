# Smart Placement System

A role-based web application developed to streamline the campus placement process for Students, Recruiters, and Administrators.

The system provides a centralized platform for managing students, companies, jobs, applications, applicant evaluation, interviews, and placement-related activities.

---

## 🚀 Features

### 👨‍🎓 Student Module

- Student login and authentication
- Role-based access control
- Student profile management
- View personal academic and contact information
- Browse available job opportunities
- View detailed job information
- Apply for jobs
- Duplicate application prevention
- View submitted applications
- Track application status
- View interview schedules
- Access online interview meeting links
- Session-based logout
- Unauthorized-access protection

### 🏢 Recruiter Module

- Recruiter login and authentication
- Recruiter dashboard
- Company profile management/view
- Create, edit, close and reopen job postings
- View recruiter-owned jobs and applicant counts
- View, search and filter applicants
- View detailed applicant profiles
- CGPA and branch eligibility checking
- Skill-match score calculation
- Update application status and shortlist applicants
- Schedule, reschedule, complete and cancel interviews
- Online meeting links and offline venue management
- Recruiter ownership validation

### 👨‍💼 Administrator Module

- Admin authentication and dashboard
- Dashboard statistics
- Student management
- Recruiter management
- Company management
- Job management
- Application management
- Interview management
- Detailed record views
- Recruiter deletion protection
- Role-based access control
- Unauthorized-access protection

---

## 🔄 Application Workflow

```text
Student
   │
   ▼
Login
   │
   ▼
Student Profile
   │
   ▼
Browse Jobs
   │
   ▼
View Job Details
   │
   ▼
Apply for Job
   │
   ▼
APPLIED
   │
   ▼
Recruiter Reviews Application
   │
   ├──────────────► REJECTED
   │
   ▼
SHORTLISTED
   │
   ▼
INTERVIEW
   │
   ▼
Interview Scheduled
   │
   ├──────────────► CANCELLED
   │
   ▼
COMPLETED
   │
   ▼
SELECTED / REJECTED
```

---

## 🏗️ System Architecture

```text
                    Web Browser
                         │
                         ▼
              ┌─────────────────────┐
              │    HTML / CSS / JS  │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │        JSP          │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │   Java Servlets     │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │        JDBC         │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │       MySQL         │
              └─────────────────────┘
```

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 26 | Backend Development |
| Jakarta Servlets | Request Handling and Business Logic |
| JSP | Dynamic Web Pages |
| HTML5 | Frontend Structure |
| CSS3 | Styling and Responsive UI |
| JavaScript | Client-side Functionality |
| JDBC | Database Connectivity |
| MySQL | Database Management |
| Maven | Build and Dependency Management |
| Apache Tomcat 11 | Application Server |
| Git | Version Control |
| GitHub | Source Code Hosting |

---

## 🗄️ Database

The application uses **MySQL** as its relational database.

### Main Tables

- `users`
- `students`
- `companies`
- `jobs`
- `applications`
- `interviews`

The application communicates with MySQL using **JDBC** and prepared SQL statements.

---

## 🔐 Security & Access Control

- Session-based authentication
- Role-based authorization
- Student-only resources
- Recruiter-only resources
- Admin-only resources
- Recruiter ownership validation
- Prepared SQL statements
- Duplicate application prevention
- Application ownership verification
- Job ownership verification
- Interview ownership verification
- Application status workflow validation
- Interview scheduling validation
- Unauthorized-access protection
- Session invalidation during logout

---

## 📊 Applicant Evaluation

Recruiters can evaluate applicants using:

### Eligibility

- Minimum CGPA
- Eligible branch

### Skill Matching

```text
Student Skills
      │
      ▼
Required Job Skills
      │
      ▼
Skill Match Score
```

---

## 📅 Interview Management

The interview module supports:

- Schedule interview
- Online interviews
- Offline interviews
- Meeting links
- Venue information
- Interview date and time
- Reschedule interview
- Complete interview
- Cancel interview
- Multiple interview records for an application
- Student interview schedule
- Recruiter interview management

Interview scheduling validates date, time, mode, meeting link/venue, recruiter ownership, and existing interview status.

---

## 🎨 User Interface

The application includes a consistent responsive UI across the major modules.

- Modern dashboard layouts
- Navigation bars
- Dashboard statistics cards
- Responsive tables
- Applicant cards
- Job cards
- Status badges
- Forms
- Alerts and notifications
- Responsive layouts
- Consistent buttons and controls
- Student, Recruiter and Admin interfaces

---

## 📂 Project Structure

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
│           ├── css/
│           ├── admin-dashboard.jsp
│           ├── admin-students.jsp
│           ├── admin-recruiters.jsp
│           ├── admin-companies.jsp
│           ├── admin-jobs.jsp
│           ├── admin-applications.jsp
│           ├── admin-interviews.jsp
│           ├── applicants.jsp
│           ├── company-profile.jsp
│           ├── create-job.jsp
│           ├── dashboard.html
│           ├── edit-job.jsp
│           ├── interview-schedule.jsp
│           ├── job-details.jsp
│           ├── jobs.jsp
│           ├── login.html
│           ├── my-applications.jsp
│           ├── profile.jsp
│           ├── recruiter-dashboard.jsp
│           ├── recruiter-interviews.jsp
│           ├── recruiter-jobs.jsp
│           ├── reschedule-interview.jsp
│           └── schedule-interview.jsp
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## ⚙️ Local Setup

### Requirements

- JDK 26 or compatible Java version
- Apache Maven
- MySQL Server
- Apache Tomcat 11
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/adityamaurya2209/smart-placement-system.git
cd smart-placement-system
```

### 2. Create the Database

```sql
CREATE DATABASE smart_placement;
```

Create the required tables according to the project's database schema.

### 3. Configure Database Connection

Configure the application's MySQL connection using your local database credentials.

```text
Database: smart_placement
Host: localhost
Port: 3306
Username: root
Password: YOUR_MYSQL_PASSWORD
```

> Never commit passwords or other sensitive database credentials to GitHub.

### 4. Build the Project

```bash
mvn clean package
```

The generated WAR file will be:

```text
target/smart-placement-system.war
```

### 5. Deploy to Apache Tomcat

Copy the WAR file to the Apache Tomcat `webapps` directory.

### 6. Start Apache Tomcat

Start Apache Tomcat 11 and wait for the application to deploy.

### 7. Open the Application

```text
http://localhost:8080/smart-placement-system/
```

Login page:

```text
http://localhost:8080/smart-placement-system/login.html
```

---

## 🧪 Testing

### Student

- [x] Login
- [x] Role protection
- [x] Dashboard
- [x] Profile
- [x] Browse jobs
- [x] Job details
- [x] Apply for job
- [x] Duplicate application prevention
- [x] My applications
- [x] Interview schedule
- [x] Online meeting link
- [x] Logout

### Recruiter

- [x] Login
- [x] Dashboard
- [x] Company information
- [x] Create job
- [x] Edit job
- [x] Close job
- [x] Reopen job
- [x] View applicants
- [x] Search/filter applicants
- [x] Eligibility checking
- [x] Skill matching
- [x] Application status updates
- [x] Applicant details
- [x] Schedule interview
- [x] Reschedule interview
- [x] Complete interview
- [x] Cancel interview
- [x] Logout

### Administrator

- [x] Login
- [x] Dashboard
- [x] Student management
- [x] Recruiter management
- [x] Company management
- [x] Job management
- [x] Application management
- [x] Interview management
- [x] Access-control testing
- [x] Logout

---

## 📸 Screenshots

Recommended screenshots for the repository:

1. Login Page
2. Student Dashboard
3. Student Jobs Page
4. Job Details
5. My Applications
6. Interview Schedule
7. Recruiter Dashboard
8. Recruiter Jobs
9. Applicant Management
10. Interview Management
11. Admin Dashboard
12. Admin Management Pages

---

## 🚀 Deployment

The project currently runs locally using:

```text
Java
   +
Maven
   +
Apache Tomcat
   +
MySQL
```

Cloud deployment is planned so that the application can be accessed through a public URL.

---

## 📌 Future Improvements

- Cloud deployment
- Email notifications
- Advanced placement analytics
- Automated testing
- Resume management
- Production security improvements
- Advanced applicant filtering
- REST API integration
- Spring Boot migration
- React frontend
- Docker deployment

---

## 📈 Project Status

### Completed

- [x] Maven project setup
- [x] MySQL database integration
- [x] Student authentication
- [x] Recruiter authentication
- [x] Admin authentication
- [x] Student profile
- [x] Job browsing
- [x] Job details
- [x] Job applications
- [x] Duplicate application prevention
- [x] Application tracking
- [x] Recruiter dashboard
- [x] Company management
- [x] Job creation
- [x] Job editing
- [x] Job closing/reopening
- [x] Applicant management
- [x] Applicant search/filtering
- [x] Eligibility checking
- [x] Skill-match scoring
- [x] Application status workflow
- [x] Interview scheduling
- [x] Interview rescheduling
- [x] Interview completion/cancellation
- [x] Student interview schedule
- [x] Admin dashboard
- [x] Admin management modules
- [x] Role-based access control
- [x] Responsive UI
- [x] Git & GitHub integration

### Planned

- [ ] Cloud deployment
- [ ] Email notifications
- [ ] Advanced analytics
- [ ] Automated testing
- [ ] Resume management
- [ ] Production security improvements
- [ ] Spring Boot migration
- [ ] React frontend

---

## 📝 Version Control

The project is maintained using Git and GitHub.

Example commit types:

```text
feat: add student application workflow
feat: add recruiter interview management
feat: add admin management functionality
feat: improve application workflow
fix: resolve interview scheduling issue
docs: update project README
```

---

## 👨‍💻 Author

**Aditya Maurya**

MCA — Master of Computer Applications

GitHub:  
https://github.com/adityamaurya2209

---

## 📄 License

This project was developed as an academic and portfolio project.
