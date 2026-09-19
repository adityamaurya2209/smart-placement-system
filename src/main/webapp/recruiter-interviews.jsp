<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Recruiter Interviews</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 40px;
            background: #f5f6fa;
        }

        h1 {
            margin-bottom: 10px;
        }

        .interview {
            background: white;
            padding: 25px;
            margin-bottom: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .success {
            background: #d4edda;
            color: #155724;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .warning {
            background: #fff3cd;
            color: #856404;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .meeting {
            display: inline-block;
            padding: 9px 15px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-right: 8px;
        }

        .btn {
            display: inline-block;
            padding: 9px 15px;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
            text-decoration: none;
            margin-right: 8px;
            margin-top: 8px;
        }

        .reschedule {
            background: #007bff;
        }

        .complete {
            background: #28a745;
        }

        .cancel {
            background: #dc3545;
        }

        .status {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 13px;
        }

        .scheduled {
            background: #cfe2ff;
            color: #084298;
        }

        .completed {
            background: #d1e7dd;
            color: #0f5132;
        }

        .cancelled {
            background: #f8d7da;
            color: #842029;
        }

        .back {
            display: inline-block;
            margin-top: 15px;
            text-decoration: none;
            color: #007bff;
        }

        form {
            display: inline;
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<h1>Interview Schedule</h1>

<hr>

<c:if test="${param.success == 'true'}">
    <div class="success">
        Interview scheduled successfully.
    </div>
</c:if>

<c:if test="${param.rescheduled == 'true'}">
    <div class="success">
        Interview rescheduled successfully.
        The previous interview has been preserved as cancelled.
    </div>
</c:if>

<c:if test="${param.completed == 'true'}">
    <div class="success">
        Interview marked as completed.
    </div>
</c:if>

<c:if test="${param.cancelled == 'true'}">
    <div class="success">
        Interview cancelled successfully.
    </div>
</c:if>

<c:if test="${param.error == 'already-closed'}">
    <div class="error">
        This interview has already been completed or cancelled.
    </div>
</c:if>

<c:if test="${param.error == 'unauthorized'}">
    <div class="error">
        You are not authorized to manage this interview.
    </div>
</c:if>

<c:if test="${param.error == 'not-scheduled'}">
    <div class="error">
        Only scheduled interviews can be rescheduled.
    </div>
</c:if>

<c:if test="${param.error == 'database'}">
    <div class="error">
        Something went wrong while processing the interview.
    </div>
</c:if>

<c:choose>

    <c:when test="${empty interviews}">

        <p>No interviews have been scheduled yet.</p>

    </c:when>

    <c:otherwise>

        <c:forEach var="interview"
                   items="${interviews}">

            <div class="interview">

                <h2>${interview.jobTitle}</h2>

                <p>
                    <strong>Company:</strong>
                    ${interview.companyName}
                </p>

                <p>
                    <strong>Date:</strong>
                    ${interview.interviewDate}
                </p>

                <p>
                    <strong>Time:</strong>
                    ${interview.interviewTime}
                </p>

                <p>
                    <strong>Mode:</strong>
                    ${interview.mode}
                </p>

                <c:if test="${interview.mode == 'ONLINE'
                              && not empty interview.meetingLink}">

                    <a class="meeting"
                       href="${interview.meetingLink}"
                       target="_blank">

                        Open Meeting Link

                    </a>

                </c:if>

                <c:if test="${interview.mode == 'OFFLINE'
                              && not empty interview.venue}">

                    <p>
                        <strong>Venue:</strong>
                        ${interview.venue}
                    </p>

                </c:if>

                <p>
                    <strong>Status:</strong>

                    <c:choose>

                        <c:when test="${interview.status == 'SCHEDULED'}">

                            <span class="status scheduled">
                                SCHEDULED
                            </span>

                        </c:when>

                        <c:when test="${interview.status == 'COMPLETED'}">

                            <span class="status completed">
                                COMPLETED
                            </span>

                        </c:when>

                        <c:when test="${interview.status == 'CANCELLED'}">

                            <span class="status cancelled">
                                CANCELLED
                            </span>

                        </c:when>

                        <c:otherwise>

                            <span class="status">
                                ${interview.status}
                            </span>

                        </c:otherwise>

                    </c:choose>

                </p>

                <c:if test="${interview.status == 'SCHEDULED'}">

                    <a class="btn reschedule"
                       href="reschedule-interview?interviewId=${interview.id}">

                        Reschedule

                    </a>

                    <form action="recruiter-interview-status"
                          method="post">

                        <input type="hidden"
                               name="interviewId"
                               value="${interview.id}">

                        <input type="hidden"
                               name="status"
                               value="COMPLETED">

                        <button class="btn complete"
                                type="submit"
                                onclick="return confirm('Mark this interview as completed?');">

                            Mark Completed

                        </button>

                    </form>

                    <form action="recruiter-interview-status"
                          method="post">

                        <input type="hidden"
                               name="interviewId"
                               value="${interview.id}">

                        <input type="hidden"
                               name="status"
                               value="CANCELLED">

                        <button class="btn cancel"
                                type="submit"
                                onclick="return confirm('Cancel this interview?');">

                            Cancel Interview

                        </button>

                    </form>

                </c:if>

            </div>

        </c:forEach>

    </c:otherwise>

</c:choose>

<br>

<a class="back" href="recruiter-dashboard">
    &larr; Back to Dashboard
</a>

</body>

</html>