<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/inner-ui.css">

    <meta charset="UTF-8">

    <title>Schedule Interview</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 40px;
            background: #f5f6fa;
        }

        .container {
            background: white;
            max-width: 700px;
            margin: auto;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        h1 {
            margin-top: 0;
        }

        label {
            display: block;
            margin-top: 18px;
            font-weight: bold;
        }

        input,
        select {
            width: 100%;
            padding: 11px;
            margin-top: 6px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 15px;
        }

        input:focus,
        select:focus {
            outline: none;
            border-color: #007bff;
        }

        .hint {
            font-size: 13px;
            color: #666;
            margin-top: 5px;
        }

        button {
            margin-top: 25px;
            padding: 12px 22px;
            background: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 15px;
        }

        button:hover {
            background: #218838;
        }

        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 12px;
        }

        .back {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #007bff;
        }

        .hidden {
            display: none;
        }

    </style>

</head>

<body class="sp-inner-page">
<nav class="sp-inner-nav"><div class="nav-inner"><a class="sp-brand" href="recruiter-dashboard"><span class="sp-brand-mark">SP</span>Smart Placement</a><div class="sp-nav-links"><a href="recruiter-dashboard">Dashboard</a><a href="applicants">Applicants</a><a href="recruiter-jobs">Jobs</a><a href="company-profile">Company</a><a href="recruiter-interviews">Interviews</a><a class="logout" href="recruiter-logout">Logout</a></div></div></nav>


<div class="container">

    <h1>Schedule Interview</h1>

    <hr>

    <% if ("missing".equals(request.getParameter("error"))) { %>
        <div class="error">
            Please fill in all required fields.
        </div>
    <% } %>

    <% if ("invalid-mode".equals(request.getParameter("error"))) { %>
        <div class="error">
            Please select a valid interview mode.
        </div>
    <% } %>

    <% if ("already-scheduled".equals(request.getParameter("error"))) { %>
        <div class="error">
            An interview is already scheduled for this application.
            Use the Reschedule option from the Interviews page.
        </div>
    <% } %>

    <% if ("completed".equals(request.getParameter("error"))) { %>
        <div class="error">
            This application already has a completed interview.
        </div>
    <% } %>

    <% if ("past-date".equals(request.getParameter("error"))) { %>
        <div class="error">
            Interview date cannot be in the past.
        </div>
    <% } %>

    <% if ("past-time".equals(request.getParameter("error"))) { %>
        <div class="error">
            The selected interview time has already passed.
        </div>
    <% } %>

    <% if ("outside-hours".equals(request.getParameter("error"))) { %>
        <div class="error">
            Interviews can only be scheduled between 09:00 AM and 06:00 PM.
        </div>
    <% } %>

    <% if ("online-link".equals(request.getParameter("error"))) { %>
        <div class="error">
            A meeting link is required for an online interview.
        </div>
    <% } %>

    <% if ("offline-venue".equals(request.getParameter("error"))) { %>
        <div class="error">
            A venue is required for an offline interview.
        </div>
    <% } %>

    <% if ("unauthorized".equals(request.getParameter("error"))) { %>
        <div class="error">
            You are not authorized to schedule this interview.
        </div>
    <% } %>

    <% if ("database".equals(request.getParameter("error"))) { %>
        <div class="error">
            Something went wrong while scheduling the interview.
            Please try again.
        </div>
    <% } %>

    <form action="schedule-interview" method="post">

        <input type="hidden"
               name="applicationId"
               value="${applicationId}">

        <label>
            Interview Date *
        </label>

        <input type="date"
               id="interviewDate"
               name="interviewDate"
               required>

        <label>
            Interview Time *
        </label>

        <input type="time"
               id="interviewTime"
               name="interviewTime"
               min="09:00"
               max="18:00"
               required>

        <div class="hint">
            Interview hours: 09:00 AM – 06:00 PM
        </div>

        <label>
            Interview Mode *
        </label>

        <select id="mode"
                name="mode"
                required>

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

        <div id="onlineSection">

            <label>
                Meeting Link
            </label>

            <input type="url"
                   id="meetingLink"
                   name="meetingLink"
                   placeholder="https://meet.google.com/...">

        </div>

        <div id="offlineSection">

            <label>
                Venue
            </label>

            <input type="text"
                   id="venue"
                   name="venue"
                   placeholder="Office / Interview Room">

        </div>

        <button type="submit">
            Schedule Interview
        </button>

    </form>

    <a class="back" href="applicants">
        &larr; Back to Applicants
    </a>

</div>

<script>

    const mode = document.getElementById("mode");
    const onlineSection = document.getElementById("onlineSection");
    const offlineSection = document.getElementById("offlineSection");

    const meetingLink = document.getElementById("meetingLink");
    const venue = document.getElementById("venue");

    function updateModeFields() {

        if (mode.value === "ONLINE") {

            onlineSection.style.display = "block";
            offlineSection.style.display = "none";

            meetingLink.required = true;
            venue.required = false;

            venue.value = "";

        } else if (mode.value === "OFFLINE") {

            onlineSection.style.display = "none";
            offlineSection.style.display = "block";

            meetingLink.required = false;
            venue.required = true;

            meetingLink.value = "";

        } else {

            onlineSection.style.display = "block";
            offlineSection.style.display = "block";

            meetingLink.required = false;
            venue.required = false;
        }
    }

    mode.addEventListener("change", updateModeFields);

    updateModeFields();

    /*
     * Prevent selecting a date before today.
     */
    const dateInput =
        document.getElementById("interviewDate");

    const today =
        new Date().toISOString().split("T")[0];

    dateInput.min = today;

</script>

</body>

</html>