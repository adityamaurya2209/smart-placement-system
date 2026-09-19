<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Company Profile</title>

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

        .field {
            margin-bottom: 20px;
        }

        .label {
            font-weight: bold;
        }

        .button {
            display: inline-block;
            padding: 10px 18px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<div class="container">

    <h1>Company Profile</h1>

    <hr>

    <div class="field">
        <div class="label">Company Name</div>
        <p>${companyName}</p>
    </div>

    <div class="field">
        <div class="label">Description</div>
        <p>${description}</p>
    </div>

    <div class="field">
        <div class="label">Website</div>
        <p>${website}</p>
    </div>

    <div class="field">
        <div class="label">Location</div>
        <p>${location}</p>
    </div>

    <hr>

    <a class="button" href="recruiter-dashboard">
        Back to Dashboard
    </a>

</div>

</body>

</html>