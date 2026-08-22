<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <title>My Applications</title>
</head>

<body>

<h1>My Applications</h1>

<hr>

<c:choose>

    <c:when test="${empty applications}">

        <p>You have not applied for any jobs yet.</p>

    </c:when>

    <c:otherwise>

        <c:forEach var="application" items="${applications}">

            <h2>${application.jobTitle}</h2>

            <p>
                <strong>Company:</strong>
                ${application.companyName}
            </p>

            <p>
                <strong>Applied On:</strong>
                ${application.applicationDate}
            </p>

            <p>
                <strong>Status:</strong>
                ${application.status}
            </p>

            <p>
                <strong>Match Score:</strong>
                ${application.matchScore}
            </p>

            <hr>

        </c:forEach>

    </c:otherwise>

</c:choose>

<a href="dashboard.html">Back to Dashboard</a>

</body>

</html>