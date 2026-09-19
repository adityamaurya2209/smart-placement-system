<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Edit Job</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f5f6fa;
        }

        .container {
            max-width: 800px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.1);
        }

        h1 {
            margin-bottom: 5px;
        }

        .company {
            color: #666;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin-top: 16px;
            font-weight: bold;
        }

        input,
        textarea {
            width: 100%;
            box-sizing: border-box;
            padding: 11px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 15px;
        }

        textarea {
            min-height: 120px;
            resize: vertical;
        }

        button {
            margin-top: 25px;
            padding: 12px 22px;
            border: none;
            border-radius: 6px;
            background: #007bff;
            color: white;
            font-size: 15px;
            cursor: pointer;
        }

        button:hover {
            opacity: 0.9;
        }

        .back {
            display: inline-block;
            margin-top: 20px;
            color: #333;
            text-decoration: none;
        }

        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 15px;
        }

        .status {
            margin-top: 15px;
            font-weight: bold;
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<div class="container">

    <h1>Edit Job</h1>

    <div class="company">
        ${job.companyName}
    </div>

    <c:if test="${param.error == 'missing'}">
        <div class="error">
            Please fill in all required fields.
        </div>
    </c:if>

    <c:if test="${param.error == 'cgpa'}">
        <div class="error">
            Minimum CGPA must be between 0 and 10.
        </div>
    </c:if>

    <c:if test="${param.error == 'deadline'}">
        <div class="error">
            Application deadline must be today or a future date.
        </div>
    </c:if>

    <form action="edit-job" method="post">

        <input type="hidden"
               name="id"
               value="${job.id}">

        <label>Job Title *</label>

        <input type="text"
               name="title"
               value="${job.title}"
               required>

        <label>Description</label>

        <textarea name="description">${job.description}</textarea>

        <label>Location</label>

        <input type="text"
               name="location"
               value="${job.location}">

        <label>Minimum CGPA *</label>

        <input type="number"
               name="minimumCgpa"
               value="${job.minimumCgpa}"
               step="0.01"
               min="0"
               max="10"
               required>

        <label>Eligible Branch *</label>

        <input type="text"
               name="eligibleBranch"
               value="${job.eligibleBranch}"
               required>

        <label>Required Skills</label>

        <input type="text"
               name="requiredSkills"
               value="${job.requiredSkills}">

        <label>Salary</label>

        <input type="text"
               name="salary"
               value="${job.salary}">

        <label>Application Deadline</label>

        <input type="date"
               name="applicationDeadline"
               value="${job.applicationDeadline}">

        <div class="status">
            Current Status: ${job.status}
        </div>

        <button type="submit">
            Save Changes
        </button>

    </form>

    <a class="back" href="recruiter-jobs">
        &larr; Back to Jobs
    </a>

</div>

</body>

</html>
