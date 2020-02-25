<jsp:useBean id="directoriesManagerDirectory" scope="session"
    class="fr.paris.lutece.plugins.directories.web.DirectoryJspBean" />
<% String strContent = directoriesManagerDirectory.processController ( request , response ); %>
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />
<%= strContent %>
<%@ include file="../../AdminFooter.jsp" %>