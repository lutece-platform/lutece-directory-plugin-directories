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
package fr.paris.lutece.plugins.directories.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides instances management methods (create, find, ...) for DirectoryEntity objects
 */
public final class DirectoryEntityHome
{
    // Static variable pointed at the DAO instance
    private static IDirectoryEntityDAO _dao = SpringContextService.getBean( "directories.directoryEntityDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "directories" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DirectoryEntityHome( )
    {
    }

    /**
     * Create an instance of the directoryEntity class
     * 
     * @param directoryEntity
     *            The instance of the DirectoryEntity which contains the informations to store
     * @return The instance of directoryEntity which has been created with its primary key.
     */
    public static DirectoryEntity create( DirectoryEntity directoryEntity )
    {
        _dao.insert( directoryEntity, _plugin );
        return directoryEntity;
    }

    /**
     * Update of the directoryEntity which is specified in parameter
     * 
     * @param directoryEntity
     *            The instance of the DirectoryEntity which contains the data to store
     * @return The instance of the directoryEntity which has been updated
     */
    public static DirectoryEntity update( DirectoryEntity directoryEntity )
    {
        _dao.store( directoryEntity, _plugin );
        return directoryEntity;
    }

    /**
     * Remove the directoryEntity whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryEntity Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a directoryEntity whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryEntity primary key
     * @return an instance of DirectoryEntity
     */
    public static DirectoryEntity findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the directoryEntity objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryEntity objects
     */
    public static List<DirectoryEntity> getDirectoryEntityListByIdDirectory( int nKey )
    {
        return _dao.selectDirectoryEntitiesListByIdDirectory( nKey, _plugin );
    }

    /**
     * Load the data of all the directoryEntity objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryEntity objects
     */
    public static List<DirectoryEntity> getDirectoryEntitiesList( )
    {
        return _dao.selectDirectoryEntitiesList( _plugin );
    }

    /**
     * Load the id of all the directoryEntity objects and returns them as a list
     * 
     * @return the list which contains the id of all the directoryEntity objects
     */
    public static List<Integer> getIdDirectoryEntitiesList( )
    {
        return _dao.selectIdDirectoryEntitiesList( _plugin );
    }

    /**
     * Load the data of all the directoryEntity objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the directoryEntity objects
     */
    public static ReferenceList getDirectoryEntitiesReferenceList( )
    {
        return _dao.selectDirectoryEntitiesReferenceList( _plugin );
    }

    /**
     * Fill name for creator and modificator
     * 
     */
    public static void fillAdminUserName( List<DirectoryEntity> listEntity )
    {
        for ( DirectoryEntity entity : listEntity )
        {
            int nIdCreator = entity.getIdCreator( );
            int nIdModificator = entity.getIdModificator( );
            if ( nIdCreator != 0 )
            {
                entity.setCreator( nIdCreator );
            }
            if ( nIdModificator != 0 )
            {
                entity.setModificator( nIdModificator );
            }
        }
    }

    /**
     * Search in entity list ( title, creator , modificator )
     * 
     * @return a filtered list
     */
    public static List<DirectoryEntity> filter( String searchValue, List<DirectoryEntity> listEntity )
    {
        String [ ] terms = searchValue.split( " " );
        List<DirectoryEntity> listEntityFilter = new ArrayList<>( listEntity );
        for ( String term : terms )
        {
            List<DirectoryEntity> list = listEntityFilter.parallelStream( ).filter( x -> x.getCreator( ).matches( "(?i).*" + term + ".*" )
                    || x.getModificator( ).matches( "(?i).*" + term + ".*" ) || x.getTitle( ).matches( "(?i).*" + term + ".*" ) )
                    .collect( Collectors.toList( ) );
            listEntityFilter = new ArrayList<>( list );
        }
        return listEntityFilter;
    }
}
