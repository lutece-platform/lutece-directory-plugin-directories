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

import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.url.UrlItem;

/**
 * JspBean to manage directories form fieldsl
 * 
 * @author
 *
 */
@Controller( controllerJsp = "ManageDirectoryFields.jsp", controllerPath = "jsp/admin/plugins/directories/", right = "DIRECTORIES_MANAGEMENT" )
public class DirectoryFieldsJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = -1505164256619633838L;
    // Urls
    private static final String JSP_URL_MANAGE_DIRECTORYITEM_FORM_FIELDS = "jsp/admin/plugins/directories/ManageDirectoryFields.jsp";
    // Views
    private static final String VIEW_GET_MODIFY_FIELD_WITH_CONDITIONAL_QUESTIONS = "getModifyFieldCC";
    // Parameters
    private static final String PARAMETER_ID_FIELD = "id_field";

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
}
