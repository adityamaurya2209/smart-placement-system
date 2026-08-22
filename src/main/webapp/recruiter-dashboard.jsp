<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <title>Recruiter Dashboard</title>
</head>

<body>

<h1>Smart Placement System</h1>

<h2>Recruiter Dashboard</h2>

<hr>

<c:choose>

    <c:when test="${empty jobs}">

        <p>No jobs have been posted by your company.</p>

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

            <p>
                <strong>Applicants:</strong>
                ${job.applicantCount}
            </p>
            
            <c:if test="${job.applicantCount > 0}">

            <p>
                <a href="applicants?jobId=${job.id}">
                    View Applicants
                </a>
            </p>

        </c:if>

            <hr>

        </c:forEach>

    </c:otherwise>

</c:choose>

<a href="login.html">Logout</a>

</body>

</html>