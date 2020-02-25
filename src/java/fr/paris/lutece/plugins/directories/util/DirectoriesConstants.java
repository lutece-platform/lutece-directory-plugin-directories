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
package fr.paris.lutece.plugins.directories.util;

/**
 * 
 * Constants class for directories plugin
 *
 */
public final class DirectoriesConstants
{
    // Marks
    public static final String MARK_ID_DIRECTORY = "id_directory";
    public static final String MARK_ENTRY = "entry";
    public static final String MARK_ID_ENTRY = "id_entry";
    public static final String MARK_ID_PARENT = "id_parent";
    public static final String MARK_FIELD = "field";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LOCALE = "locale";
    public static final String MARK_LIST = "list";
    public static final String MARK_FORM_HTML = "form_html";
    public static final String MARK_STR_ENTRY = "str_entry";
    public static final String MARK_ENTRY_TYPE_SERVICE = "entryTypeService";
    public static final String MARK_LIST_DIRECTORY_ENTITY = "list_directory_entity";

    // Parameters
    public static final String PARAMETER_ID_DIRECTORY = "id_directory";
    public static final String PARAMETER_ID_ENTRY_TYPE = "id_type";
    public static final String PARAMETER_ID_FIELD = "id_field";
    public static final String PARAMETER_ID_ENTRY = "id_entry";
    public static final String PARAMETER_CANCEL = "cancel";
    public static final String PARAMETER_APPLY = "apply";
    public static final String PARAMETER_ORDER_ID = "order_id_";
    public static final String PARAMETER_ADD_TO_GROUP = "add_to_group";
    public static final String PARAMETER_ID_ENTRY_GROUP = "id_entry_group";
    public static final String PARAMETER_ENTRY_ID_MOVE = "entry_id_move";
    public static final String PARAMETER_FILTER = "filter";
    public static final String PARAMETER_SEARCH_RESULT_POSITION = "search_result_position";

    // Urls
    public static final String JSP_MANAGE_DIRECTORIES = "jsp/admin/plugins/directories/ManageDirectories.jsp";
    public static final String JSP_MANAGE_DIRECTORY_ENTRIES = "jsp/admin/plugins/directories/ManageDirectoryEntries.jsp";

    // Entry Fields
    public static final String FIELD_FILTER = "filter";
    public static final String FIELD_SEARCH_RESULT_POSITION = "search_result_position";

    /**
     * Default private constructor. Do not call
     */
    private DirectoriesConstants( )
    {
        throw new AssertionError( );
    }
}
