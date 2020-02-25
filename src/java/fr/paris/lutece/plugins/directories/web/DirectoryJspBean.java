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

import fr.paris.lutece.plugins.directories.business.Directory;
import fr.paris.lutece.plugins.directories.business.DirectoryHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Directory features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDirectories.jsp", controllerPath = "jsp/admin/plugins/directories/", right = "DIRECTORIES_MANAGEMENT" )
public class DirectoryJspBean extends AbstractDirectoriesManagerJspBean
{
    private static final long serialVersionUID = -1760898157231830659L;
    // Templates
    private static final String TEMPLATE_MANAGE_DIRECTORIES = "/admin/plugins/directories/manage_directories.html";
    private static final String TEMPLATE_CREATE_DIRECTORY = "/admin/plugins/directories/create_directory.html";
    private static final String TEMPLATE_MODIFY_DIRECTORY = "/admin/plugins/directories/modify_directory.html";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DIRECTORIES = "directories.manage_directories.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DIRECTORY = "directories.modify_directory.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DIRECTORY = "directories.create_directory.pageTitle";

    // Markers
    private static final String MARK_DIRECTORY_LIST = "directory_list";
    private static final String MARK_DIRECTORY = "directory";

    private static final String JSP_MANAGE_DIRECTORIES = "jsp/admin/plugins/directories/ManageDirectories.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DIRECTORY = "directories.message.confirmRemoveDirectory";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "directories.model.entity.directory.attribute.";

    // Views
    private static final String VIEW_MANAGE_DIRECTORIES = "manageDirectories";
    private static final String VIEW_CREATE_DIRECTORY = "createDirectory";
    private static final String VIEW_MODIFY_DIRECTORY = "modifyDirectory";

    // Actions
    private static final String ACTION_CREATE_DIRECTORY = "createDirectory";
    private static final String ACTION_MODIFY_DIRECTORY = "modifyDirectory";
    private static final String ACTION_REMOVE_DIRECTORY = "removeDirectory";
    private static final String ACTION_CONFIRM_REMOVE_DIRECTORY = "confirmRemoveDirectory";

    // Infos
    private static final String INFO_DIRECTORY_CREATED = "directories.info.directory.created";
    private static final String INFO_DIRECTORY_UPDATED = "directories.info.directory.updated";
    private static final String INFO_DIRECTORY_REMOVED = "directories.info.directory.removed";

    // Session variable to store working values
    private Directory _directory;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DIRECTORIES, defaultView = true )
    public String getManageDirectories( HttpServletRequest request )
    {
        _directory = null;
        List<Directory> listDirectories = DirectoryHome.getDirectoriesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DIRECTORY_LIST, listDirectories, JSP_MANAGE_DIRECTORIES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DIRECTORIES, TEMPLATE_MANAGE_DIRECTORIES, model );
    }

    /**
     * Returns the form to create a directory
     *
     * @param request
     *            The Http request
     * @return the html code of the directory form
     */
    @View( VIEW_CREATE_DIRECTORY )
    public String getCreateDirectory( HttpServletRequest request )
    {
        _directory = ( _directory != null ) ? _directory : new Directory( );

        Map<String, Object> model = getModel( );
        model.put( MARK_DIRECTORY, _directory );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DIRECTORY, TEMPLATE_CREATE_DIRECTORY, model );
    }

    /**
     * Process the data capture form of a new directory
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DIRECTORY )
    public String doCreateDirectory( HttpServletRequest request )
    {
        populate( _directory, request, request.getLocale( ) );

        // Check constraints
        if ( !validateBean( _directory, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DIRECTORY );
        }

        DirectoryHome.create( _directory );
        addInfo( INFO_DIRECTORY_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORIES );
    }

    /**
     * Manages the removal form of a directory whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DIRECTORY )
    public String getConfirmRemoveDirectory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DIRECTORY ) );
        url.addParameter( PARAMETER_ID_DIRECTORY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DIRECTORY, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a directory
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage directories
     */
    @Action( ACTION_REMOVE_DIRECTORY )
    public String doRemoveDirectory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );
        DirectoryHome.remove( nId );
        addInfo( INFO_DIRECTORY_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORIES );
    }

    /**
     * Returns the form to update info about a directory
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DIRECTORY )
    public String getModifyDirectory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );

        if ( _directory == null || ( _directory.getId( ) != nId ) )
        {
            _directory = DirectoryHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_DIRECTORY, _directory );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DIRECTORY, TEMPLATE_MODIFY_DIRECTORY, model );
    }

    /**
     * Process the change form of a directory
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DIRECTORY )
    public String doModifyDirectory( HttpServletRequest request )
    {
        populate( _directory, request, request.getLocale( ) );

        // Check constraints
        if ( !validateBean( _directory, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DIRECTORY, PARAMETER_ID_DIRECTORY, _directory.getId( ) );
        }

        DirectoryHome.update( _directory );
        addInfo( INFO_DIRECTORY_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORIES );
    }
}
