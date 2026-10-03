<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="icon" href="favicon.ico" sizes="any">
    <link rel="icon" type="image/svg+xml" href="images/brand/smart-placement-icon.svg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${job.title} | Smart Placement System</title>
    <link rel="stylesheet" href="css/style.css?v=6">
    <link rel="stylesheet" href="css/inner-ui.css?v=6">
</head>

<body class="sp-inner-page">

<header class="topbar">
    <div class="topbar-inner">
        <a class="brand" href="dashboard.html">
            <img class="brand-mark brand-logo" src="images/brand/smart-placement-icon.svg" alt="">
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

    <article class="card content-card">

        <div class="company">${job.companyName}</div>
        <h1 class="page-title">${job.title}</h1>

        <div class="job-meta">

            <div class="job-meta-item">
                <strong>Location</strong><br>
                ${job.location}
            </div>

            <div class="job-meta-item">
                <strong>Minimum CGPA</strong><br>
                ${job.minimumCgpa}
            </div>

            <div class="job-meta-item">
                <strong>Eligible Branch</strong><br>
                ${job.eligibleBranch}
            </div>

            <div class="job-meta-item">
                <strong>Required Skills</strong><br>
                ${job.requiredSkills}
            </div>

            <div class="job-meta-item">
                <strong>Salary</strong><br>
                ${job.salary}
            </div>

            <div class="job-meta-item">
                <strong>Application Deadline</strong><br>
                ${job.applicationDeadline}
            </div>

            <div class="job-meta-item">
                <strong>Status</strong><br>
                <span class="status ${job.status}">${job.status}</span>
            </div>

        </div>

        <h2>Job Description</h2>
        <p>${job.description}</p>

        <hr>

        <form action="apply-job" method="post">
            <input type="hidden" name="jobId" value="${job.id}">
            <button class="apply" type="submit">Apply Now</button>
        </form>

    </article>

    <a class="back" href="jobs">&larr; Back to Jobs</a>

</main>

</body>
</html>
