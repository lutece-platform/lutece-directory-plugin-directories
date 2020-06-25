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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;

/**
 * This is the business class for the object DirectoryEntity
 */
public class DirectoryEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    // Variables declarations
    private int _nId;
    private int _nIdDirectory;
    private List<Response> _listResponses;
    private int _nIdCreator;
    private String _strCreator = "";
    private Timestamp _dateCreation;
    private int _nIdModificator;
    private String _strModificator = "";
    private Timestamp _dateUpdate;
    private String _strTitle;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the IdDirectory
     * 
     * @return The IdDirectory
     */
    public int getIdDirectory( )
    {
        return _nIdDirectory;
    }

    /**
     * Sets the IdDirectory
     * 
     * @param nIdDirectory
     *            The IdDirectory
     */
    public void setIdDirectory( int nIdDirectory )
    {
        _nIdDirectory = nIdDirectory;
    }

    /**
     * Returns responses
     * 
     * @return The IdDirectory
     */
    public List<Response> getResponses( )
    {
        return new ArrayList<>( _listResponses );
    }

    /**
     * Sets responses
     * 
     * @param listResponses
     *            Entity Responses
     */
    public void setResponses( List<Response> listResponses )
    {
        _listResponses = new ArrayList<>( listResponses );
    }

    /**
     * @return the _dateCreation
     */
    public Timestamp getCreation( )
    {
        return (Timestamp) _dateCreation.clone( );
    }

    /**
     * @param dateCreation
     *            the dateCreation to set
     */
    public void setDateCreation( Timestamp dateCreation )
    {
        this._dateCreation = (Timestamp) dateCreation.clone( );
    }

    /**
     * @return the _dateUpdate
     */
    public Timestamp getUpdate( )
    {
        return _dateUpdate != null ? (Timestamp) _dateUpdate.clone( ) : null;
    }

    /**
     * @param dateUpdate
     *            the dateUpdate to set
     */
    public void setUpdate( Timestamp dateUpdate )
    {
        this._dateUpdate = dateUpdate != null ? (Timestamp) dateUpdate.clone( ) : null;
    }

    /**
     * Returns the _nIdCreator
     * 
     * @return The _nIdCreator
     */
    public int getIdCreator( )
    {
        return _nIdCreator;
    }

    /**
     * Sets the _nIdCreator
     * 
     * @param nIdCreator
     *            The Id Creator
     */
    public void setIdCreator( int nIdCreator )
    {
        _nIdCreator = nIdCreator;
    }

    /**
     * Returns the _nIdModificator
     * 
     * @return The _nIdModificator
     */
    public int getIdModificator( )
    {
        return _nIdModificator;
    }

    /**
     * Sets the _nIdModificator
     * 
     * @param nIdModificator
     *            The Id Modificator
     */
    public void setIdModificator( int nIdModificator )
    {
        _nIdModificator = nIdModificator;
    }

    /**
     * Returns the _strCreator
     * 
     * @return The _strCreator
     */
    public String getCreator( )
    {
        return _strCreator;
    }

    /**
     * Sets the _strCreator
     * 
     * @param nIdCreator
     *            The Id Creator
     */
    public void setCreator( int nIdCreator )
    {
        _strCreator = getUserAdmin( nIdCreator );
    }

    /**
     * Returns the _strModificator
     * 
     * @return The _strModificator
     */
    public String getModificator( )
    {
        return _strModificator;
    }

    /**
     * Sets the _strModificator
     * 
     * @param nIdModificator
     *            The Id Modificator
     */
    public void setModificator( int nIdModificator )
    {
        _strModificator = getUserAdmin( nIdModificator );
    }

    /**
     * get the Admin user
     * 
     * @param nUserId
     *            The Id Admin User
     */
    private String getUserAdmin( int nUserId )
    {
        AdminUser adminUser = AdminUserHome.findByPrimaryKey( nUserId );
        String name = "";
        if ( adminUser != null )
        {
            name = adminUser.getLastName( ) + " " + adminUser.getFirstName( );
        }
        return name;
    }

    /**
     * Returns the title
     * 
     * @return The title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Sets the title
     * 
     * @param strTitle
     *            The title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }
}
