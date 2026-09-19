<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Profile | Smart Placement System</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">
</head>

<body class="sp-inner-page">

<header class="topbar">
    <div class="topbar-inner">
        <a class="brand" href="dashboard.html">
            <span class="brand-mark">SP</span>
            <span>Smart Placement</span>
        </a>

        <nav class="nav-links">
            <a href="student-profile">Profile</a>
            <a href="jobs">Jobs</a>
            <a href="my-applications">Applications</a>
            <a href="interview-schedule">Interviews</a>
            <a class="nav-logout" href="student-logout">Logout</a>
        </nav>
    </div>
</header>

<main class="page-shell">

    <section class="profile">

        <div class="page-header">
            <div>
                <h1 class="page-title">Student Profile</h1>
                <p class="page-subtitle">Your academic and placement information.</p>
            </div>
        </div>

        <div class="field">
            <strong>Name</strong><br>
            ${name}
        </div>

        <div class="field">
            <strong>Email</strong><br>
            ${email}
        </div>

        <div class="field">
            <strong>Roll Number</strong><br>
            ${rollNumber}
        </div>

        <div class="field">
            <strong>Branch</strong><br>
            ${branch}
        </div>

        <div class="field">
            <strong>CGPA</strong><br>
            ${cgpa}
        </div>

        <div class="field">
            <strong>Phone</strong><br>
            ${phone}
        </div>

        <div class="field">
            <strong>Skills</strong><br>
            ${skills}
        </div>

        <div class="field">
            <strong>Certifications</strong><br>
            ${certifications}
        </div>

        <div class="field">
            <strong>Resume</strong><br>
            <c:choose>
                <c:when test="${not empty resumePath}">
                    ${resumePath}
                </c:when>
                <c:otherwise>
                    Not uploaded
                </c:otherwise>
            </c:choose>
        </div>

        <a class="back" href="dashboard.html">&larr; Back to Dashboard</a>

    </section>

</main>

</body>
</html>
