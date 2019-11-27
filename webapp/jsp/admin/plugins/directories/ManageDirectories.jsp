<jsp:useBean id="directoriesmanagerDirectory" scope="session" class="fr.paris.lutece.plugins.directories.web.DirectoryJspBean" />
<% String strContent = directoriesmanagerDirectory.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
