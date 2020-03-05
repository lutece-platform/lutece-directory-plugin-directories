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
package fr.paris.lutece.plugins.directories.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import fr.paris.lutece.plugins.directories.business.DirectoryEntity;
import fr.paris.lutece.plugins.directories.business.DirectoryEntityHome;
import fr.paris.lutece.plugins.directories.business.DirectoryResponse;
import fr.paris.lutece.plugins.directories.business.DirectoryResponseHome;
import fr.paris.lutece.plugins.directories.service.upload.DirectoriesAsynchronousUploadHandler;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.business.GenAttFileItem;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeServiceManager;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.IEntryTypeService;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;

/**
 * This Service manages document actions (create, update, delete, validate ...) .
 */
public class DirectoriesService
{
    private static DirectoriesService _singleton = new DirectoriesService( );

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static DirectoriesService getInstance( )
    {
        return _singleton;
    }

    /**
     * Find and build all the response of all directory entities
     * 
     * 
     * @return the list of directory entities
     */
    public List<DirectoryEntity> getListDocWithoutBinaries( )
    {
        List<DirectoryEntity> listDirectoryEntity = DirectoryEntityHome.getDirectoryEntitiesList( );
        for ( DirectoryEntity directoryEntity : listDirectoryEntity )
        {
            List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( directoryEntity.getId( ) );
            List<Response> listResponse = new ArrayList<>( );
            for ( DirectoryResponse directoryResponse : listDirectoryResponse )
            {
                Response response = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
                if ( response.getFile( ) == null )
                {
                    listResponse.add( response );
                }
                listResponse.add( response );
            }
            directoryEntity.setResponses( listResponse );
        }
        return listDirectoryEntity;
    }

    /**
     * Find and build all the response of an directory entity
     * 
     * @param nIdDirectoryEntity
     *            the directory entity id
     * @return the directory entity
     */
    public DirectoryEntity findByPrimaryKeyWithoutBinaries( int nIdDirectoryEntity )
    {
        DirectoryEntity directoryEntity = DirectoryEntityHome.findByPrimaryKey( nIdDirectoryEntity );
        List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( directoryEntity.getId( ) );
        List<Response> listResponse = new ArrayList<>( );
        for ( DirectoryResponse directoryResponse : listDirectoryResponse )
        {
            Response response = ResponseHome.findByPrimaryKey( directoryResponse.getId( ) );
            if ( response.getFile( ) == null )
            {
                listResponse.add( response );
            }
        }
        directoryEntity.setResponses( listResponse );
        return directoryEntity;
    }

    /**
     * Find and build all the response of an directory entity
     * 
     * @param nIdEntity
     *            the directory entity id
     * @param request
     *            the request
     * @return a list of response
     */
    public static List<Response> findAndBuildListResponse( HttpServletRequest request, int nIdEntity )
    {
        DirectoriesAsynchronousUploadHandler.getHandler( ).removeSessionFiles( request.getSession( ).getId( ) );
        List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( nIdEntity );
        List<Response> listResponse = new ArrayList<>( );
        for ( DirectoryResponse directoryResponse : listDirectoryResponse )
        {
            Response response = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
            if ( ( response.getFile( ) != null ) && ( response.getFile( ).getIdFile( ) > 0 ) )
            {
                File file = FileHome.findByPrimaryKey( response.getFile( ).getIdFile( ) );
                PhysicalFile physicalFile = PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) );
                FileItem fileItem = new GenAttFileItem( physicalFile.getValue( ), file.getTitle( ) );
                DirectoriesAsynchronousUploadHandler.getHandler( ).addFileItemToUploadedFilesList( fileItem,
                        IEntryTypeService.PREFIX_ATTRIBUTE + Integer.toString( response.getEntry( ).getIdEntry( ) ), request );
                response.setFile( file );
            }
            listResponse.add( response );
        }
        return listResponse;
    }

    /**
     * Create directory entity
     * 
     * @param request
     *            the request
     * @param nIdDirectory
     *            the directory id
     * @param strEntityTitle
     *            the entity title
     */
    public static void createEntity( HttpServletRequest request, int nIdDirectory, String strEntityTitle )
    {
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( nIdDirectory, true );
        List<Response> listResponse = new ArrayList<>( );
        for ( Entry entry : listEntryFirstLevel )
        {
            entry.setFields( FieldHome.getFieldListByIdEntry( entry.getIdEntry( ) ) );
            EntryTypeServiceManager.getEntryTypeService( entry ).getResponseData( entry, request, listResponse, request.getLocale( ) );
        }
        AdminUser currentUser = AdminUserService.getAdminUser( request );
        DirectoryEntity entity = new DirectoryEntity( );
        entity.setIdDirectory( nIdDirectory );
        entity.setDateCreation( Timestamp.valueOf( LocalDateTime.now( ) ) );
        entity.setIdCreator( currentUser.getUserId( ) );
        entity.setTitle( strEntityTitle );
        DirectoryEntityHome.create( entity );
        createEntityResponse( listResponse, nIdDirectory, entity.getId( ) );
        DirectoriesAsynchronousUploadHandler.getHandler( ).removeSessionFiles( request.getSession( ).getId( ) );
    }

    /**
     * Modify directory entity
     * 
     * @param request
     *            the request
     * @param nIdDirectory
     *            the directory id
     * @param nIdEntity
     *            the entity id
     * @param strEntityTitle
     *            the entity title
     */
    public static void modifyEntity( HttpServletRequest request, int nIdDirectory, int nIdEntity, String strEntityTitle )
    {
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( nIdDirectory, true );
        List<Response> listResponse = new ArrayList<>( );
        for ( Entry entry : listEntryFirstLevel )
        {
            entry.setFields( FieldHome.getFieldListByIdEntry( entry.getIdEntry( ) ) );
            EntryTypeServiceManager.getEntryTypeService( entry ).getResponseData( entry, request, listResponse, request.getLocale( ) );
        }
        removeEntityResponse( nIdEntity );
        createEntityResponse( listResponse, nIdDirectory, nIdEntity );
        AdminUser currentUser = AdminUserService.getAdminUser( request );
        DirectoryEntity entity = DirectoryEntityHome.findByPrimaryKey( nIdEntity );
        entity.setTitle( strEntityTitle );
        entity.setUpdate( Timestamp.valueOf( LocalDateTime.now( ) ) );
        entity.setIdModificator( currentUser.getUserId( ) );
        DirectoryEntityHome.update( entity );
    }

    /**
     * Create entity response
     * 
     * @param listResponse
     * @param nIdDirectory
     *            the directory id
     * @param nIdEntity
     *            the entity id
     * @param strEntityTitle
     *            the entity title
     */
    public static void createEntityResponse( List<Response> listResponse, int nIdDirectory, int nIdEntity )
    {
        for ( Response response : listResponse )
        {
            DirectoryResponse directoryResponse = new DirectoryResponse( );
            ResponseHome.create( response );
            directoryResponse.setIdDirectory( nIdDirectory );
            directoryResponse.setIdResponse( response.getIdResponse( ) );
            directoryResponse.setIdEntity( nIdEntity );
            DirectoryResponseHome.create( directoryResponse );
        }
    }

    /**
     * Remove entity response
     * 
     * @param nIdEntity
     *            the directory entity id
     */
    public static void removeEntityResponse( int nIdEntity )
    {
        List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( nIdEntity );
        for ( DirectoryResponse directoryResponse : listDirectoryResponse )
        {
            ResponseHome.remove( directoryResponse.getIdResponse( ) );
            DirectoryResponseHome.remove( directoryResponse.getId( ) );
        }
    }
}
