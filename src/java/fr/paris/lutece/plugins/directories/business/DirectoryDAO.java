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
 * This class provides Data Access methods for Directory objects
 */
public final class DirectoryDAO implements IDirectoryDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_reference, name, description FROM directories_directory WHERE id_reference = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directories_directory ( name, description ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directories_directory WHERE id_reference = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directories_directory SET id_reference = ?, name = ?, description = ? WHERE id_reference = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_reference, name, description FROM directories_directory";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_reference FROM directories_directory";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Directory directory, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin );
        try
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, directory.getName( ) );
            daoUtil.setString( nIndex++, directory.getDescription( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                directory.setId( daoUtil.getGeneratedKeyInt( 1 ) );
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
    public Directory load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );
        Directory directory = null;

        if ( daoUtil.next( ) )
        {
            directory = new Directory( );
            int nIndex = 1;

            directory.setId( daoUtil.getInt( nIndex++ ) );
            directory.setName( daoUtil.getString( nIndex++ ) );
            directory.setDescription( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return directory;
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
    public void store( Directory directory, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, directory.getId( ) );
        daoUtil.setString( nIndex++, directory.getName( ) );
        daoUtil.setString( nIndex++, directory.getDescription( ) );
        daoUtil.setInt( nIndex, directory.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Directory> selectDirectoriesList( Plugin plugin )
    {
        List<Directory> directoryList = new ArrayList<Directory>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Directory directory = new Directory( );
            int nIndex = 1;

            directory.setId( daoUtil.getInt( nIndex++ ) );
            directory.setName( daoUtil.getString( nIndex++ ) );
            directory.setDescription( daoUtil.getString( nIndex++ ) );

            directoryList.add( directory );
        }

        daoUtil.free( );
        return directoryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDirectoriesList( Plugin plugin )
    {
        List<Integer> directoryList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            directoryList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return directoryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDirectoriesReferenceList( Plugin plugin )
    {
        ReferenceList directoryList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            directoryList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return directoryList;
    }
}
