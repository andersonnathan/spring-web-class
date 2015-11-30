<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>
		<fmt:message>
			<tiles:insertAttribute name="title"/>
		</fmt:message>
	</title>
	<style type="text/css" media="screen">
        @import url("<c:url value="/styles/css-framework/css/tools.css" />");
        @import url("<c:url value="/styles/css-framework/css/typo.css" />");
        @import url("<c:url value="/styles/css-framework/css/layout-navleft-1col.css" />");
        @import url("<c:url value="/styles/css-framework/css/layout.css" />");
        @import url("<c:url value="/styles/css-framework/css/nav-vertical.css" />");
        @import url("<c:url value="/styles/richweb.css" />");
	</style>
 
	<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
	<script src="<c:url value="/resources/js/jquery-1.11.0.min.js"/>" type="text/javascript"></script>
	<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
</head>
<body>
	<div id="page">
		<div id="header" class="clearfix">
			<div id="branding">
				<img src="<c:url value="/images/springsource_banner_green.png"/>" />
			</div>
		</div>
		<div id="content" class="clearfix">
			<div id="nav">
				<tiles:importAttribute name="navigationTab" />
				<ul class="clearfix">
					<li>
						<c:if test="${navigationTab eq 'home'}">
							<strong>
								<a href="<c:url value="/"/>">
									<fmt:message key="navigate.home"/>
								</a>
							</strong>
						</c:if>
						<c:if test="${navigationTab != 'home'}">
							<a href="<c:url value="/"/>">
								<fmt:message key="navigate.home"/>
							</a>
						</c:if>
					</li>
					<li>
						<c:if test="${navigationTab eq 'accounts'}">
							<strong>
								<a href="<c:url value="/accounts/search"/>">
									<fmt:message key="navigate.accounts"/>
								</a>
							</strong>
						</c:if>
						<c:if test="${navigationTab != 'accounts'}">
							<a href="<c:url value="/accounts/search"/>">
								<fmt:message key="navigate.accounts"/>
							</a>
						</c:if>
					</li>
				</ul>
			</div>
			<div id="main">
				<tiles:insertAttribute name="main" />
				<hr/>
			</div>
		</div>
		<div id="footer" class="clearfix">
			<p><fmt:message key="footer.message"/></p>
		</div>
	</div>
</body>
</html>
