<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@taglib
	uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%><%@taglib
	uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%><%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String servletPath = request.getServletPath();
	application.setAttribute("basePath", basePath);
%><c:set var="ctx" scope="application"
	value="${pageContext.request.contextPath }" /><c:if test="${empty param.tag}"><c:set var="tag" scope="page"
	value="en" /></c:if><c:if test="${!empty param.tag}"><c:set var="tag" scope="page"
	value="${param.tag}" /></c:if><c:set var="midprefix" scope="application"
	value="${ctx }/web/mid/" /><c:set var="midsuffix" scope="application"
	value="?tag=${tag}" /><c:set var="idprefix" scope="application"
	value="${ctx }/web/id/" /><c:set var="idsuffix" scope="application"
	value="?tag=${tag}" /><c:set var="keyprefix" scope="application"
	value="${ctx }/web/key" /><c:set var="keysuffix" scope="application"
	value="?tag=${tag}" />