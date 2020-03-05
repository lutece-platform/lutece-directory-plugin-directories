/*
 * Copyright (c) 2002-2020, City of Paris
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

import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.plugins.directories.business.DirectoryEntity;
import fr.paris.lutece.plugins.directories.business.DirectoryEntityHome;
import fr.paris.lutece.plugins.directories.business.DirectoryResponse;
import fr.paris.lutece.plugins.directories.business.DirectoryResponseHome;
import fr.paris.lutece.plugins.directories.service.EntryService;
import fr.paris.lutece.plugins.directories.util.DirectoriesConstants;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "directories", pageTitleI18nKey = "directories.xpage.directories.pageTitle", pagePathI18nKey = "directories.xpage.directories.pagePathLabel" )
public class DirectoriesApp extends MVCApplication
{
    private static final long serialVersionUID = -7930244683357483911L;
    private static final String TEMPLATE_ENTITY = "/skin/plugins/directories/directory_entity.html";
    private static final String TEMPLATE_HTML_CODE_FORM = "skin/plugins/directories/html_code_form.html";
    private static final String VIEW_DIRECTORY_ENTITY = "viewDirectoryEntity";
    private static final String VIEW_MODIFY_DIRECTORY_ENTITY = "viewModifyDirectoryEntity";
    private static final String PARAMETER_ID_ENTITY = "entity_id";
    private static final String MARK_ENTITY = "entity";
    private static final String MARK_LIST_RESPONSES = "list_responses";

    /**
     * Returns Entity Data.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_DIRECTORY_ENTITY )
    public XPage viewDirectoryEntity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTITY ) );
        Map<String, Object> model = getModel( );
        List<Response> listResponse = new ArrayList<>( );
        StringBuilder strBuilder = new StringBuilder( );
        DirectoryEntity directoryEntity = DirectoryEntityHome.findByPrimaryKey( nId );
        for ( DirectoryResponse directoryResponse : DirectoryResponseHome.getDirectoryResponsesListByIdEntity( nId ) )
        {
            Response response = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
            if ( ( response.getFile( ) != null ) && ( response.getFile( ).getIdFile( ) > 0 ) )
            {
                File file = FileHome.findByPrimaryKey( response.getFile( ).getIdFile( ) );
                response.setFile( file );
            }
            listResponse.add( response );
        }
        directoryEntity.setResponses( listResponse );
        model.put( MARK_ENTITY, directoryEntity );
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( directoryEntity.getIdDirectory( ), true );

        for ( Entry entry : listEntryFirstLevel )
        {
            List<Response> listEntryResponse = listResponse.stream( ).filter( response -> response.getEntry( ).getIdEntry( ) == entry.getIdEntry( ) )
                    .collect( Collectors.toCollection( ArrayList::new ) );
            model.put( MARK_LIST_RESPONSES, listEntryResponse );
            EntryService.getHtmlEntryReadOnly( model, entry.getIdEntry( ), strBuilder, getLocale( request ), true );
        }
        model.put( DirectoriesConstants.MARK_STR_ENTRY, strBuilder.toString( ) );
        HtmlTemplate templateForm = AppTemplateService.getTemplate( TEMPLATE_HTML_CODE_FORM, getLocale( request ), model );
        model.put( DirectoriesConstants.MARK_FORM_HTML, templateForm.getHtml( ) );

        return getXPage( TEMPLATE_ENTITY, request.getLocale( ), model );
    }

    /**
     * Returns Entity Data.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_MODIFY_DIRECTORY_ENTITY )
    public XPage viewModifyDirectoryEntity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTITY ) );
        Map<String, Object> model = getModel( );
        List<Response> listResponse = new ArrayList<>( );
        StringBuilder strBuilder = new StringBuilder( );
        DirectoryEntity directoryEntity = DirectoryEntityHome.findByPrimaryKey( nId );
        for ( DirectoryResponse directoryResponse : DirectoryResponseHome.getDirectoryResponsesListByIdEntity( nId ) )
        {
            Response response = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
            if ( ( response.getFile( ) != null ) && ( response.getFile( ).getIdFile( ) > 0 ) )
            {
                File file = FileHome.findByPrimaryKey( response.getFile( ).getIdFile( ) );
                response.setFile( file );
            }
            listResponse.add( response );
        }
        directoryEntity.setResponses( listResponse );
        model.put( MARK_ENTITY, directoryEntity );
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( directoryEntity.getIdDirectory( ), true );

        for ( Entry entry : listEntryFirstLevel )
        {
            List<Response> listEntryResponse = listResponse.stream( ).filter( response -> response.getEntry( ).getIdEntry( ) == entry.getIdEntry( ) )
                    .collect( Collectors.toCollection( ArrayList::new ) );
            model.put( MARK_LIST_RESPONSES, listEntryResponse );
            EntryService.getHtmlEntry( model, entry.getIdEntry( ), strBuilder, getLocale( request ), true );
        }
        model.put( DirectoriesConstants.MARK_STR_ENTRY, strBuilder.toString( ) );
        HtmlTemplate templateForm = AppTemplateService.getTemplate( TEMPLATE_HTML_CODE_FORM, getLocale( request ), model );
        model.put( DirectoriesConstants.MARK_FORM_HTML, templateForm.getHtml( ) );

        return getXPage( TEMPLATE_ENTITY, request.getLocale( ), model );
    }

}
