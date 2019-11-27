<jsp:useBean id="directoriesmanagerDirectoryEntries" scope="session" class="fr.paris.lutece.plugins.directories.web.DirectoryEntriesJspBean" />
<% String strContent = directoriesmanagerDirectoryEntries.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
