<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Applications | Smart Placement System</title>
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

    <div class="page-header">
        <div>
            <h1 class="page-title">My Applications</h1>
            <p class="page-subtitle">Track every placement application from one place.</p>
        </div>
    </div>

    <c:if test="${param.success == 'true'}">
        <div class="success">
            Job application submitted successfully.
        </div>
    </c:if>

    <c:choose>

        <c:when test="${empty applications}">
            <div class="empty-state">
                <h2>No Applications Yet</h2>
                <p>You have not applied for any jobs yet.</p>
                <a class="button" href="jobs">Browse Available Jobs</a>
            </div>
        </c:when>

        <c:otherwise>

            <c:forEach var="application" items="${applications}">

                <article class="application">

                    <h2>${application.jobTitle}</h2>
                    <div class="company">${application.companyName}</div>

                    <div class="job-meta">

                        <div class="job-meta-item">
                            <strong>Application Date</strong><br>
                            ${application.applicationDate}
                        </div>

                        <div class="job-meta-item">
                            <strong>Status</strong><br>
                            <span class="status ${application.status}">
                                ${application.status}
                            </span>
                        </div>

                    </div>

                    <c:if test="${not empty application.matchScore}">
                        <div class="match-score">
                            <strong>Skill Match Score:</strong>
                            ${application.matchScore}%
                        </div>
                    </c:if>

                </article>

            </c:forEach>

        </c:otherwise>

    </c:choose>

    <a class="back" href="dashboard.html">&larr; Back to Dashboard</a>

</main>

</body>
</html>
