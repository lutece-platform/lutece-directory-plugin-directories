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
package fr.paris.lutece.plugins.directories.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.directories.service.DirectoriesPlugin;
import fr.paris.lutece.plugins.directories.service.EntryService;
import fr.paris.lutece.plugins.directories.service.EntryTypeService;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.EntryFilter;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.EntryType;
import fr.paris.lutece.plugins.genericattributes.business.EntryTypeHome;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.business.ResponseFilter;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeServiceManager;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * JspBean to manage directories form entries
 * 
 * @author
 *
 */
@Controller( controllerJsp = "ManageDirectoryEntries.jsp", controllerPath = "jsp/admin/plugins/directories/", right = "DIRECTORIES_MANAGEMENT" )
public class DirectoryEntriesJspBean extends MVCAdminJspBean
{
    // Parameters
    private static final String PARAMETER_ID_ENTRY_TYPE = "id_type";
    private static final String PARAMETER_ID_DIRECTORYITEM_FORM = "id";
    private static final String PARAMETER_ID_FIELD = "id_field";
    private static final String PARAMETER_ID_ENTRY = "id_entry";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_APPLY = "apply";
    private static final String PARAMETER_ORDER_ID = "order_id_";
    private static final String PARAMETER_ADD_TO_GROUP = "add_to_group";
    private static final String PARAMETER_ID_ENTRY_GROUP = "id_entry_group";
    private static final String PARAMETER_ENTRY_ID_MOVE = "entry_id_move";
    // Urls
    private static final String JSP_URL_MANAGE_DIRECTORYITEM_FORM_ENTRIES = "jsp/admin/plugins/directories/ManageDirectoryEntries.jsp";
    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_ENTRY = "directories.message.confirmRemoveEntry";
    private static final String MESSAGE_CANT_REMOVE_ENTRY = "advert.message.cantRemoveEntry";
    private static final String MESSAGE_CANT_REMOVE_ENTRY_RESOURCES_ATTACHED = "directories.message.cantRemoveEntry.resourceAttached";
    private static final String PROPERTY_CREATE_ENTRY_TITLE = "directories.createEntry.titleQuestion";
    // Views
    private static final String VIEW_GET_CREATE_ENTRY = "getCreateEntry";
    private static final String VIEW_GET_MODIFY_ENTRY = "getModifyEntry";
    private static final String VIEW_CONFIRM_REMOVE_ENTRY = "confirmRemoveEntry";
    private static final String VIEW_MANAGE_DIRECTORYITEM_FORM_ENTRIES = "manageDirectoryEntries";
    private static final String VIEW_CREATE_DIRECTORYITEM_FORM_ENTRIES_RESPONSE = "createDirectoryResponse";

    // Actions
    private static final String ACTION_DO_CREATE_ENTRY = "doCreateEntry";
    private static final String ACTION_DO_REMOVE_ENTRY = "doRemoveEntry";
    private static final String ACTION_DO_CHANGE_ORDER_ENTRY = "doChangeOrderEntry";
    // Marks
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_ENTRY = "entry";
    private static final String MARK_LOCALE = "locale";

    private static final String MARK_ENTRY_TYPE_SERVICE = "entryTypeService";
    private static final String TEMPLATE_MANAGE_DIRECTORYITEM_FORM = "/admin/plugins/directories/manage_directory_entries.html";
    private static final String TEMPLATE_CREATE_DIRECTORYITEM_FORM_ENTRIES_RESPONSE = "/admin/plugins/directories/create_directory_response.html";
    // Local variables
    private EntryService _entryService = EntryService.getService( );
    private static final String TEMPLATE_HTML_CODE_FORM_ADMIN = "admin/plugins/directories/html_code_form.html";
    // Markers
    private static final String MARK_FORM_HTML = "form_html";
    private static final String MARK_STR_ENTRY = "str_entry";

    /**
     * Get the HTML code to create an entry
     * 
     * @param request
     *            The request
     * @return The HTML code to display or the next URL to redirect to
     */
    @View( value = VIEW_MANAGE_DIRECTORYITEM_FORM_ENTRIES )
    public String getManageDirectoryEntries( HttpServletRequest request )
    {
        String strIdForm = request.getParameter( PARAMETER_ID_DIRECTORYITEM_FORM );
        int nIdForm = Integer.parseInt( strIdForm );
        Map<String, Object> model = getModel( );
        model.put( PARAMETER_ID_DIRECTORYITEM_FORM, nIdForm );
        EntryService.addListEntryToModel( model, nIdForm );
        return getPage( "Formulaire du modèle", TEMPLATE_MANAGE_DIRECTORYITEM_FORM, model );
    }

    /**
     * Returns the form to create an directories
     * 
     * @param request
     *            The HTTP request
     * @return the HTML code of the directories form
     * @throws AccessDeniedException
     *             If the user is not authorized to access this feature
     */
    @SuppressWarnings( "unchecked" )
    @View( VIEW_CREATE_DIRECTORYITEM_FORM_ENTRIES_RESPONSE )
    public synchronized String getViewCreateDirectories( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdForm = request.getParameter( PARAMETER_ID_DIRECTORYITEM_FORM );
        int nIdForm = Integer.parseInt( strIdForm );
        Map<String, Object> model = getModel( );
        List<Entry> listEntryFirstLevel = EntryService.getFilter( nIdForm, true );
        StringBuffer strBuffer = new StringBuffer( );
        for ( Entry entry : listEntryFirstLevel )
        {
            EntryService.getHtmlEntry( model, entry.getIdEntry( ), strBuffer, getLocale( ), false, request );
        }
        model.put( MARK_STR_ENTRY, strBuffer.toString( ) );
        HtmlTemplate templateForm = AppTemplateService.getTemplate( TEMPLATE_HTML_CODE_FORM_ADMIN, getLocale( ), model );
        model.put( MARK_FORM_HTML, templateForm.getHtml( ) );
        return getPage( "création d'une réponse", TEMPLATE_CREATE_DIRECTORYITEM_FORM_ENTRIES_RESPONSE, model );

    }

    /**
     * Get the HTML code to create an entry
     * 
     * @param request
     *            The request
     * @return The HTML code to display or the next URL to redirect to
     */
    @View( value = VIEW_GET_CREATE_ENTRY )
    public String getCreateEntry( HttpServletRequest request )
    {
        String strIdForm = request.getParameter( PARAMETER_ID_DIRECTORYITEM_FORM );
        String strIdType = request.getParameter( PARAMETER_ID_ENTRY_TYPE );
        int nIdForm = Integer.parseInt( strIdForm );
        int nIdType = Integer.parseInt( strIdType );
        Entry entry = new Entry( );
        entry.setEntryType( EntryTypeHome.findByPrimaryKey( nIdType ) );
        String strIdField = request.getParameter( PARAMETER_ID_FIELD );
        int nIdField = -1;
        if ( StringUtils.isNotEmpty( strIdField ) && StringUtils.isNumeric( strIdField ) )
        {
            nIdField = Integer.parseInt( strIdField );
            Field field = new Field( );
            field.setIdField( nIdField );
            entry.setFieldDepend( field );
        }
        entry.setIdResource( nIdForm );
        entry.setResourceType( DirectoriesPlugin.RESOURCE_TYPE );
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_ENTRY, entry );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage( ) );
        model.put( MARK_ENTRY_TYPE_SERVICE, EntryTypeServiceManager.getEntryTypeService( entry ) );
        String strTemplate = EntryTypeServiceManager.getEntryTypeService( entry ).getTemplateCreate( entry, false );
        if ( strTemplate == null )
        {
            return doCreateEntry( request );
        }
        model.put( "id", nIdForm );
        return getPage( PROPERTY_CREATE_ENTRY_TITLE, strTemplate, model );
    }

    /**
     * Do create an entry
     * 
     * @param request
     *            the request
     * @return The HTML code to display or the next URL to redirect to
     */
    @Action( ACTION_DO_CREATE_ENTRY )
    public String doCreateEntry( HttpServletRequest request )
    {
        String strIdForm = request.getParameter( PARAMETER_ID_DIRECTORYITEM_FORM );
        String strIdType = request.getParameter( PARAMETER_ID_ENTRY_TYPE );
        int nIdForm = Integer.parseInt( strIdForm );
        Field fieldDepend = null;
        if ( ( request.getParameter( PARAMETER_CANCEL ) == null ) && StringUtils.isNotEmpty( strIdType ) && StringUtils.isNumeric( strIdType ) )
        {
            int nIdType = Integer.parseInt( strIdType );
            EntryType entryType = new EntryType( );
            entryType.setIdType( nIdType );
            Entry entry = new Entry( );
            entry.setEntryType( EntryTypeService.getInstance( ).getEntryType( nIdType ) );
            String strIdField = request.getParameter( PARAMETER_ID_FIELD );
            int nIdField = -1;
            if ( StringUtils.isNotEmpty( strIdField ) && StringUtils.isNumeric( strIdField ) )
            {
                nIdField = Integer.parseInt( strIdField );
                fieldDepend = new Field( );
                fieldDepend.setIdField( nIdField );
                entry.setFieldDepend( fieldDepend );
            }
            String strError = EntryTypeServiceManager.getEntryTypeService( entry ).getRequestData( entry, request, getLocale( ) );
            if ( strError != null )
            {
                return redirect( request, strError );
            }
            entry.setIdResource( nIdForm );
            entry.setResourceType( DirectoriesPlugin.RESOURCE_TYPE );
            entry.setIdEntry( EntryHome.create( entry ) );
            if ( entry.getFields( ) != null )
            {
                for ( Field field : entry.getFields( ) )
                {
                    field.setParentEntry( entry );
                    FieldHome.create( field );
                }
            }
            if ( request.getParameter( PARAMETER_APPLY ) != null )
            {
                return redirect( request, VIEW_GET_MODIFY_ENTRY, PARAMETER_ID_ENTRY, entry.getIdEntry( ) );
            }
        }
        if ( fieldDepend != null )
        {
            return redirect( request, DirectoryFieldsJspBean.getUrlModifyField( request, fieldDepend.getIdField( ) ) );
        }
        return redirect( request, getURLModify( request, strIdForm ) );
    }

    /**
     * Get the URL to modify an entry
     * 
     * @param request
     *            The request
     * @param nIdEntry
     *            The id of the entry
     * @return The URL to modify the given entry
     */
    public static String getURLModifyEntry( HttpServletRequest request, int nIdEntry )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_DIRECTORYITEM_FORM_ENTRIES );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_GET_MODIFY_ENTRY );
        urlItem.addParameter( PARAMETER_ID_ENTRY, nIdEntry );
        return urlItem.getUrl( );
    }

    /**
     * Get the URL to modify an directories form
     * 
     * @param request
     *            The request
     * @param nIdForm
     *            The id of the form to modify
     * @return The URL to modify the given Directories form
     */
    public static String getURLModify( HttpServletRequest request, int nIdForm )
    {
        return getURLModify( request, Integer.toString( nIdForm ) );
    }

    /**
     * Get the URL to modify an directories form
     * 
     * @param request
     *            The request
     * @param strIdForm
     *            The id of the form to modify
     * @return The URL to modify the given Directories form
     */
    public static String getURLModify( HttpServletRequest request, String strIdForm )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_DIRECTORYITEM_FORM_ENTRIES );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_MANAGE_DIRECTORYITEM_FORM_ENTRIES );
        urlItem.addParameter( PARAMETER_ID_DIRECTORYITEM_FORM, strIdForm );
        return urlItem.getUrl( );
    }

    /**
     * Gets the confirmation page of delete entry
     * 
     * @param request
     *            The HTTP request
     * @return the confirmation page of delete entry
     */
    @View( VIEW_CONFIRM_REMOVE_ENTRY )
    public String getConfirmRemoveEntry( HttpServletRequest request )
    {
        String strIdEntry = request.getParameter( PARAMETER_ID_ENTRY );
        int nIdEntry = Integer.parseInt( strIdEntry );
        ResponseFilter responsefilter = new ResponseFilter( );
        responsefilter.setIdEntry( nIdEntry );
        if ( !ResponseHome.getResponseList( responsefilter ).isEmpty( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CANT_REMOVE_ENTRY_RESOURCES_ATTACHED, AdminMessage.TYPE_STOP ) );
        }
        UrlItem url = new UrlItem( getActionUrl( ACTION_DO_REMOVE_ENTRY ) );
        url.addParameter( PARAMETER_ID_ENTRY, strIdEntry );
        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ENTRY, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Perform the entry removal
     * 
     * @param request
     *            The HTTP request
     * @return The URL to go after performing the action
     */
    @Action( ACTION_DO_REMOVE_ENTRY )
    public String doRemoveEntry( HttpServletRequest request )
    {
        String strIdEntry = request.getParameter( PARAMETER_ID_ENTRY );
        int nIdEntry = Integer.parseInt( strIdEntry );
        Entry entry = EntryHome.findByPrimaryKey( nIdEntry );
        List<String> listErrors = new ArrayList<String>( );
        if ( !_entryService.checkForRemoval( strIdEntry, listErrors, getLocale( ) ) )
        {
            String strCause = AdminMessageService.getFormattedList( listErrors, getLocale( ) );
            Object [ ] args = {
                strCause
            };
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANT_REMOVE_ENTRY, args, AdminMessage.TYPE_STOP );
        }
        // Update order
        List<Entry> listEntry;
        EntryFilter filter = new EntryFilter( );
        filter.setIdResource( entry.getIdResource( ) );
        filter.setResourceType( DirectoriesPlugin.PLUGIN_NAME );
        listEntry = EntryHome.getEntryList( filter );
        if ( entry.getFieldDepend( ) == null )
        {
            _entryService.moveDownEntryOrder( listEntry.size( ), entry );
        }
        else
        {
            // conditional questions
            EntryHome.decrementOrderByOne( entry.getPosition( ), entry.getFieldDepend( ).getIdField( ), entry.getIdResource( ), entry.getResourceType( ) );
        }
        EntryHome.remove( nIdEntry );

        return redirect( request, getURLModify( request, entry.getIdResource( ) ) );
    }

    /**
     * Change the attribute's order (move up or move down in the list)
     * 
     * @param request
     *            the request
     * @return The URL of the form management page
     */
    @Action( ACTION_DO_CHANGE_ORDER_ENTRY )
    public String doChangeOrderEntry( HttpServletRequest request )
    {
        // gets the entry which needs to be changed (order)
        String strIdForm = request.getParameter( PARAMETER_ID_DIRECTORYITEM_FORM );
        int nIdForm = Integer.parseInt( strIdForm );
        // If the parameter move.x has been set, then we have to add entries to
        // a group
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_ADD_TO_GROUP ) ) )
        {
            String strIdEntryGroup = request.getParameter( PARAMETER_ID_ENTRY_GROUP );
            if ( StringUtils.isNotEmpty( strIdEntryGroup ) && StringUtils.isNumeric( strIdEntryGroup ) )
            {
                int nIdEntryGroup = Integer.parseInt( strIdEntryGroup );
                Entry entryParent = EntryHome.findByPrimaryKey( nIdEntryGroup );
                String [ ] strArrayIdEntries = request.getParameterValues( PARAMETER_ENTRY_ID_MOVE );
                if ( ( strArrayIdEntries != null ) && ( strArrayIdEntries.length > 0 ) )
                {
                    for ( String strIdEntry : strArrayIdEntries )
                    {
                        if ( StringUtils.isNotEmpty( strIdEntry ) && StringUtils.isNumeric( strIdEntry ) )
                        {
                            int nIdEntry = Integer.parseInt( strIdEntry );
                            _entryService.moveEntryIntoGroup( EntryHome.findByPrimaryKey( nIdEntry ), entryParent );
                        }
                    }
                }
            }
        }
        else
        {
            Integer nEntryId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTRY ) );
            Integer nOrderToSet = Integer.parseInt( request.getParameter( PARAMETER_ORDER_ID + request.getParameter( PARAMETER_ID_ENTRY ) ) );
            Entry entryToChangeOrder = EntryHome.findByPrimaryKey( nEntryId );
            int nActualOrder = entryToChangeOrder.getPosition( );
            // does nothing if the order to set is equal to the actual order
            if ( nOrderToSet != nActualOrder )
            {
                // entry goes up in the list
                if ( nOrderToSet < entryToChangeOrder.getPosition( ) )
                {
                    _entryService.moveUpEntryOrder( nOrderToSet, entryToChangeOrder );
                }
                // entry goes down in the list
                else
                {
                    _entryService.moveDownEntryOrder( nOrderToSet, entryToChangeOrder );
                }
            }
        }
        return redirect( request, getURLModify( request, nIdForm ) );
    }
}
