<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recruiter Dashboard | Smart Placement System</title>
    <link rel="stylesheet" href="css/style.css?v=5">
</head>
<body class="dashboard-page recruiter-dashboard-page">
<header class="app-nav">
  <div class="app-nav-inner">
    <a class="app-brand" href="recruiter-dashboard"><span class="brand-mark">SP</span><span>Smart Placement</span></a>
    <nav class="app-nav-links">
      <a class="active" href="recruiter-dashboard">Dashboard</a>
      <a href="applicants">Applicants</a>
      <a href="recruiter-jobs">Jobs</a>
      <a href="company-profile">Company</a>
      <a href="recruiter-interviews">Interviews</a>
      <a class="logout-link" href="recruiter-logout">Logout</a>
    </nav>
  </div>
</header>
<main class="app-main">
  <section class="dashboard-hero recruiter-hero">
    <div>
      <span class="eyebrow">RECRUITER PORTAL</span>
      <h1>Welcome back, ${sessionScope.name}</h1>
      <p>Manage hiring, applicants, job postings and interviews for <strong>${companyName}</strong>.</p>
    </div>
    <div class="hero-badge">${companyName}</div>
  </section>
  <section class="stats-grid recruiter-stats">
    <a class="stat-card" href="recruiter-jobs"><span class="stat-icon">JB</span><span class="stat-label">Total Jobs</span><strong>${jobCount}</strong><span class="stat-link">Manage jobs →</span></a>
    <a class="stat-card" href="applicants"><span class="stat-icon">AP</span><span class="stat-label">Total Applicants</span><strong>${applicantCount}</strong><span class="stat-link">View applicants →</span></a>
    <a class="stat-card" href="recruiter-interviews"><span class="stat-icon">IV</span><span class="stat-label">Total Interviews</span><strong>${interviewCount}</strong><span class="stat-link">View interviews →</span></a>
  </section>
  <section class="section-heading"><div><span class="eyebrow dark">HIRING WORKSPACE</span><h2>Quick Actions</h2></div></section>
  <section class="quick-grid recruiter-quick-grid">
    <a class="quick-card featured" href="applicants"><span class="quick-icon">01</span><h3>Applicants</h3><p>Review candidates, eligibility, skill match and application status.</p><span class="card-action">View Applicants →</span></a>
    <a class="quick-card" href="recruiter-jobs"><span class="quick-icon">02</span><h3>Manage Jobs</h3><p>Create, edit, close and reopen your job postings.</p><span class="card-action">Manage Jobs →</span></a>
    <a class="quick-card" href="company-profile"><span class="quick-icon">03</span><h3>Company Profile</h3><p>Review your company information and profile details.</p><span class="card-action">View Profile →</span></a>
    <a class="quick-card" href="recruiter-interviews"><span class="quick-icon">04</span><h3>Interviews</h3><p>Schedule, reschedule and manage candidate interviews.</p><span class="card-action">View Interviews →</span></a>
  </section>
</main>
<footer class="app-footer">Smart Placement System · Recruiter Portal</footer>
</body>
</html>
