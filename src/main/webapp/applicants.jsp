<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <title>Applicants</title>
</head>

<body>

<h1>Job Applicants</h1>

<hr>

<c:choose>

    <c:when test="${empty applications}">

        <p>No students have applied for this job yet.</p>

    </c:when>

    <c:otherwise>

        <c:forEach var="application" items="${applications}">

            <h2>${application.studentName}</h2>

            <p>
                <strong>Email:</strong>
                ${application.studentEmail}
            </p>

            <p>
                <strong>Roll Number:</strong>
                ${application.rollNumber}
            </p>

            <p>
                <strong>Branch:</strong>
                ${application.branch}
            </p>

            <p>
                <strong>CGPA:</strong>
                ${application.cgpa}
            </p>

            <p>
                <strong>Phone:</strong>
                ${application.phone}
            </p>

            <p>
                <strong>Skills:</strong>
                ${application.skills}
            </p>

            <p>
                <strong>Certifications:</strong>
                ${application.certifications}
            </p>

            <p>
                <strong>Applied On:</strong>
                ${application.applicationDate}
            </p>

            <p>
                <strong>Status:</strong>
                ${application.status}
            </p>

            <c:if test="${application.status == 'APPLIED'}">

            <form action="update-application-status" method="post">

                <input type="hidden"
                    name="applicationId"
                    value="${application.id}">

                <input type="hidden"
                    name="jobId"
                    value="${jobId}">

                <input type="hidden"
                    name="status"
                    value="SHORTLISTED">

                <button type="submit">
                    Shortlist
                </button>

            </form>

            <form action="update-application-status" method="post">

                <input type="hidden"
                    name="applicationId"
                    value="${application.id}">

                <input type="hidden"
                    name="jobId"
                    value="${jobId}">

                <input type="hidden"
                    name="status"
                    value="REJECTED">

                <button type="submit">
                    Reject
                </button>

            </form>

        </c:if>

        <c:if test="${application.status == 'SHORTLISTED'}">

            <h3>Schedule Interview</h3>

            <form action="schedule-interview" method="post">

                <input type="hidden"
                    name="applicationId"
                    value="${application.id}">

                <input type="hidden"
                    name="jobId"
                    value="${jobId}">

                <p>
                    <label>Interview Date:</label>
                    <input type="date"
                        name="interviewDate"
                        required>
                </p>

                <p>
                    <label>Interview Time:</label>
                    <input type="time"
                        name="interviewTime"
                        required>
                </p>

                <p>
                    <label>Mode:</label>

                    <select name="mode" required>

                        <option value="">
                            Select Mode
                        </option>

                        <option value="ONLINE">
                            Online
                        </option>

                        <option value="OFFLINE">
                            Offline
                        </option>

                    </select>

                </p>

                <p>
                    <label>Meeting Link:</label>

                    <input type="text"
                        name="meetingLink"
                        placeholder="https://meet.google.com/...">
                </p>

                <p>
                    <label>Venue:</label>

                    <input type="text"
                        name="venue"
                        placeholder="Office / Interview Location">
                </p>

                <button type="submit">
                    Schedule Interview
                </button>

            </form>

        </c:if>

            <p>
                <strong>Match Score:</strong>
                ${application.matchScore}
            </p>

            <hr>

        </c:forEach>

    </c:otherwise>

</c:choose>

<a href="recruiter-dashboard">Back to Recruiter Dashboard</a>

</body>

</html>