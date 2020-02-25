<jsp:useBean id="directoriesManagerDirectoryFields" scope="session"
    class="fr.paris.lutece.plugins.directories.web.DirectoryFieldsJspBean" />
<% String strContent = directoriesManagerDirectoryFields.processController ( request , response ); %>
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />
<%= strContent %>
<%@ include file="../../AdminFooter.jsp" %>