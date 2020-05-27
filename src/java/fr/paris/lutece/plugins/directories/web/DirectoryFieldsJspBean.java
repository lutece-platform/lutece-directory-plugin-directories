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
package fr.paris.lutece.plugins.directories.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.directories.util.DirectoriesConstants;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.IEntryTypeService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;
import org.apache.commons.lang.StringUtils;

/**
 * JspBean to manage directories form fieldsl
 * 
 * @author
 *
 */
@Controller( controllerJsp = "ManageDirectoryFields.jsp", controllerPath = "jsp/admin/plugins/directories/", right = "DIRECTORIES_MANAGEMENT" )
public class DirectoryFieldsJspBean extends AbstractDirectoriesManagerJspBean
{
    private static final long serialVersionUID = -1505164256619633838L;
    // Urls
    private static final String JSP_URL_MANAGE_DIRECTORYITEM_FORM_FIELDS = "jsp/admin/plugins/directories/ManageDirectoryFields.jsp";
    // Views
    private static final String VIEW_GET_MODIFY_FIELD_WITH_CONDITIONAL_QUESTIONS = "getModifyFieldCC";
    // Parameters
    private static final String PARAMETER_ID_FIELD = "id_field";
    // templates
    private static final String TEMPLATE_CREATE_FIELD = "admin/plugins/directories/entries/create_field.html";
    private static final String TEMPLATE_MODIFY_FIELD = "admin/plugins/directories/entries/modify_field.html";
    // properties
    private static final String PROPERTY_CREATE_FIELD_TITLE = "directories.createField.title";
    private static final String PROPERTY_MODIFY_FIELD_TITLE = "directories.modifyField.title";
    // parameters form
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_VALUE = "value";
    private static final String PARAMETER_DEFAULT_VALUE = "default_value";
    private static final String PARAMETER_NO_DISPLAY_TITLE = "no_display_title";
    private static final String PARAMETER_COMMENT = "comment";
    // Views
    private static final String VIEW_MODIFY_FIELD = "modifyField";
    private static final String VIEW_CREATE_FIELD = "createField";
    private static final String VIEW_CONFIRM_REMOVE_FIELD = "confirmRemoveField";
    
    // Actions
    private static final String ACTION_CREATE_FIELD = "createField";
    private static final String ACTION_MODIFY_FIELD = "modifyField";
    private static final String ACTION_MOVE_FIELD_UP = "moveFieldUp";
    private static final String ACTION_MOVE_FIELD_DOWN = "moveFieldDown";
    private static final String ACTION_REMOVE_FIELD = "removeField";
    
    // other constants
    private static final String FIELD_TITLE_FIELD = "directories.createField.labelTitle";
    private static final String FIELD_VALUE_FIELD = "directories.createField.labelValue";
    // Messages
    private static final String MESSAGE_MANDATORY_FIELD = "portal.util.message.mandatoryField";
    private static final String MESSAGE_FIELD_VALUE_FIELD = "directories.message.error.field_value_field";
    private static final String MESSAGE_CONFIRM_REMOVE_FIELD = "directories.message.confirmRemoveField";


    /**
     * Get the URL to modify a field. The field is assumed to allow conditional questions.
     * 
     * @param request
     *            The request
     * @param nIdField
     *            The id of the field
     * @return The URL of the page to modify the field
     */
    public static String getUrlModifyField( HttpServletRequest request, int nIdField )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_DIRECTORYITEM_FORM_FIELDS );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_GET_MODIFY_FIELD_WITH_CONDITIONAL_QUESTIONS );
        urlItem.addParameter( PARAMETER_ID_FIELD, nIdField );
        return urlItem.getUrl( );
    }

    /**
     * Gets the field creation page
     * 
     * @param request
     *            The HTTP request
     * @return the field creation page
     */
    @View( value = VIEW_CREATE_FIELD )
    public String getCreateField( HttpServletRequest request )
    {
        String strIdEntry = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY );
        int nIdEntry = Integer.parseInt( strIdEntry );
        Entry entry = EntryHome.findByPrimaryKey( nIdEntry );
        Field field = new Field( );
        field.setParentEntry( entry );
        Map<String, Object> model = new HashMap<>( );
        model.put( DirectoriesConstants.MARK_ID_DIRECTORY, request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY ) );
        model.put( DirectoriesConstants.MARK_ENTRY, entry );
        model.put( DirectoriesConstants.MARK_FIELD, field );
        return getPage( PROPERTY_CREATE_FIELD_TITLE, TEMPLATE_CREATE_FIELD, model );
    }

    /**
     * Perform creation field
     * 
     * @param request
     *            The HTTP request
     * @return The URL to go after performing the action
     */
    @Action( ACTION_CREATE_FIELD )
    public String doCreateField( HttpServletRequest request )
    {
        int nIdEntry = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY ) );
        Entry entry = new Entry( );
        entry.setIdEntry( nIdEntry );
        Field field = new Field( );
        field.setParentEntry( entry );
        String strError = getFieldData( request, field );
        if ( strError != null )
        {
            return redirect( request, strError );
        }
        FieldHome.create( field );
        return redirect( request, DirectoryEntriesJspBean.getURLModifyEntry( request, nIdEntry ) );

    }

    /**
     * Get the page to modify a field without displaying its conditional questions
     * 
     * @param request
     *            The request
     * @return The HTML content to display, or the next URL to redirect to
     */
    @View( VIEW_MODIFY_FIELD )
    public String getModifyField( HttpServletRequest request )
    {
        int nIdField = Integer.parseInt( request.getParameter( PARAMETER_ID_FIELD ) );
        Field field = FieldHome.findByPrimaryKey( nIdField );
        Entry entry = EntryHome.findByPrimaryKey( field.getParentEntry( ).getIdEntry( ) );
        field.setParentEntry( entry );
        HashMap<String, Object> model = new HashMap<>( );
        model.put( DirectoriesConstants.MARK_ENTRY, entry );
        model.put( DirectoriesConstants.MARK_FIELD, field );
        return getPage( PROPERTY_MODIFY_FIELD_TITLE, TEMPLATE_MODIFY_FIELD, model );
    }

    /**
     * Perform modification field
     * 
     * @param request
     *            The HTTP request
     * @return The URL to go after performing the action
     */
    @Action( ACTION_MODIFY_FIELD )
    public String doModifyField( HttpServletRequest request )
    {
        String strIdField = request.getParameter( PARAMETER_ID_FIELD );
        int nIdEntry = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY ) );
        Field field = null;
        int nIdField = Integer.parseInt( strIdField );
        field = FieldHome.findByPrimaryKey( nIdField );
        String strError = getFieldData( request, field );
        if ( strError != null )
        {
            return strError;
        }
        FieldHome.update( field );
        return redirect( request, DirectoryEntriesJspBean.getURLModifyEntry( request, nIdEntry ) );
    }

    /**
     * Move a field up
     * 
     * @param request
     *            The request
     * @return The next URL to redirect to
     */
    @Action( ACTION_MOVE_FIELD_UP )
    public String doMoveFieldUp( HttpServletRequest request )
    {
        return doMoveField( request, true );
    }

    /**
     * Move a field down
     * 
     * @param request
     *            The request
     * @return The next URL to redirect to
     */
    @Action( ACTION_MOVE_FIELD_DOWN )
    public String doMoveFieldDown( HttpServletRequest request )
    {
        return doMoveField( request, false );
    }

    /**
     * Move a field up or down
     * 
     * @param request
     *            The request
     * @param bMoveUp
     *            True to move the field up, false to move it down
     * @return The next URL to redirect to
     */
    private String doMoveField( HttpServletRequest request, boolean bMoveUp )
    {
        int nIdEntry = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY ) );
        int nIdField = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ID_FIELD ) );
        Field field = FieldHome.findByPrimaryKey( nIdField );
        List<Field> listField = FieldHome.getFieldListByIdEntry( field.getParentEntry( ).getIdEntry( ) );
        int nIndexField = getIndexFieldInTheFieldList( nIdField, listField );

        if ( nIndexField != -1 && ( ( bMoveUp && nIndexField > 0 ) || ( !bMoveUp && nIndexField < ( listField.size( ) - 1 ) ) ) )
        {
            Field fieldToInversePosition = listField.get( bMoveUp ? ( nIndexField - 1 ) : ( nIndexField + 1 ) );
            int nNewPosition = fieldToInversePosition.getPosition( );
            fieldToInversePosition.setPosition( field.getPosition( ) );
            field.setPosition( nNewPosition );
            FieldHome.update( field );
            FieldHome.update( fieldToInversePosition );
        }
        return redirect( request, DirectoryEntriesJspBean.getURLModifyEntry( request, nIdEntry ) );
    }

    /**
     * Get the request data and if there is no error insert the data in the field specified in parameter. return null if there is no error or else return the
     * error page URL
     * 
     * @param request
     *            the request
     * @param field
     *            field
     * @return null if there is no error or else return the error page URL
     */
    private String getFieldData( HttpServletRequest request, Field field )
    {
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strValue = request.getParameter( PARAMETER_VALUE );
        String strDefaultValue = request.getParameter( PARAMETER_DEFAULT_VALUE );
        String strNoDisplayTitle = request.getParameter( PARAMETER_NO_DISPLAY_TITLE );
        String strComment = request.getParameter( PARAMETER_COMMENT );
        String strFieldError = null;
        if ( StringUtils.isEmpty( strTitle ) )
        {
            strFieldError = FIELD_TITLE_FIELD;
        }
        else
            if ( StringUtils.isEmpty( strValue ) )
            {
                strFieldError = FIELD_VALUE_FIELD;
            }
            else
                if ( !StringUtil.checkCodeKey( strValue ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_FIELD_VALUE_FIELD, AdminMessage.TYPE_STOP );
                }
        if ( strFieldError != null )
        {
            Object [ ] tabRequiredFields = {
                    I18nService.getLocalizedString( strFieldError, getLocale( ) )
            };
            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }
        field.setCode( IEntryTypeService.FIELD_ANSWER_CHOICE );
        field.setTitle( strTitle );
        field.setValue( strValue );
        field.setComment( strComment );
        field.setDefaultValue( strDefaultValue != null );
        field.setNoDisplayTitle( strNoDisplayTitle != null );
        // field.setParentEntry(EntryHome.findByPrimaryKey(nIdEntry));
        return null; // No error
    }

    /**
     * Return the index in the list of the field whose key is specified in parameter
     * 
     * @param nIdField
     *            the key of the field
     * @param listField
     *            the list of field
     * @return the index in the list of the field whose key is specified in parameter
     */
    public static int getIndexFieldInTheFieldList( int nIdField, List<Field> listField )
    {
        int nIndex = 0;

        for ( Field field : listField )
        {
            if ( field.getIdField( ) == nIdField )
            {
                return nIndex;
            }
            nIndex++;
        }

        return nIndex;
    }

    /**
     * Gets the confirmation page of delete field
     * 
     * @param request
     *            The HTTP request
     * @return the html code to confirm
     */
    @View( value = VIEW_CONFIRM_REMOVE_FIELD )
    public String getConfirmRemoveField( HttpServletRequest request )
    {
        String strIdField = request.getParameter( DirectoriesConstants.PARAMETER_ID_FIELD );
        String strIdEntry = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY );
        String strIdDirectory = request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_FIELD ) );
        url.addParameter( DirectoriesConstants.PARAMETER_ID_FIELD, strIdField );
        url.addParameter( DirectoriesConstants.PARAMETER_ID_ENTRY, strIdEntry );
        url.addParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY, strIdDirectory );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_FIELD, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Perform suppression field
     * 
     * @param request
     *            The HTTP request
     * @return The URL to go after performing the action
     */
    @Action( ACTION_REMOVE_FIELD )
    public String doRemoveField( HttpServletRequest request )
    {
        int nIdField = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ID_FIELD ) );
        int nIdEntry = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY ) );
        FieldHome.remove( nIdField );
        return redirect( request, DirectoryEntriesJspBean.getURLModifyEntry( request, nIdEntry ) );
    }

}
