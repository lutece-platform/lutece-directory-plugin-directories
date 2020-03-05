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
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for DirectoryResponse objects
 */
public final class DirectoryResponseHome
{
    // Static variable pointed at the DAO instance
    private static IDirectoryResponseDAO _dao = SpringContextService.getBean( "directories.directoryResponseDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "directories" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DirectoryResponseHome( )
    {
    }

    /**
     * Create an instance of the directoryResponse class
     * 
     * @param directoryResponse
     *            The instance of the DirectoryResponse which contains the informations to store
     * @return The instance of directoryResponse which has been created with its primary key.
     */
    public static DirectoryResponse create( DirectoryResponse directoryResponse )
    {
        _dao.insert( directoryResponse, _plugin );

        return directoryResponse;
    }

    /**
     * Update of the directoryResponse which is specified in parameter
     * 
     * @param directoryResponse
     *            The instance of the DirectoryResponse which contains the data to store
     * @return The instance of the directoryResponse which has been updated
     */
    public static DirectoryResponse update( DirectoryResponse directoryResponse )
    {
        _dao.store( directoryResponse, _plugin );

        return directoryResponse;
    }

    /**
     * Remove the directoryResponse whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryResponse Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a directoryResponse whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryResponse primary key
     * @return an instance of DirectoryResponse
     */
    public static DirectoryResponse findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the directoryResponse objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryResponse objects
     */
    public static List<DirectoryResponse> getDirectoryResponsesList( )
    {
        return _dao.selectDirectoryResponsesList( _plugin );
    }

    /**
     * Load the data of all the entity directoryResponse objects and returns them as a list
     * 
     * @param nKey
     *            The directoryResponse Id
     * @return the list which contains the data of all the directoryResponse objects
     */
    public static List<DirectoryResponse> getDirectoryResponsesListByIdEntity( int nKey )
    {
        return _dao.selectDirectoryResponsesListByIdEntity( nKey, _plugin );
    }

    /**
     * Load the data of directoryResponse object
     * 
     * @param nKey
     *            The directoryResponse Id
     * @return the list which contains the data of all the directoryResponse objects
     */
    public static DirectoryResponse getDirectoryResponsesListByIdResponse( int nKey )
    {
        return _dao.selectDirectoryResponseByIdResponse( nKey, _plugin );
    }

}
