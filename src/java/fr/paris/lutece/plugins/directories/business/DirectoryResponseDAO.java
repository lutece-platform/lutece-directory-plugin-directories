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

package fr.paris.lutece.plugins.directories.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for DirectoryResponse objects
 */
public final class DirectoryResponseDAO implements IDirectoryResponseDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_directory_response, id_directory, id_response, id_entity FROM directories_directory_response WHERE id_directory_response = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directories_directory_response ( id_directory, id_response, id_entity ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directories_directory_response WHERE id_directory_response = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directories_directory_response SET id_directory_response = ?, id_directory = ?, id_response = ?, id_entity = ? WHERE id_directory_response = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_directory_response, id_directory, id_response, id_entity FROM directories_directory_response";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_directory_response FROM directories_directory_response";
    private static final String SQL_QUERY_SELECTALL_BY_ENTITY = "SELECT id_directory_response, id_directory, id_response, id_entity FROM directories_directory_response WHERE id_entity = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DirectoryResponse directoryResponse, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin );
        try
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, directoryResponse.getIdDirectory( ) );
            daoUtil.setInt( nIndex++, directoryResponse.getIdResponse( ) );
            daoUtil.setInt( nIndex++, directoryResponse.getIdEntity( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                directoryResponse.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        finally
        {
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DirectoryResponse load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );
        DirectoryResponse directoryResponse = null;

        if ( daoUtil.next( ) )
        {
            directoryResponse = new DirectoryResponse( );
            int nIndex = 1;

            directoryResponse.setId( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdResponse( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdEntity( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free( );
        return directoryResponse;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DirectoryResponse directoryResponse, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, directoryResponse.getId( ) );
        daoUtil.setInt( nIndex++, directoryResponse.getIdDirectory( ) );
        daoUtil.setInt( nIndex++, directoryResponse.getIdResponse( ) );
        daoUtil.setInt( nIndex++, directoryResponse.getIdEntity( ) );
        daoUtil.setInt( nIndex, directoryResponse.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DirectoryResponse> selectDirectoryResponsesList( Plugin plugin )
    {
        List<DirectoryResponse> directoryResponseList = new ArrayList<DirectoryResponse>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            DirectoryResponse directoryResponse = new DirectoryResponse( );
            int nIndex = 1;

            directoryResponse.setId( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdResponse( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdEntity( daoUtil.getInt( nIndex++ ) );

            directoryResponseList.add( directoryResponse );
        }

        daoUtil.free( );
        return directoryResponseList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDirectoryResponsesList( Plugin plugin )
    {
        List<Integer> directoryResponseList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            directoryResponseList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return directoryResponseList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDirectoryResponsesReferenceList( Plugin plugin )
    {
        ReferenceList directoryResponseList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            directoryResponseList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return directoryResponseList;
    }

    @Override
    public List<DirectoryResponse> selectDirectoryResponsesListByIdEntity( int nKey, Plugin plugin )
    {
        List<DirectoryResponse> directoryResponseList = new ArrayList<DirectoryResponse>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_ENTITY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            DirectoryResponse directoryResponse = new DirectoryResponse( );
            int nIndex = 1;

            directoryResponse.setId( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdResponse( daoUtil.getInt( nIndex++ ) );
            directoryResponse.setIdEntity( daoUtil.getInt( nIndex++ ) );

            directoryResponseList.add( directoryResponse );
        }

        daoUtil.free( );
        return directoryResponseList;
    }
}
