<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <title>Interview Schedule</title>
</head>

<body>

<h1>Interview Schedule</h1>

<hr>

<c:choose>

    <c:when test="${empty interviews}">

        <p>You currently have no scheduled interviews.</p>

    </c:when>

    <c:otherwise>

        <c:forEach var="interview" items="${interviews}">

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

            <c:if test="${not empty interview.meetingLink}">

                <p>
                    <strong>Meeting Link:</strong>

                    <a href="${interview.meetingLink}"
                       target="_blank">
                        Join Interview
                    </a>
                </p>

            </c:if>

            <c:if test="${not empty interview.venue}">

                <p>
                    <strong>Venue:</strong>
                    ${interview.venue}
                </p>

            </c:if>

            <p>
                <strong>Status:</strong>
                ${interview.status}
            </p>

            <hr>

        </c:forEach>

    </c:otherwise>

</c:choose>

<a href="dashboard.html">Back to Dashboard</a>

</body>

</html>