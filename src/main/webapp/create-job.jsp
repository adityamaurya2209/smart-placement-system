<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Create Job</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f5f6fa;
        }

        .container {
            background: white;
            max-width: 800px;
            margin: auto;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }

        input,
        textarea {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            box-sizing: border-box;
        }

        textarea {
            min-height: 100px;
        }

        button {
            margin-top: 20px;
            padding: 12px 20px;
            background: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 5px;
        }

        .back {
            display: inline-block;
            margin-top: 20px;
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<div class="container">

    <h1>Create Job</h1>

    <hr>

    <% if ("missing".equals(request.getParameter("error"))) { %>

        <div class="error">
            Please fill in all required fields.
        </div>

    <% } %>

    <% if ("cgpa".equals(request.getParameter("error"))) { %>

        <div class="error">
            Minimum CGPA must be between 0 and 10.
        </div>

    <% } %>

    <form action="create-job" method="post">

        <label>Job Title *</label>

        <input type="text"
               name="title"
               required>

        <label>Description</label>

        <textarea name="description"></textarea>

        <label>Location</label>

        <input type="text"
               name="location">

        <label>Minimum CGPA *</label>

        <input type="number"
               name="minimumCgpa"
               step="0.01"
               min="0"
               max="10"
               required>

        <label>Eligible Branch *</label>

        <input type="text"
               name="eligibleBranch"
               placeholder="CSE, IT"
               required>

        <label>Required Skills</label>

        <input type="text"
               name="requiredSkills"
               placeholder="Java, SQL, HTML">

        <label>Salary</label>

        <input type="text"
               name="salary"
               placeholder="7 LPA">

        <label>Application Deadline</label>

        <input type="date"
               name="applicationDeadline">

        <button type="submit">
            Create Job
        </button>

    </form>

    <a class="back"
       href="recruiter-dashboard">
        ← Back to Dashboard
    </a>

</div>

</body>

</html>
