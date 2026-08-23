<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>
        Company Details
    </title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            color: #1f2937;
        }

        .header {
            background: #1f2937;
            color: white;
            padding: 25px 35px;
        }

        .header h1 {
            margin: 0;
            font-size: 32px;
        }

        .container {
            padding: 35px;
        }

        .card {
            background: white;
            max-width: 900px;
            padding: 35px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .row {
            padding: 20px 0;
            border-bottom: 1px solid #e5e7eb;
        }

        .label {
            font-weight: bold;
            color: #374151;
            font-size: 18px;
        }

        .value {
            margin-top: 8px;
            font-size: 20px;
            color: #111827;
        }

        .description {
            line-height: 1.6;
        }

        .website {
            color: #2563eb;
            text-decoration: none;
        }

        .website:hover {
            text-decoration: underline;
        }

        .button {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 20px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 6px;
        }

        .button:hover {
            background: #1d4ed8;
        }

    </style>

</head>

<body>

<div class="header">

    <h1>Company Details</h1>

</div>

<div class="container">

    <div class="card">

        <div class="row">

            <div class="label">
                Company ID
            </div>

            <div class="value">
                <%= request.getAttribute(
                        "companyId") %>
            </div>

        </div>

        <div class="row">

            <div class="label">
                Company Name
            </div>

            <div class="value">
                <%= request.getAttribute(
                        "companyName") %>
            </div>

        </div>

        <div class="row">

            <div class="label">
                Description
            </div>

            <div class="value description">
                <%= request.getAttribute(
                        "companyDescription") %>
            </div>

        </div>

        <div class="row">

            <div class="label">
                Website
            </div>

            <div class="value">

                <a
                    class="website"
                    href="<%= request.getAttribute(
                            "companyWebsite") %>"
                    target="_blank">

                    <%= request.getAttribute(
                            "companyWebsite") %>

                </a>

            </div>

        </div>

        <div class="row">

            <div class="label">
                Location
            </div>

            <div class="value">
                <%= request.getAttribute(
                        "companyLocation") %>
            </div>

        </div>

        <a href="admin-companies"
           class="button">

            ← Back to Companies

        </a>

    </div>

</div>

</body>

</html>