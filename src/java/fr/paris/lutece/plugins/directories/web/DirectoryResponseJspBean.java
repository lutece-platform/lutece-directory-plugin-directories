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
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeServiceManager;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Directory features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDirectoryResponse.jsp", controllerPath = "jsp/admin/plugins/directories/", right = "DIRECTORIES_MANAGEMENT" )
public class DirectoryResponseJspBean extends AbstractDirectoriesManagerJspBean
{
    private static final long serialVersionUID = -2119684946497061605L;
    // Views
    private static final String VIEW_CREATE_DIRECTORY_RESPONSE = "createDirectoryResponse";
    private static final String VIEW_MANAGE_DIRECTORY_RESPONSE = "manageDirectoryResponse";
    private static final String ACTION_CREATE_DIRECTORY_RESPONSE = "createDirectoryResponse";
    // Templates
    private static final String TEMPLATE_CREATE_DIRECTORY_RESPONSE = "/admin/plugins/directories/create_directory_response.html";
    private static final String TEMPLATE_MANAGE_DIRECTORY_RESPONSE = "/admin/plugins/directories/manage_directory_response.html";
    private static final String TEMPLATE_HTML_CODE_FORM_ADMIN = "admin/plugins/directories/html_code_form.html";
    // Markers
    private static final String MARK_FORM_HTML = "form_html";
    private static final String MARK_STR_ENTRY = "str_entry";
    // messages
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DIRECTORY_RESPONSE = "directories.entity.title";
    private static final String INFO_REFERENCE_CREATED = "directories.entity.created";
    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id";

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
        Map<String, Object> model = getModel( );
        int nIdDirectory = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );
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
    public String getCreateDirectoryResponse( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        Map<String, Object> model = getModel( );
        List<Entry> listEntryFirstLevel = EntryService.getFilter( nIdDirectory, true );
        StringBuffer strBuffer = new StringBuffer( );
        for ( Entry entry : listEntryFirstLevel )
        {
            EntryService.getHtmlEntry( model, entry.getIdEntry( ), strBuffer, getLocale( ), false, request );
        }
        model.put( PARAMETER_ID_DIRECTORY, strIdDirectory );
        model.put( MARK_STR_ENTRY, strBuffer.toString( ) );
        HtmlTemplate templateForm = AppTemplateService.getTemplate( TEMPLATE_HTML_CODE_FORM_ADMIN, getLocale( ), model );
        model.put( MARK_FORM_HTML, templateForm.getHtml( ) );
        return getPage( "création d'une réponse", TEMPLATE_CREATE_DIRECTORY_RESPONSE, model );
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
        List<Entry> listEntryFirstLevel = EntryService.getFilter( nIdDirectory, true );
        List<Response> listResponse = new ArrayList<>( );

        for ( Entry entry : listEntryFirstLevel )
        {
            entry.setFields( FieldHome.getFieldListByIdEntry( entry.getIdEntry( ) ) );
            EntryTypeServiceManager.getEntryTypeService( entry ).getResponseData( entry, request, listResponse, getLocale( ) );
        }

        DirectoryEntity entity = new DirectoryEntity( );
        entity.setIdDirectory( nIdDirectory );
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
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DIRECTORY_RESPONSE, TEMPLATE_MANAGE_DIRECTORY_RESPONSE, model );
    }
}
