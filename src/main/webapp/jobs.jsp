<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Available Jobs</title>
</head>

<body>

<h1>Available Jobs</h1>

<hr>

<c:choose>

    <c:when test="${empty jobs}">
        <p>No jobs are currently available.</p>
    </c:when>

    <c:otherwise>

        <c:forEach var="job" items="${jobs}">

            <h2>${job.title}</h2>

            <p>
                <strong>Company:</strong>
                ${job.companyName}
            </p>

            <p>
                <strong>Description:</strong>
                ${job.description}
            </p>

            <p>
                <strong>Location:</strong>
                ${job.location}
            </p>

            <p>
                <strong>Minimum CGPA:</strong>
                ${job.minimumCgpa}
            </p>

            <p>
                <strong>Eligible Branch:</strong>
                ${job.eligibleBranch}
            </p>

            <p>
                <strong>Required Skills:</strong>
                ${job.requiredSkills}
            </p>

            <p>
                <strong>Salary:</strong>
                ${job.salary}
            </p>

            <p>
                <strong>Application Deadline:</strong>
                ${job.applicationDeadline}
            </p>

            <p>
                <strong>Status:</strong>
                ${job.status}
            </p>

            <form action="apply-job" method="post">

            <input type="hidden"
                name="jobId"
                value="${job.id}">

            <button type="submit">
                Apply Now
            </button>

            </form>

            <hr>

        </c:forEach>

    </c:otherwise>

</c:choose>

<a href="dashboard.html">Back to Dashboard</a>

</body>
</html>