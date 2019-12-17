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
 * This class provides Data Access methods for DirectoryEntity objects
 */
public final class DirectoryEntityDAO implements IDirectoryEntityDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_reference, id_directory FROM directories_directory_entity WHERE id_reference = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directories_directory_entity ( id_directory ) VALUES ( ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directories_directory_entity WHERE id_reference = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directories_directory_entity SET id_reference = ?, id_directory = ? WHERE id_reference = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_reference, id_directory FROM directories_directory_entity";
    private static final String SQL_QUERY_SELECTALL_BY_DIRECTORY = "SELECT id_reference, id_directory FROM directories_directory_entity WHERE id_directory = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_reference FROM directories_directory_entity";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DirectoryEntity directoryEntity, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, directoryEntity.getIdDirectory( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                directoryEntity.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DirectoryEntity load( int nKey, Plugin plugin )
    {
        DirectoryEntity directoryEntity = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                directoryEntity = new DirectoryEntity( );
                int nIndex = 1;
                directoryEntity.setId( daoUtil.getInt( nIndex++ ) );
                directoryEntity.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            }
        }
        return directoryEntity;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DirectoryEntity directoryEntity, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, directoryEntity.getId( ) );
            daoUtil.setInt( nIndex++, directoryEntity.getIdDirectory( ) );
            daoUtil.setInt( nIndex, directoryEntity.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DirectoryEntity> selectDirectoryEntitiesList( Plugin plugin )
    {
        List<DirectoryEntity> directoryEntityList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                DirectoryEntity directoryEntity = new DirectoryEntity( );
                int nIndex = 1;
                directoryEntity.setId( daoUtil.getInt( nIndex++ ) );
                directoryEntity.setIdDirectory( daoUtil.getInt( nIndex++ ) );
                directoryEntityList.add( directoryEntity );
            }
        }
        return directoryEntityList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDirectoryEntitiesList( Plugin plugin )
    {
        List<Integer> directoryEntityList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                directoryEntityList.add( daoUtil.getInt( 1 ) );
            }
        }
        return directoryEntityList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDirectoryEntitiesReferenceList( Plugin plugin )
    {
        ReferenceList directoryEntityList = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                directoryEntityList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return directoryEntityList;
    }

    @Override
    public List<DirectoryEntity> selectDirectoryEntitiesListByIdDirectory( int nKey, Plugin plugin )
    {
        List<DirectoryEntity> directoryEntityList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_DIRECTORY, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                DirectoryEntity directoryEntity = new DirectoryEntity( );
                int nIndex = 1;
                directoryEntity.setId( daoUtil.getInt( nIndex++ ) );
                directoryEntity.setIdDirectory( daoUtil.getInt( nIndex++ ) );
                directoryEntityList.add( directoryEntity );
            }
        }
        return directoryEntityList;
    }
}
