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
import fr.paris.lutece.util.ReferenceList;
import java.util.List;

/**
 * IDirectoryResponseDAO Interface
 */
public interface IDirectoryResponseDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param directoryResponse
     *            instance of the DirectoryResponse object to insert
     * @param plugin
     *            the Plugin
     */
    void insert( DirectoryResponse directoryResponse, Plugin plugin );

    /**
     * Update the record in the table
     * 
     * @param directoryResponse
     *            the reference of the DirectoryResponse
     * @param plugin
     *            the Plugin
     */
    void store( DirectoryResponse directoryResponse, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nKey
     *            The identifier of the DirectoryResponse to delete
     * @param plugin
     *            the Plugin
     */
    void delete( int nKey, Plugin plugin );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param nKey
     *            The identifier of the directoryResponse
     * @param plugin
     *            the Plugin
     * @return The instance of the directoryResponse
     */
    DirectoryResponse load( int nKey, Plugin plugin );

    /**
     * Load the data of all the directoryResponse objects and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @return The list which contains the data of all the directoryResponse objects
     */
    List<DirectoryResponse> selectDirectoryResponsesList( Plugin plugin );

    /**
     * Load the id of all the directoryResponse objects and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @return The list which contains the id of all the directoryResponse objects
     */
    List<Integer> selectIdDirectoryResponsesList( Plugin plugin );

    /**
     * Load the data of all the directoryResponse objects
     * 
     * @param plugin
     *            the Plugin
     * @return The referenceList which contains the data of all the directoryResponse objects
     */
    ReferenceList selectDirectoryResponsesReferenceList( Plugin plugin );

    /**
     * Load the data of all the entity directoryResponse objects
     * 
     * @param nKey
     *            The identifier of the Entity
     * @param plugin
     *            the Plugin
     * @return List of directory Response
     */
    List<DirectoryResponse> selectDirectoryResponsesListByIdEntity( int nKey, Plugin plugin );

    /**
     * Load the data of directoryResponse object
     * 
     * @param nKey
     *            The identifier of the id Response
     * @param plugin
     *            the Plugin
     * @return the directory Response
     */
    DirectoryResponse selectDirectoryResponseByIdResponse( int nKey, Plugin plugin );

}
