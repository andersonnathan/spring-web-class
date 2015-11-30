
<!-- TODO-01: (Task 1): Deploy this application to the server to make sure it works. Use
                Run As -> Run On Server again but after selecting your server, on the
                next dialog remove any other projects from the right-hand side panel.
                Ask your teacher if unsure. Removing any previously deployed applications
                from your server allows for faster startup.
              Access the home page at http://localhost:8080/mvc-layout and you should
                see the same application that you built in mvc-getting-started.

<!-- TODO-02: (Task 2): Copy almost everything from here into WEB-INF/layouts/standard.jsp.
              Leave behind the taglib lines and the contents of <div id="main">.
              Redisplay the welcome (home) page.  You should see only the title, 
              and caption. -->

<!--  Don't copy these two lines ... -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link type="text/css" rel="stylesheet" href="<c:url value="/styles/springsource.css"/>"/>
	
		<title>
			<fmt:message key="welcome.title" />
		</title>
	</head>
	<body>
		<div id="page">
			<div id="header" class="clearfix">
			</div>
			<div id="content" class="clearfix">
				<div id="main">
					<!--  Start main content - leave this bit. -->
					<h1>
						<fmt:message key="welcome.title" />
					</h1>
					<h2>
						<fmt:message key="welcome.caption" />
					</h2>
					<!--  End main content. -->
				</div>

				<div id="nav">
					<ul class="controlLinks">
						<li>
							<a href="<c:url value="/"/>">
								<fmt:message key="navigate.home"/>
							</a>
						</li>
						<li>
							<a href="<c:url value="/accounts"/>">
								<fmt:message key="navigate.accounts" />
							</a>
						</li>
					</ul>		
				</div>		
			</div>
			<div id="footer" class="clearfix">
				<p><fmt:message key="footer.message"/></p>
			</div>	
		</div>
	</body>
</html>
