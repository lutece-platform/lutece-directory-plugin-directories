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
package fr.paris.lutece.plugins.directories.service.entrytype;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import fr.paris.lutece.plugins.directories.service.DirectoriesPlugin;
import fr.paris.lutece.plugins.directories.service.EntryService;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.GenericAttributeError;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.AbstractEntryTypeMyLuteceUser;
import fr.paris.lutece.plugins.genericattributes.util.GenericAttributesUtils;
import fr.paris.lutece.plugins.mylutece.business.attribute.AttributeHome;
import fr.paris.lutece.plugins.mylutece.business.attribute.IAttribute;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * class EntryTypeText
 */
public class EntryTypeMyLuteceAttribute extends AbstractEntryTypeMyLuteceUser
{
    /**
     * Name of the bean of this service
     */
    public static final String BEAN_NAME = "directories.entryTypeMyLuteceAttribute";
    public static final String CONSTANT_COMMA = ",";
    private static final String TEMPLATE_CREATE = "admin/plugins/directories/entries/create_entry_type_mylutece_user_attribute.html";
    private static final String TEMPLATE_MODIFY = "admin/plugins/directories/entries/modify_entry_type_mylutece_user_attribute.html";
    private static final String TEMPLATE_READONLY_BACKOFFICE = "admin/plugins/directories/entries/readonly_entry_type_mylutece_user_attribute.html";
    private static final String TEMPLATE_EDITION_BACKOFFICE = "admin/plugins/directories/entries/fill_entry_type_mylutece_user_attribute.html";
    private static final String TEMPLATE_EDITION_FRONTOFFICE = "skin/plugins/directories/entries/fill_entry_type_mylutece_user_attribute.html";
    private static final String TEMPLATE_READONLY_FRONTOFFICE = "skin/plugins/directories/entries/readonly_entry_type_mylutece_user_attribute.html";
    private static final String PROPERTY_ENTRY_TITLE = "directories.entryTypeMyLuteceUserAttribute.title";
    private static final String PARAMETER_ONLY_DISPLAY_IN_BACK = "only_display_in_back";
    private static final String PARAMETER_MYLUTECE_ATTRIBUTE_NAME = "mylutece_attribute_name";
    private ReferenceList _refListUserAttributes;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateHtmlForm( Entry entry, boolean bDisplayFront )
    {
        if ( bDisplayFront )
        {
            return TEMPLATE_EDITION_FRONTOFFICE;
        }
        return TEMPLATE_EDITION_BACKOFFICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateCreate( Entry entry, boolean bDisplayFront )
    {
        return TEMPLATE_CREATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateModify( Entry entry, boolean bDisplayFront )
    {
        return TEMPLATE_MODIFY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestData( Entry entry, HttpServletRequest request, Locale locale )
    {
        initCommonRequestData( entry, request );
        String strOnlyDisplayInBack = request.getParameter( PARAMETER_ONLY_DISPLAY_IN_BACK );
        entry.setTitle( I18nService.getLocalizedString( PROPERTY_ENTRY_TITLE, locale ) );
        entry.setComment( StringUtils.EMPTY );
        entry.setMandatory( Boolean.parseBoolean( request.getParameter( PARAMETER_MANDATORY ) ) );
        entry.setCSSClass( request.getParameter( PARAMETER_CSS_CLASS ) );
        entry.setTitle( request.getParameter( PARAMETER_TITLE ) );
        entry.setHelpMessage( request.getParameter( PARAMETER_HELP_MESSAGE ) );
        entry.setIndexed( request.getParameter( PARAMETER_INDEXED ) != null );
        entry.setOnlyDisplayInBack( strOnlyDisplayInBack != null );
        entry.setCode( request.getParameter( PARAMETER_ENTRY_CODE ) );
        GenericAttributesUtils.createOrUpdateField( entry, FIELD_MYLUTECE_ATTRIBUTE_NAME_CODE, null, request.getParameter( PARAMETER_MYLUTECE_ATTRIBUTE_NAME ) );
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericAttributeError getResponseData( Entry entry, HttpServletRequest request, List<Response> listResponse, Locale locale )
    {
        String strValueEntry = request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) ).trim( );
        if ( strValueEntry == null )
        {
            strValueEntry = "";
        }
        Response response = new Response( );
        response.setEntry( entry );
        response.setResponseValue( strValueEntry );
        if ( StringUtils.isNotBlank( response.getResponseValue( ) ) )
        {
            response.setToStringValueResponse( getResponseValueForRecap( entry, request, response, locale ) );
        }
        else
        {
            response.setToStringValueResponse( StringUtils.EMPTY );
        }

        response.setIterationNumber( getResponseIterationValue( request ) );
        listResponse.add( response );
        return null;
    }

    /**
     * Get a reference list with every lutece user attributes
     * 
     * @param strLangage
     *            the langage of admin user
     * @return The reference list with every user attributes
     */
    public ReferenceList getLuteceUserAttributesRefList( String strLangage )
    {
        ReferenceList referenceList = new ReferenceList( );
        List<IAttribute> listMyLuteceAttribute = AttributeHome.findAll( Locale.forLanguageTag( strLangage ), DirectoriesPlugin.getPlugin( ) );
        for ( IAttribute attribute : listMyLuteceAttribute )
        {
            ReferenceItem item = new ReferenceItem( );
            item.setName( attribute.getTitle( ) );
            item.setCode( String.valueOf( attribute.getIdAttribute( ) ) );
            referenceList.add( item );
        }
        _refListUserAttributes = referenceList;
        return _refListUserAttributes;
    }

    /**
     * Get a reference list with every lutece user attributes
     * 
     * @param strLangage
     *            the langage of admin user
     * @return The reference list with every user attributes
     */
    public ReferenceList getMyLuteceUserPickerList( String strIdEntryType, String strIdDirectory )
    {
        List<Entry> listEntry = EntryService.getEntryListFromType( Integer.valueOf( strIdEntryType ), Integer.valueOf( strIdDirectory ) );
        ReferenceList listEntryOpt = new ReferenceList( );
        for ( Entry entry : listEntry )
        {
            ReferenceItem entryOpt = new ReferenceItem( );
            entryOpt.setName( entry.getTitle( ) );
            entryOpt.setCode( String.valueOf( entry.getIdEntry( ) ) );
            listEntryOpt.add( entryOpt );
        }
        return listEntryOpt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForExport( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        return response.getResponseValue( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForRecap( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        return response.getResponseValue( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateEntryReadOnly( boolean bDisplayFront )
    {
        if ( bDisplayFront )
        {
            return TEMPLATE_READONLY_FRONTOFFICE;
        }
        return TEMPLATE_READONLY_BACKOFFICE;
    }
}
