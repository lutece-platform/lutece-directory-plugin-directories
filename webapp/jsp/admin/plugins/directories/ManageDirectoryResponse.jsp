<jsp:useBean id="directoriesmanagerDirectoryResponse" scope="session" class="fr.paris.lutece.plugins.directories.web.DirectoryResponseJspBean" />
<% String strContent = directoriesmanagerDirectoryResponse.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
