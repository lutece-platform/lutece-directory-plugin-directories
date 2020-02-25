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
package fr.paris.lutece.plugins.directories.service;

import java.util.ArrayList;
import java.util.List;
import fr.paris.lutece.plugins.directories.business.DirectoryEntity;
import fr.paris.lutece.plugins.directories.business.DirectoryEntityHome;
import fr.paris.lutece.plugins.directories.business.DirectoryResponse;
import fr.paris.lutece.plugins.directories.business.DirectoryResponseHome;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;

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
                listResponse.add( response );
            }
            directoryEntity.setResponses( listResponse );
        }
        return listDirectoryEntity;
    }

    public DirectoryEntity findByPrimaryKeyWithoutBinaries( int nIdDirectoryEntity )
    {
        DirectoryEntity directoryEntity = DirectoryEntityHome.findByPrimaryKey( nIdDirectoryEntity );
        List<DirectoryResponse> listDirectoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdEntity( directoryEntity.getId( ) );
        List<Response> listResponse = new ArrayList<>( );
        for ( DirectoryResponse directoryResponse : listDirectoryResponse )
        {
            Response response = ResponseHome.findByPrimaryKey( directoryResponse.getId( ) );
            listResponse.add( response );
        }
        directoryEntity.setResponses( listResponse );
        return directoryEntity;
    }
}
