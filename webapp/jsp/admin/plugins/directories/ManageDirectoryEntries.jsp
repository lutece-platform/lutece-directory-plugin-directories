<jsp:useBean id="directoriesManagerDirectoryEntries" scope="session"
    class="fr.paris.lutece.plugins.directories.web.DirectoryEntriesJspBean" />
<% String strContent = directoriesManagerDirectoryEntries.processController ( request , response ); %>
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />
<%= strContent %>
<%@ include file="../../AdminFooter.jsp" %>