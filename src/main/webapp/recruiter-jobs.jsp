<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Manage Jobs</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background: #f5f6fa;
            color: #222;
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        h1 {
            margin: 0;
        }

        .create {
            background: #28a745;
            color: white;
            text-decoration: none;
            padding: 11px 18px;
            border-radius: 7px;
        }

        .job {
            background: white;
            padding: 25px;
            margin-bottom: 20px;
            border-radius: 12px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.08);
        }

        .job-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 20px;
        }

        .job h2 {
            margin-top: 0;
            margin-bottom: 8px;
        }

        .company {
            color: #666;
        }

        .details {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px 30px;
            margin: 20px 0;
        }

        .detail {
            padding: 8px 0;
        }

        .status {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
        }

        .open {
            background: #d4edda;
            color: #155724;
        }

        .closed {
            background: #f8d7da;
            color: #721c24;
        }

        .actions {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            margin-top: 20px;
            padding-top: 18px;
            border-top: 1px solid #eee;
        }

        .btn {
            border: none;
            padding: 9px 14px;
            border-radius: 6px;
            text-decoration: none;
            cursor: pointer;
            font-size: 14px;
        }

        .edit {
            background: #007bff;
            color: white;
        }

        .close {
            background: #dc3545;
            color: white;
        }

        .reopen {
            background: #28a745;
            color: white;
        }

        .applicants {
            background: #6f42c1;
            color: white;
        }

        .success {
            background: #d4edda;
            color: #155724;
            padding: 12px;
            border-radius: 7px;
            margin-bottom: 20px;
        }

        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 7px;
            margin-bottom: 20px;
        }

        .empty {
            background: white;
            padding: 30px;
            border-radius: 12px;
            text-align: center;
        }

        .back {
            display: inline-block;
            margin-top: 15px;
            color: #333;
            text-decoration: none;
        }

        form {
            display: inline;
        }

        @media (max-width: 700px) {

            .header,
            .job-header {
                flex-direction: column;
                align-items: stretch;
            }

            .details {
                grid-template-columns: 1fr;
            }

        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<div class="container">

    <div class="header">

        <h1>Manage Jobs</h1>

        <a class="create" href="create-job">
            + Create New Job
        </a>

    </div>

    <c:if test="${param.success == 'true'}">
        <div class="success">
            Job created successfully.
        </div>
    </c:if>

    <c:if test="${param.updated == 'true'}">
        <div class="success">
            Job updated successfully.
        </div>
    </c:if>

    <c:if test="${param.closed == 'true'}">
        <div class="success">
            Job closed successfully.
        </div>
    </c:if>

    <c:if test="${param.reopened == 'true'}">
        <div class="success">
            Job reopened successfully.
        </div>
    </c:if>

    <c:if test="${param.error == 'not-found'}">
        <div class="error">
            Job not found or you are not authorized to manage it.
        </div>
    </c:if>

    <c:if test="${param.error == 'invalid-id'}">
        <div class="error">
            Invalid job ID.
        </div>
    </c:if>

    <c:if test="${param.error == 'database'}">
        <div class="error">
            Database error occurred.
        </div>
    </c:if>

    <c:choose>

        <c:when test="${empty jobs}">

            <div class="empty">
                <h2>No Jobs Posted</h2>
                <p>You have not posted any jobs yet.</p>
            </div>

        </c:when>

        <c:otherwise>

            <c:forEach var="job" items="${jobs}">

                <div class="job">

                    <div class="job-header">

                        <div>

                            <h2>${job.title}</h2>

                            <div class="company">
                                ${job.companyName}
                            </div>

                        </div>

                        <c:choose>

                            <c:when test="${job.status == 'OPEN'}">

                                <span class="status open">
                                    OPEN
                                </span>

                            </c:when>

                            <c:otherwise>

                                <span class="status closed">
                                    CLOSED
                                </span>

                            </c:otherwise>

                        </c:choose>

                    </div>

                    <div class="details">

                        <div class="detail">
                            <strong>Location:</strong>
                            ${job.location}
                        </div>

                        <div class="detail">
                            <strong>Minimum CGPA:</strong>
                            ${job.minimumCgpa}
                        </div>

                        <div class="detail">
                            <strong>Eligible Branch:</strong>
                            ${job.eligibleBranch}
                        </div>

                        <div class="detail">
                            <strong>Required Skills:</strong>
                            ${job.requiredSkills}
                        </div>

                        <div class="detail">
                            <strong>Salary:</strong>
                            ${job.salary}
                        </div>

                        <div class="detail">
                            <strong>Deadline:</strong>
                            ${job.applicationDeadline}
                        </div>

                        <div class="detail">
                            <strong>Applicants:</strong>
                            ${job.applicantCount}
                        </div>

                    </div>

                    <c:if test="${not empty job.description}">

                        <p>
                            <strong>Description:</strong><br>
                            ${job.description}
                        </p>

                    </c:if>

                    <div class="actions">

                        <a class="btn edit"
                           href="edit-job?id=${job.id}">
                            Edit Job
                        </a>

                        <a class="btn applicants"
                           href="applicants">
                            View Applicants
                        </a>

                        <c:choose>

                            <c:when test="${job.status == 'OPEN'}">

                                <form action="job-status"
                                      method="post"
                                      onsubmit="return confirm('Close this job?');">

                                    <input type="hidden"
                                           name="id"
                                           value="${job.id}">

                                    <input type="hidden"
                                           name="status"
                                           value="CLOSED">

                                    <button class="btn close"
                                            type="submit">
                                        Close Job
                                    </button>

                                </form>

                            </c:when>

                            <c:otherwise>

                                <form action="job-status"
                                      method="post"
                                      onsubmit="return confirm('Reopen this job?');">

                                    <input type="hidden"
                                           name="id"
                                           value="${job.id}">

                                    <input type="hidden"
                                           name="status"
                                           value="OPEN">

                                    <button class="btn reopen"
                                            type="submit">
                                        Reopen Job
                                    </button>

                                </form>

                            </c:otherwise>

                        </c:choose>

                    </div>

                </div>

            </c:forEach>

        </c:otherwise>

    </c:choose>

    <a class="back" href="recruiter-dashboard">
        &larr; Back to Dashboard
    </a>

</div>

</body>

</html>
