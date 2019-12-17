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

import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.plugins.directories.business.DirectoryEntity;
import fr.paris.lutece.plugins.directories.business.DirectoryEntityHome;
import fr.paris.lutece.plugins.directories.business.DirectoryResponse;
import fr.paris.lutece.plugins.directories.business.DirectoryResponseHome;
import fr.paris.lutece.plugins.directories.service.EntryService;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseFilter;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "directories", pageTitleI18nKey = "directories.xpage.directories.pageTitle", pagePathI18nKey = "directories.xpage.directories.pagePathLabel" )
public class DirectoriesApp extends MVCApplication
{

    private static final long serialVersionUID = -7930244683357483911L;
    private static final String TEMPLATE_XPAGE = "/skin/plugins/directories/directories_directory_search.html";
    private static final String TEMPLATE_ENTITY = "/skin/plugins/directories/directories_directory_entity.html";
    private static final String VIEW_SEARCH_DIRECTORY = "searchDirectoryResponse";
    private static final String VIEW_DIRECTORY_RESPONSE = "viewDirectoryEntity";
    private static final String PARAMETER_ID_DIRECTORY = "id";
    private static final String PARAMETER_SEARCH_FILTER = "search_filter";
    private static final String PARAMETER_SEARCH_RESULT = "search_result";
    private static final String PARAMETER_ID_ENTITY = "idEntity";

    /**
     * Returns the content of the page directories.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_SEARCH_DIRECTORY, defaultView = true )
    public XPage viewSearchDirectoryResponse( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );
        Map<String, Object> model = getModel( );
        model.put( PARAMETER_ID_DIRECTORY, nId );
        model.put( PARAMETER_SEARCH_FILTER, getDistinctResponses( nId ) );
        model.put( PARAMETER_SEARCH_RESULT, getSearchResult( request, nId ) );
        return getXPage( TEMPLATE_XPAGE, request.getLocale( ), model );
    }

    /**
     * Returns Entity Data.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_DIRECTORY_RESPONSE )
    public XPage viewDirectoryResponse( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTITY ) );
        Map<String, Object> model = getModel( );
        List<Response> listResponse = new ArrayList<>( );
        DirectoryEntity directoryEntity = DirectoryEntityHome.findByPrimaryKey( nId );
        for ( DirectoryResponse directoryResponse : DirectoryResponseHome.getDirectoryResponsesListByIdEntity( nId ) )
        {
            Response response = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
            listResponse.add( response );
        }
        directoryEntity.setResponses( listResponse );
        model.put( PARAMETER_SEARCH_RESULT, directoryEntity );
        model.put( PARAMETER_ID_ENTITY, nId );
        return getXPage( TEMPLATE_ENTITY, request.getLocale( ), model );
    }

    /**
     * Returns Entities search results.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    private List<DirectoryEntity> getSearchResult( HttpServletRequest request, int nIdDirectory )
    {
        List<DirectoryEntity> listDirectoryEntity = DirectoryEntityHome.getDirectoryEntitiesListByIdDirectory( nIdDirectory );
        for ( DirectoryEntity directoryEntity : listDirectoryEntity )
        {
            List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( directoryEntity.getId( ) );
            List<Response> listResponse = new ArrayList<>( );
            for ( DirectoryResponse directoryResponse : listDirectoryResponse )
            {
                Response response = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
                listResponse.add( response );
            }
            directoryEntity.setResponses( listResponse );
        }
        return listDirectoryEntity;
    }

    /**
     * Returns dinstinct entity entries responses.
     * 
     * @param nidDirectory
     *            the id of directory
     * @return list of disinct response
     */
    private ArrayList<List<Response>> getDistinctResponses( int nidDirectory )
    {
        List<Entry> listEntryFirstLevel = EntryService.getFilter( nidDirectory, true );
        ArrayList<List<Response>> listListResponse = new ArrayList<>( );
        ArrayList<List<Response>> listListResponseDistinct = new ArrayList<>( );
        for ( Entry entry : listEntryFirstLevel )
        {
            ResponseFilter responseFilter = new ResponseFilter( );
            responseFilter.setIdEntry( entry.getIdEntry( ) );
            listListResponse.add( ResponseHome.getResponseList( responseFilter ) );
        }
        for ( List<Response> listResponse : listListResponse )
        {
            Set<String> responseValueSet = new HashSet<>( );
            List<Response> listResponseDistinctValue = listResponse.stream( ).filter( response -> responseValueSet.add( response.getResponseValue( ) ) )
                    .collect( Collectors.toList( ) );
            listListResponseDistinct.add( listResponseDistinctValue );
        }
        return listListResponseDistinct;
    }
}
