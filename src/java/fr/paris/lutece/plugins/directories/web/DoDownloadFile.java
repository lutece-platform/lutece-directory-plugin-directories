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

import fr.paris.lutece.plugins.directories.business.DirectoryResponse;
import fr.paris.lutece.plugins.directories.business.DirectoryResponseHome;
import fr.paris.lutece.plugins.directories.util.DirectoriesUtils;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.filesystem.FileSystemUtil;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * class DoDownloadGraph
 *
 */
public final class DoDownloadFile
{
    private static final String PARAMETER_ID_FILE = "id_file";
    private static final String PARAMETER_ID_RESPONSE = "id_response";
    private static final String MESSAGE_ERROR_DURING_DOWNLOAD_FILE = "error";

    /**
     * Private constructor
     */
    private DoDownloadFile( )
    {
    }

    /**
     * Write in the http response the file to upload
     * 
     * @param request
     *            the http request
     * @param response
     *            The http response
     * @return Error Message
     *
     */
    public static String doDownloadFile( HttpServletRequest request, HttpServletResponse response )
    {
        String strIdFile = request.getParameter( PARAMETER_ID_FILE );
        int nIdFile = Integer.parseInt( strIdFile );
        String strIdResponse = request.getParameter( PARAMETER_ID_RESPONSE );
        int nIdResponse = Integer.parseInt( strIdResponse );
        DirectoryResponse directoryResponse = DirectoryResponseHome.getDirectoryResponsesListByIdResponse( nIdResponse );
        if ( directoryResponse != null )
        {
            Response genAttResponse = ResponseHome.findByPrimaryKey( directoryResponse.getIdResponse( ) );
            if ( genAttResponse.getFile( ) != null && genAttResponse.getFile( ).getIdFile( ) == nIdFile )
            {
                File file = FileHome.findByPrimaryKey( nIdFile );
                PhysicalFile physicalFile = ( file != null ) ? PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) ) : null;
                if ( physicalFile != null )
                {
                    try
                    {
                        byte [ ] byteFileOutPut = physicalFile.getValue( );
                        DirectoriesUtils.addHeaderResponse( request, response, file.getTitle( ) );
                        String strMimeType = file.getMimeType( );
                        if ( strMimeType == null )
                        {
                            strMimeType = FileSystemUtil.getMIMEType( file.getTitle( ) );
                        }
                        response.setContentType( strMimeType );
                        response.setContentLength( byteFileOutPut.length );
                        OutputStream os = response.getOutputStream( );
                        os.write( byteFileOutPut );
                        os.close( );
                    }
                    catch( IOException e )
                    {
                        AppLogService.error( e );
                    }
                }
            }
        }
        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DURING_DOWNLOAD_FILE, AdminMessage.TYPE_STOP );
    }
}
