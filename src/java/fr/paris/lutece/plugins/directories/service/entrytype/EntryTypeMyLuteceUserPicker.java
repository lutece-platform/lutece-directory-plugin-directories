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
package fr.paris.lutece.plugins.directories.service.entrytype;

import fr.paris.lutece.plugins.directories.util.DirectoriesConstants;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.AbstractEntryTypeText;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.GenericAttributeError;
import fr.paris.lutece.plugins.genericattributes.business.MandatoryError;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.util.GenericAttributesUtils;
import fr.paris.lutece.portal.business.regularexpression.RegularExpression;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.regularexpression.RegularExpressionService;
import fr.paris.lutece.util.string.StringUtil;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * class EntryTypeText
 * 
 * @author
 * 
 */
public class EntryTypeMyLuteceUserPicker extends AbstractEntryTypeText
{
    private static final String TEMPLATE_MODIFY = "admin/plugins/directories/entries/modify_entry_type_mylutece_user_picker.html";
    private static final String TEMPLATE_HTML_CODE_ADMIN = "admin/plugins/directories/entries/fill_entry_type_mylutece_user_picker.html";
    private static final String TEMPLATE_HTML_CODE = "skin/plugins/directories/entries/fill_entry_type_mylutece_user_picker.html";
    private static final String TEMPLATE_CREATE = "admin/plugins/directories/entries/create_entry_type_mylutece_user_picker.html";
    private static final String TEMPLATE_READONLY_FRONTOFFICE = "skin/plugins/directories/entries/readonly_entry_type_mylutece_user_picker.html";
    private static final String TEMPLATE_READONLY_BACKOFFICE = "admin/plugins/directories/entries/readonly_entry_type_mylutece_user_picker.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateHtmlForm( Entry entry, boolean bDisplayFront )
    {
        return bDisplayFront ? TEMPLATE_HTML_CODE : TEMPLATE_HTML_CODE_ADMIN;
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
    public String getTemplateCreate( Entry entry, boolean bDisplayFront )
    {
        return TEMPLATE_CREATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestData( Entry entry, HttpServletRequest request, Locale locale )
    {
        initCommonRequestData( entry, request );
        String strCode = request.getParameter( PARAMETER_ENTRY_CODE );
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strHelpMessage = ( request.getParameter( PARAMETER_HELP_MESSAGE ) != null ) ? request.getParameter( PARAMETER_HELP_MESSAGE ).trim( ) : null;
        String strComment = request.getParameter( PARAMETER_COMMENT );
        String strValue = request.getParameter( PARAMETER_VALUE );
        String strMandatory = request.getParameter( PARAMETER_MANDATORY );
        String strWidth = request.getParameter( PARAMETER_WIDTH );
        String strMaxSizeEnter = request.getParameter( PARAMETER_MAX_SIZE_ENTER );
        String strConfirmField = request.getParameter( PARAMETER_CONFIRM_FIELD );
        String strConfirmFieldTitle = request.getParameter( PARAMETER_CONFIRM_FIELD_TITLE );
        String strUnique = request.getParameter( PARAMETER_UNIQUE );
        String strCSSClass = request.getParameter( PARAMETER_CSS_CLASS );
        String strOnlyDisplayInBack = request.getParameter( PARAMETER_ONLY_DISPLAY_IN_BACK );
        String strEditableBack = request.getParameter( PARAMETER_EDITABLE_BACK );
        String strErrorMessage = request.getParameter( PARAMETER_ERROR_MESSAGE );
        String strIndexed = request.getParameter( PARAMETER_INDEXED );

        int nWidth = -1;
        int nMaxSizeEnter = -1;

        String strFieldError = StringUtils.EMPTY;

        if ( StringUtils.isBlank( strTitle ) )
        {
            strFieldError = ERROR_FIELD_TITLE;
        }

        else
            if ( StringUtils.isBlank( strWidth ) )
            {
                strFieldError = ERROR_FIELD_WIDTH;
            }

        if ( ( strConfirmField != null ) && StringUtils.isBlank( strConfirmFieldTitle ) )
        {
            strFieldError = FIELD_CONFIRM_FIELD_TITLE;
        }

        if ( StringUtils.isNotBlank( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                    I18nService.getLocalizedString( strFieldError, locale )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        try
        {
            nWidth = Integer.parseInt( strWidth );
        }
        catch( NumberFormatException ne )
        {
            strFieldError = ERROR_FIELD_WIDTH;
        }

        try
        {
            if ( StringUtils.isNotBlank( strMaxSizeEnter ) )
            {
                nMaxSizeEnter = Integer.parseInt( strMaxSizeEnter );
            }
        }
        catch( NumberFormatException ne )
        {
            strFieldError = FIELD_MAX_SIZE_ENTER;
        }

        if ( StringUtils.isNotBlank( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                    I18nService.getLocalizedString( strFieldError, locale )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_NUMERIC_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        entry.setTitle( strTitle );
        entry.setHelpMessage( strHelpMessage );
        entry.setComment( strComment );
        entry.setCSSClass( strCSSClass );
        entry.setIndexed( strIndexed != null );
        entry.setErrorMessage( strErrorMessage );
        entry.setCode( strCode );

        GenericAttributesUtils.createOrUpdateField( entry, FIELD_TEXT_CONF, null, strValue );
        GenericAttributesUtils.createOrUpdateField( entry, FIELD_WIDTH, null, String.valueOf( nWidth ) );
        GenericAttributesUtils.createOrUpdateField( entry, FIELD_MAX_SIZE, null, String.valueOf( nMaxSizeEnter ) );
        GenericAttributesUtils.createOrUpdateField( entry, DirectoriesConstants.FIELD_FILTER, null,
                request.getParameter( DirectoriesConstants.PARAMETER_FILTER ) );

        entry.setMandatory( strMandatory != null );
        entry.setOnlyDisplayInBack( strOnlyDisplayInBack != null );
        entry.setEditableBack( strEditableBack != null );

        boolean confirm = false;
        String fieldTitle = null;
        if ( strConfirmField != null )
        {
            confirm = true;
            fieldTitle = strConfirmFieldTitle;
        }

        GenericAttributesUtils.createOrUpdateField( entry, FIELD_CONFIRM, fieldTitle, String.valueOf( confirm ) );
        entry.setUnique( strUnique != null );
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericAttributeError getResponseData( Entry entry, HttpServletRequest request, List<Response> listResponse, Locale locale )
    {
        String strValueEntry = request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) ).trim( );
        Field confirmField = entry.getFieldByCode( FIELD_CONFIRM );

        boolean bConfirmField = confirmField != null && Boolean.valueOf( confirmField.getValue( ) );

        String strValueEntryConfirmField = null;

        if ( bConfirmField )
        {
            strValueEntryConfirmField = request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) + SUFFIX_CONFIRM_FIELD ).trim( );
        }

        List<RegularExpression> listRegularExpression = entry.getFieldByCode( FIELD_TEXT_CONF ).getRegularExpressionList( );
        Response response = new Response( );
        response.setEntry( entry );

        if ( strValueEntry == null )
        {
            return null;
        }

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

        String strFilter = entry.getFieldByCode( DirectoriesConstants.FIELD_FILTER ).getValue( );
        response = new Response( );
        response.setEntry( entry );
        response.setResponseValue( strFilter );
        response.setIterationNumber( getResponseIterationValue( request ) );
        listResponse.add( response );

        return checkEntryErrors( entry, confirmField, strValueEntry, strValueEntryConfirmField, listRegularExpression, bConfirmField, locale );
    }

    /**
     * {@inheritDoc}
     */
    private GenericAttributeError checkEntryErrors( Entry entry, Field confirmField, String strValueEntry, String strValueEntryConfirmField,
            List<RegularExpression> listRegularExpression, boolean bConfirmField, Locale locale )
    {
        // Checks if the entry value contains XSS characters
        if ( StringUtil.containsXssCharacters( strValueEntry ) )
        {
            GenericAttributeError error = new GenericAttributeError( );
            error.setMandatoryError( false );
            error.setTitleQuestion( entry.getTitle( ) );
            error.setErrorMessage( I18nService.getLocalizedString( MESSAGE_XSS_FIELD, locale ) );

            return error;
        }

        if ( entry.isMandatory( ) && StringUtils.isBlank( strValueEntry ) )
        {
            if ( StringUtils.isNotEmpty( entry.getErrorMessage( ) ) )
            {
                GenericAttributeError error = new GenericAttributeError( );
                error.setMandatoryError( true );
                error.setErrorMessage( entry.getErrorMessage( ) );

                return error;
            }

            return new MandatoryError( entry, locale );
        }

        if ( ( !strValueEntry.equals( StringUtils.EMPTY ) ) && CollectionUtils.isNotEmpty( listRegularExpression )
                && RegularExpressionService.getInstance( ).isAvailable( ) )
        {
            for ( RegularExpression regularExpression : listRegularExpression )
            {
                if ( !RegularExpressionService.getInstance( ).isMatches( strValueEntry, regularExpression ) )
                {
                    GenericAttributeError error = new GenericAttributeError( );
                    error.setMandatoryError( false );
                    error.setTitleQuestion( entry.getTitle( ) );
                    error.setErrorMessage( regularExpression.getErrorMessage( ) );

                    return error;
                }
            }
        }

        if ( bConfirmField && ( ( strValueEntryConfirmField == null ) || !strValueEntry.equals( strValueEntryConfirmField ) ) )
        {
            GenericAttributeError error = new GenericAttributeError( );
            error.setMandatoryError( false );
            error.setTitleQuestion( confirmField.getTitle( ) );
            error.setErrorMessage( I18nService.getLocalizedString( MESSAGE_CONFIRM_FIELD, new String [ ] {
                    entry.getTitle( )
            }, locale ) );

            return error;
        }
        return null;
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
