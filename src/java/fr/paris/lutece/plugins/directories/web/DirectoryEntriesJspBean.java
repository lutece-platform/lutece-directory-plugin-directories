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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import fr.paris.lutece.plugins.directories.service.DirectoriesPlugin;
import fr.paris.lutece.plugins.directories.service.EntryService;
import fr.paris.lutece.plugins.directories.service.EntryTypeService;
import fr.paris.lutece.plugins.directories.util.DirectoriesConstants;
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
import fr.paris.lutece.plugins.genericattributes.service.entrytype.IEntryTypeService;
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
 * JspBean to manage directory entries model entries
 * 
 * @author
 *
 */
@Controller( controllerJsp = "ManageDirectoryEntries.jsp", controllerPath = "jsp/admin/plugins/directories/", right = "DIRECTORIES_MANAGEMENT" )
public class DirectoryEntriesJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = -6682482064278399335L;
    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_ENTRY = "directories.message.confirmRemoveEntry";
    private static final String MESSAGE_CANT_REMOVE_ENTRY = "advert.message.cantRemoveEntry";
    private static final String MESSAGE_CANT_REMOVE_ENTRY_RESOURCES_ATTACHED = "directories.message.cantRemoveEntry.resourceAttached";
    private static final String PROPERTY_CREATE_ENTRY_TITLE = "directories.createEntry.titleQuestion";
    // Views
    private static final String VIEW_GET_CREATE_ENTRY = "getCreateEntry";
    private static final String VIEW_GET_MODIFY_ENTRY = "getModifyEntry";
    private static final String VIEW_CONFIRM_REMOVE_ENTRY = "confirmRemoveEntry";
    private static final String VIEW_MANAGE_DIRECTORY_ENTRIES = "manageDirectoryEntries";
    private static final String VIEW_CREATE_DIRECTORY_ENTRIES_RESPONSE = "createDirectoryResponse";
    // Actions
    private static final String ACTION_DO_CREATE_ENTRY = "doCreateEntry";
    private static final String ACTION_DO_MODIFY_ENTRY = "doModifyEntry";
    private static final String ACTION_DO_REMOVE_ENTRY = "doRemoveEntry";
    private static final String ACTION_DO_CHANGE_ORDER_ENTRY = "doChangeOrderEntry";
    // Templates
    private static final String TEMPLATE_MANAGE_DIRECTORY_ = "/admin/plugins/directories/manage_directory_entries.html";
    private static final String TEMPLATE_CREATE_DIRECTORY_ENTRIES_RESPONSE = "/admin/plugins/directories/create_directory_response.html";
    private static final String TEMPLATE_HTML_CODE_FORM_ADMIN = "admin/plugins/directories/html_code_form.html";
    // Local variables
    private EntryService _entryService = EntryService.getService( );

    /**
     * Get the HTML code to create an entry
     * 
     * @param request
     *            The request
     * @return The HTML code to display or the next URL to redirect to
     */
    @View( value = VIEW_MANAGE_DIRECTORY_ENTRIES )
    public String getManageDirectoryEntries( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        Map<String, Object> model = getModel( );
        model.put( DirectoriesConstants.PARAMETER_ID_DIRECTORY, nIdDirectory );
        EntryService.addListEntryToModel( model, nIdDirectory );
        return getPage( "Formulaire du modèle", TEMPLATE_MANAGE_DIRECTORY_, model );
    }

    /**
     * Returns the form to create a directory
     * 
     * @param request
     *            The HTTP request
     * @return the HTML code of the directory entries model
     * @throws AccessDeniedException
     *             If the user is not authorized to access this feature
     */
    @View( VIEW_CREATE_DIRECTORY_ENTRIES_RESPONSE )
    public synchronized String getViewCreateDirectory( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdDirectory = request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        Map<String, Object> model = getModel( );
        List<Entry> listEntryFirstLevel = EntryService.getDirectoryEntryList( nIdDirectory, true );
        StringBuffer strBuffer = new StringBuffer( );
        for ( Entry entry : listEntryFirstLevel )
        {
            EntryService.getHtmlEntry( model, entry.getIdEntry( ), strBuffer, getLocale( ), false, request );
        }
        model.put( DirectoriesConstants.MARK_STR_ENTRY, strBuffer.toString( ) );
        HtmlTemplate templateForm = AppTemplateService.getTemplate( TEMPLATE_HTML_CODE_FORM_ADMIN, getLocale( ), model );
        model.put( DirectoriesConstants.MARK_FORM_HTML, templateForm.getHtml( ) );
        return getPage( "création d'une réponse", TEMPLATE_CREATE_DIRECTORY_ENTRIES_RESPONSE, model );
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
        String strIdDirectory = request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY );
        String strIdType = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY_TYPE );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        int nIdType = Integer.parseInt( strIdType );
        Entry entry = new Entry( );
        entry.setEntryType( EntryTypeHome.findByPrimaryKey( nIdType ) );
        String strIdField = request.getParameter( DirectoriesConstants.PARAMETER_ID_FIELD );
        int nIdField = -1;
        if ( StringUtils.isNotEmpty( strIdField ) && StringUtils.isNumeric( strIdField ) )
        {
            nIdField = Integer.parseInt( strIdField );
            Field field = new Field( );
            field.setIdField( nIdField );
            entry.setFieldDepend( field );
        }
        entry.setIdResource( nIdDirectory );
        entry.setResourceType( DirectoriesPlugin.RESOURCE_TYPE );
        Map<String, Object> model = new HashMap<>( );
        model.put( DirectoriesConstants.MARK_ENTRY, entry );
        model.put( DirectoriesConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( DirectoriesConstants.MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage( ) );
        model.put( DirectoriesConstants.MARK_ENTRY_TYPE_SERVICE, EntryTypeServiceManager.getEntryTypeService( entry ) );
        String strTemplate = EntryTypeServiceManager.getEntryTypeService( entry ).getTemplateCreate( entry, false );
        if ( strTemplate == null )
        {
            return doCreateEntry( request );
        }
        model.put( DirectoriesConstants.PARAMETER_ID_DIRECTORY, nIdDirectory );
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
        String strIdDirectory = request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY );
        String strIdType = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY_TYPE );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        Field fieldDepend = null;
        if ( ( request.getParameter( DirectoriesConstants.PARAMETER_CANCEL ) == null ) && StringUtils.isNotEmpty( strIdType )
                && StringUtils.isNumeric( strIdType ) )
        {
            int nIdType = Integer.parseInt( strIdType );
            EntryType entryType = new EntryType( );
            entryType.setIdType( nIdType );
            Entry entry = new Entry( );
            entry.setEntryType( EntryTypeService.getInstance( ).getEntryType( nIdType ) );
            String strIdField = request.getParameter( DirectoriesConstants.PARAMETER_ID_FIELD );
            int nIdField = -1;
            if ( StringUtils.isNotEmpty( strIdField ) && StringUtils.isNumeric( strIdField ) )
            {
                nIdField = Integer.parseInt( strIdField );
                fieldDepend = new Field( );
                fieldDepend.setIdField( nIdField );
                entry.setFieldDepend( fieldDepend );
            }
            String strError = EntryService.doCreateEntry( entry, request );
            if ( strError != null )
            {
                return redirect( request, strError );
            }
            entry.setIdResource( nIdDirectory );
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
            if ( request.getParameter( DirectoriesConstants.PARAMETER_APPLY ) != null )
            {
                return redirect( request, VIEW_GET_MODIFY_ENTRY, DirectoriesConstants.PARAMETER_ID_ENTRY, entry.getIdEntry( ) );
            }
        }
        if ( fieldDepend != null )
        {
            return redirect( request, DirectoryFieldsJspBean.getUrlModifyField( request, fieldDepend.getIdField( ) ) );
        }
        return redirect( request, getURLModify( request, strIdDirectory ) );
    }

    /**
     * Gets the entry modification page
     * 
     * @param request
     *            The HTTP request
     * @return The entry modification page
     */
    @View( VIEW_GET_MODIFY_ENTRY )
    public String getModifyEntry( HttpServletRequest request )
    {
        String strIdEntry = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY );
        int nIdEntry = Integer.parseInt( strIdEntry );
        Entry entry = EntryHome.findByPrimaryKey( nIdEntry );
        int nIdDirectory = entry.getIdResource( );
        IEntryTypeService entryTypeService = EntryTypeServiceManager.getEntryTypeService( entry );
        Map<String, Object> model = new HashMap<String, Object>( );
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + getViewUrl( VIEW_GET_MODIFY_ENTRY ) );
        urlItem.addParameter( DirectoriesConstants.PARAMETER_ID_ENTRY, strIdEntry );
        model.put( DirectoriesConstants.MARK_ENTRY, entry );
        model.put( DirectoriesConstants.MARK_LIST, entry.getFields( ) );
        model.put( DirectoriesConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( DirectoriesConstants.MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage( ) );
        model.put( DirectoriesConstants.MARK_ENTRY_TYPE_SERVICE, EntryTypeServiceManager.getEntryTypeService( entry ) );
        model.put( DirectoriesConstants.PARAMETER_ID_DIRECTORY, nIdDirectory );
        return getPage( "modify", entryTypeService.getTemplateModify( entry, false ), model );
    }

    /**
     * Perform the entry modification
     * 
     * @param request
     *            The HTTP request
     * @return The URL to go after performing the action
     */
    @Action( ACTION_DO_MODIFY_ENTRY )
    public String doModifyEntry( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY );
        String strIdEntry = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY );
        if ( StringUtils.isNotEmpty( strIdEntry ) && StringUtils.isNumeric( strIdEntry ) )
        {
            int nIdEntry = Integer.parseInt( strIdEntry );
            Entry entry = EntryHome.findByPrimaryKey( nIdEntry );
            if ( request.getParameter( DirectoriesConstants.PARAMETER_CANCEL ) == null )
            {
                String strError = EntryService.doModifyEntry( entry, request );
                if ( strError != null )
                {
                    return redirect( request, strError );
                }
                EntryHome.update( entry );
                if ( entry.getFields( ) != null )
                {
                    for ( Field field : entry.getFields( ) )
                    {
                        Field fieldStored = FieldHome.findByPrimaryKey( field.getIdField( ) );
                        if ( fieldStored != null )
                        {
                            FieldHome.update( field );
                        }
                        else
                        {
                            FieldHome.create( field );
                        }
                    }
                }
            }
            if ( request.getParameter( DirectoriesConstants.PARAMETER_APPLY ) != null )
            {
                return redirect( request, VIEW_GET_MODIFY_ENTRY, DirectoriesConstants.PARAMETER_ID_ENTRY, nIdEntry );
            }
        }
        return redirect( request, getURLModify( request, strIdDirectory ) );
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
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + DirectoriesConstants.JSP_MANAGE_DIRECTORY_ENTRIES );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_GET_MODIFY_ENTRY );
        urlItem.addParameter( DirectoriesConstants.PARAMETER_ID_ENTRY, nIdEntry );
        return urlItem.getUrl( );
    }

    /**
     * Get the URL to modify a directory entries model
     * 
     * @param request
     *            The request
     * @param nIdDirectory
     *            The id of the directory
     * @return The URL to modify the given directory entries model
     */
    public static String getURLModify( HttpServletRequest request, int nIdDirectory )
    {
        return getURLModify( request, Integer.toString( nIdDirectory ) );
    }

    /**
     * Get the URL to modify a directory entries model
     * 
     * @param request
     *            The request
     * @param strIdDirectory
     *            The id of the directory
     * @return The URL to modify the given directory entries model
     */
    public static String getURLModify( HttpServletRequest request, String strIdDirectory )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + DirectoriesConstants.JSP_MANAGE_DIRECTORY_ENTRIES );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_MANAGE_DIRECTORY_ENTRIES );
        urlItem.addParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY, strIdDirectory );
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
        String strIdEntry = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY );
        int nIdEntry = Integer.parseInt( strIdEntry );
        ResponseFilter responsefilter = new ResponseFilter( );
        responsefilter.setIdEntry( nIdEntry );
        if ( !ResponseHome.getResponseList( responsefilter ).isEmpty( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CANT_REMOVE_ENTRY_RESOURCES_ATTACHED, AdminMessage.TYPE_STOP ) );
        }
        UrlItem url = new UrlItem( getActionUrl( ACTION_DO_REMOVE_ENTRY ) );
        url.addParameter( DirectoriesConstants.PARAMETER_ID_ENTRY, strIdEntry );
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
        String strIdEntry = request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY );
        int nIdEntry = Integer.parseInt( strIdEntry );
        Entry entry = EntryHome.findByPrimaryKey( nIdEntry );
        List<String> listErrors = new ArrayList<>( );
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
        String strIdDirectory = request.getParameter( DirectoriesConstants.PARAMETER_ID_DIRECTORY );
        int nIdDirectory = Integer.parseInt( strIdDirectory );
        Integer nEntryId = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY ) );
        Integer nOrderToSet = Integer.parseInt( request.getParameter( DirectoriesConstants.PARAMETER_ORDER_ID
                + request.getParameter( DirectoriesConstants.PARAMETER_ID_ENTRY ) ) );
        Entry entryToChangeOrder = EntryHome.findByPrimaryKey( nEntryId );
        int nActualOrder = entryToChangeOrder.getPosition( );
        if ( nOrderToSet != nActualOrder )
        {
            if ( nOrderToSet < entryToChangeOrder.getPosition( ) )
            {
                _entryService.moveUpEntryOrder( nOrderToSet, entryToChangeOrder );
            }
            else
            {
                _entryService.moveDownEntryOrder( nOrderToSet, entryToChangeOrder );
            }
        }
        return redirect( request, getURLModify( request, nIdDirectory ) );
    }

}
