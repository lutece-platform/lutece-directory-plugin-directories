/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.directories.web;

import fr.paris.lutece.plugins.directories.business.DirectoryEntity;
import fr.paris.lutece.plugins.directories.business.DirectoryEntityHome;
import fr.paris.lutece.plugins.directories.business.DirectoryResponse;
import fr.paris.lutece.plugins.directories.business.DirectoryResponseHome;
import fr.paris.lutece.plugins.directories.service.EntryService;
import fr.paris.lutece.plugins.directories.util.DirectoriesConstants;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.business.GenAttFileItem;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseFilter;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeServiceManager;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.IEntryTypeService;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.AbstractEntryTypeUpload;
import org.apache.commons.fileupload.FileItem;

/**
 * This class provides the user interface to manage Directory features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDirectoryResponse.jsp", controllerPath = "jsp/admin/plugins/directories/", right = "DIRECTORIES_MANAGEMENT" )
public class DirectoryResponseJspBean extends AbstractDirectoriesManagerJspBean
{
    private static final long serialVersionUID = -2119684946497061605L;
    // Views
    private static final String VIEW_CREATE_DIRECTORY_RESPONSE = "createDirectoryResponse";
    private static final String VIEW_MODIFY_DIRECTORY_RESPONSE = "modifyDirectoryResponse";
    private static final String VIEW_MANAGE_DIRECTORY_RESPONSE = "manageDirectoryResponse";
    // Actions
    private static final String ACTION_CREATE_DIRECTORY_RESPONSE = "createDirectoryResponse";
    private static final String ACTION_MODIFY_DIRECTORY_RESPONSE = "modifyDirectoryResponse";
    private static final String ACTION_REMOVE_DIRECTORY_RESPONSE = "removeDirectoryResponse";
    private static final String ACTION_CONFIRM_REMOVE_DIRECTORY_RESPONSE = "confirmRemoveDirectoryResponse";

    // Templates
    private static final String TEMPLATE_CREATE_DIRECTORY_RESPONSE = "/admin/plugins/directories/create_directory_response.html";
    private static final String TEMPLATE_MODIFY_DIRECTORY_RESPONSE = "/admin/plugins/directories/modify_directory_response.html";
    private static final String TEMPLATE_MANAGE_DIRECTORY_RESPONSE = "/admin/plugins/directories/manage_directory_response.html";
    private static final String TEMPLATE_HTML_CODE_FORM_ADMIN = "admin/plugins/directories/html_code_form.html";
    // messages
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DIRECTORY_RESPONSE = "directories.entity.title";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DIRECTORY_RESPONSE = "directories.createResponse.title";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DIRECTORY_RESPONSE = "directories.modifyResponse.title";

    private static final String INFO_DIRECTORY_RESPONSE_REMOVED = "directories.info.entity.removed";
    private static final String INFO_REFERENCE_CREATED = "directories.entity.created";
    private static final String MESSAGE_CONFIRM_REMOVE_ENTITY = "directories.message.confirmRemoveEntity";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_ENTITY = "id_entity";
    private static final String MARK_LIST_RESPONSES = "list_responses";

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DIRECTORY_RESPONSE, defaultView = true )
    public String getManageDirectories( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        ResponseFilter responseFilter = new ResponseFilter( );
        responseFilter.setIdResource( nIdDirectory );
        List<DirectoryEntity> listEntity = DirectoryEntityHome.getDirectoryEntitiesListByIdDirectory( nIdDirectory );
        Map<String, Object> model = getModel( );
        model.put( DirectoriesConstants.MARK_LIST_DIRECTORY_ENTITY, listEntity );
        model.put( PARAMETER_ID_DIRECTORY, nIdDirectory );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DIRECTORY_RESPONSE, TEMPLATE_MANAGE_DIRECTORY_RESPONSE, model );
    }

    /**
     * Get the HTML code to create directory response
     * 
     * @param request
     *            The request
     * @return The HTML code to display or the next URL to redirect to
     */
    @View( VIEW_CREATE_DIRECTORY_RESPONSE )
    public String getModifyDirectoryResponse( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        Map<String, Object> model = getModel( );
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( nIdDirectory, true );
        StringBuffer strBuffer = new StringBuffer( );
        for ( Entry entry : listEntryFirstLevel )
        {
            EntryService.getHtmlEntry( model, entry.getIdEntry( ), strBuffer, getLocale( ), false, request );
        }
        model.put( PARAMETER_ID_DIRECTORY, strIdDirectory );
        model.put( DirectoriesConstants.MARK_STR_ENTRY, strBuffer.toString( ) );
        HtmlTemplate templateForm = AppTemplateService.getTemplate( TEMPLATE_HTML_CODE_FORM_ADMIN, getLocale( ), model );
        model.put( DirectoriesConstants.MARK_FORM_HTML, templateForm.getHtml( ) );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_DIRECTORY_RESPONSE, TEMPLATE_CREATE_DIRECTORY_RESPONSE, model );
    }

    /**
     * Get the HTML code to create directory response
     * 
     * @param request
     *            The request
     * @return The HTML code to display or the next URL to redirect to
     */
    @View( VIEW_MODIFY_DIRECTORY_RESPONSE )
    public String getCreateDirectoryResponse( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        String strIdEntity = request.getParameter( PARAMETER_ID_ENTITY );
        int nIdEntity = Integer.parseInt( strIdEntity );
        Map<String, Object> model = getModel( );
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( nIdDirectory, true );
        StringBuffer strBuffer = new StringBuffer( );
        List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( nIdEntity );
        Collection<Response> listResponse = new ArrayList<>( );
        for ( DirectoryResponse directoryResponse : listDirectoryResponse )
        {
            Response response = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
            if ( ( response.getFile( ) != null ) && ( response.getFile( ).getIdFile( ) > 0 ) )
            {
                IEntryTypeService service = EntryTypeServiceManager.getEntryTypeService( response.getEntry( ) );
                File file = FileHome.findByPrimaryKey( response.getFile( ).getIdFile( ) );
                PhysicalFile physicalFile = PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) );
                FileItem fileItem = new GenAttFileItem( physicalFile.getValue( ), file.getTitle( ) );
                ( (AbstractEntryTypeUpload) service ).getAsynchronousUploadHandler( ).addFileItemToUploadedFilesList(
                        fileItem,
                        "nIt" + response.getIterationNumber( ) + "_" + IEntryTypeService.PREFIX_ATTRIBUTE
                                + Integer.toString( response.getEntry( ).getIdEntry( ) ), request );
                response.setFile( file );
            }
            listResponse.add( response );
        }
        model.put( MARK_LIST_RESPONSES, listResponse );
        for ( Entry entry : listEntryFirstLevel )
        {
            EntryService.getHtmlEntry( model, entry.getIdEntry( ), strBuffer, getLocale( ), false, request );
        }
        model.put( PARAMETER_ID_DIRECTORY, strIdDirectory );
        model.put( PARAMETER_ID_ENTITY, strIdEntity );
        model.put( DirectoriesConstants.MARK_STR_ENTRY, strBuffer.toString( ) );
        HtmlTemplate templateForm = AppTemplateService.getTemplate( TEMPLATE_HTML_CODE_FORM_ADMIN, getLocale( ), model );
        model.put( DirectoriesConstants.MARK_FORM_HTML, templateForm.getHtml( ) );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DIRECTORY_RESPONSE, TEMPLATE_MODIFY_DIRECTORY_RESPONSE, model );
    }

    /**
     * Get the HTML code to create directory response
     * 
     * @param request
     *            The request
     * @return The HTML code to display or the next URL to redirect to
     */
    @Action( ACTION_CREATE_DIRECTORY_RESPONSE )
    public String doCreateDirectoryResponse( HttpServletRequest request )
    {
        int nIdDirectory = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( nIdDirectory, true );
        List<Response> listResponse = new ArrayList<>( );
        for ( Entry entry : listEntryFirstLevel )
        {
            entry.setFields( FieldHome.getFieldListByIdEntry( entry.getIdEntry( ) ) );
            EntryTypeServiceManager.getEntryTypeService( entry ).getResponseData( entry, request, listResponse, getLocale( ) );
        }
        AdminUser currentUser = AdminUserService.getAdminUser( request );
        DirectoryEntity entity = new DirectoryEntity( );
        entity.setIdDirectory( nIdDirectory );
        entity.setDateCreation( Timestamp.valueOf( LocalDateTime.now( ) ) );
        entity.setIdCreator( currentUser.getUserId( ) );
        DirectoryEntityHome.create( entity );
        for ( Response response : listResponse )
        {
            DirectoryResponse directoryResponse = new DirectoryResponse( );
            ResponseHome.create( response );
            directoryResponse.setIdDirectory( nIdDirectory );
            directoryResponse.setIdResponse( response.getIdResponse( ) );
            directoryResponse.setIdEntity( entity.getId( ) );
            DirectoryResponseHome.create( directoryResponse );
        }
        Map<String, Object> model = getModel( );
        model.put( PARAMETER_ID_DIRECTORY, nIdDirectory );
        addInfo( I18nService.getLocalizedString( INFO_REFERENCE_CREATED, getLocale( ) ) );
        return redirect( request, VIEW_MANAGE_DIRECTORY_RESPONSE, PARAMETER_ID_DIRECTORY, nIdDirectory );
    }

    /**
     * Get the HTML code to create directory response
     * 
     * @param request
     *            The request
     * @return The HTML code to display or the next URL to redirect to
     */
    @Action( ACTION_MODIFY_DIRECTORY_RESPONSE )
    public String doModifyDirectoryResponse( HttpServletRequest request )
    {
        int nIdDirectory = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );
        String strIdEntity = request.getParameter( PARAMETER_ID_ENTITY );
        int nIdEntity = Integer.parseInt( strIdEntity );
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( nIdDirectory, true );
        List<Response> listResponse = new ArrayList<>( );
        for ( Entry entry : listEntryFirstLevel )
        {
            entry.setFields( FieldHome.getFieldListByIdEntry( entry.getIdEntry( ) ) );
            EntryTypeServiceManager.getEntryTypeService( entry ).getResponseData( entry, request, listResponse, getLocale( ) );
        }
        List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( nIdEntity );
        for ( DirectoryResponse directoryResponse : listDirectoryResponse )
        {
            ResponseHome.remove( directoryResponse.getIdResponse( ) );
            DirectoryResponseHome.remove( directoryResponse.getId( ) );
        }
        for ( Response response : listResponse )
        {
            DirectoryResponse directoryResponse = new DirectoryResponse( );
            ResponseHome.create( response );
            directoryResponse.setIdDirectory( nIdDirectory );
            directoryResponse.setIdResponse( response.getIdResponse( ) );
            directoryResponse.setIdEntity( nIdEntity );
            DirectoryResponseHome.create( directoryResponse );
        }
        AdminUser currentUser = AdminUserService.getAdminUser( request );
        DirectoryEntity entity = DirectoryEntityHome.findByPrimaryKey( nIdEntity );
        entity.setUpdate( Timestamp.valueOf( LocalDateTime.now( ) ) );
        entity.setIdModificator( currentUser.getUserId( ) );
        DirectoryEntityHome.update( entity );
        Map<String, Object> model = getModel( );
        model.put( PARAMETER_ID_DIRECTORY, nIdDirectory );
        addInfo( I18nService.getLocalizedString( INFO_REFERENCE_CREATED, getLocale( ) ) );
        return redirect( request, VIEW_MANAGE_DIRECTORY_RESPONSE, PARAMETER_ID_DIRECTORY, nIdDirectory );
    }

    /**
     * Manages the removal form of a directory response whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DIRECTORY_RESPONSE )
    public String getConfirmRemoveDirectory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTITY ) );
        int nIdDirectory = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DIRECTORY_RESPONSE ) );
        url.addParameter( PARAMETER_ID_ENTITY, nId );
        url.addParameter( PARAMETER_ID_DIRECTORY, nIdDirectory );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ENTITY, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a directory response
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage directories
     */
    @Action( ACTION_REMOVE_DIRECTORY_RESPONSE )
    public String doRemoveDirectory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTITY ) );
        int nIdDirectory = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );

        DirectoryEntityHome.remove( nId );
        addInfo( INFO_DIRECTORY_RESPONSE_REMOVED, getLocale( ) );

        return redirect( request, VIEW_MANAGE_DIRECTORY_RESPONSE, PARAMETER_ID_DIRECTORY, nIdDirectory );
    }

}
