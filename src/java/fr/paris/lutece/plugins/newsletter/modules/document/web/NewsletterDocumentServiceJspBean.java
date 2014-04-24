/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.newsletter.modules.document.web;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.service.DocumentPlugin;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplateHome;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocumentHome;
import fr.paris.lutece.plugins.newsletter.modules.document.service.NewsletterDocumentService;
import fr.paris.lutece.plugins.newsletter.modules.document.service.NewsletterDocumentTopicService;
import fr.paris.lutece.plugins.newsletter.modules.document.util.NewsletterDocumentUtils;
import fr.paris.lutece.plugins.newsletter.service.NewsletterPlugin;
import fr.paris.lutece.plugins.newsletter.service.NewsletterService;
import fr.paris.lutece.plugins.newsletter.util.NewsLetterConstants;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.insert.InsertServiceJspBean;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectionBean;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


/**
 * This class is responsible for the insertion of document lists in the
 * newsletter.
 */
public class NewsletterDocumentServiceJspBean extends InsertServiceJspBean implements InsertServiceSelectionBean
{
    /**
     * The newsletter right needed to manage
     */
    public static final String RIGHT_NEWSLETTER_MANAGEMENT = "NEWSLETTER_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -4095074358460689539L;

    // templates
    private static final String TEMPLATE_SELECT_DOCUMENTS = "admin/plugins/newsletter/modules/document/select_documents.html";
    private static final String TEMPLATE_INSERT_DOCEMENTS = "admin/plugins/newsletter/modules/document/insert_documents.html";

    // bookmarks
    private static final String BOOKMARK_START_PUBLISHED_DATE = "start_published_date";
    private static final String MARK_DOCUMENT_LIST = "document_list";
    private static final String MARK_COMBO_DOCUMENT_LIST = "documents_lists_list";
    private static final String MARK_INPUT = "input";
    private static final String MARK_TEMPLATES_LIST = "documents_templates_list";
    private static final String MARK_DOCUMENTS_LIST = "documents_list";

    // parameters
    private static final String PARAMETER_DOCUMENT_LIST_ID = "document_list_id";
    private static final String PARAMETER_TEMPLATE_ID = "template_id";
    private static final String PARAMETER_DOCUMENTS_LIST = "documents_list";
    private static final String PARAMETER_PUBLISHED_DATE = "published_date";
    private static final String PARAMETER_INPUT = "input";

    // property
    private static final String LABEL_FRAGMENT_COMBO_ALL_DOCUMENT_LIST_ITEM = "module.newsletter.document.documents.selection.lists.all";
    private static final String MESSAGE_NO_DOCUMENT_TEMPLATE = "module.newsletter.document.message.noDocumentTemplate";
    private static final String MESSAGE_NO_DOCUMENT_CHOSEN = "module.newsletter.document.message.noDocumentChosen";

    private static final String CONSTANT_STRING_ZERO = "0";
    private NewsletterService _newsletterService = NewsletterService.getService( );

    /**
     * Inserts Html code by the insert service
     * @param request The Http request
     * @return The string representation of the category
     */
    @Override
    public String getInsertServiceSelectorUI( HttpServletRequest request )
    {
        Plugin pluginDocument = PluginService.getPlugin( DocumentPlugin.PLUGIN_NAME );
        Plugin pluginNewsletter = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );

        Locale locale = AdminUserService.getLocale( request );

        // get the document list from request
        String strDocumentListId = request.getParameter( PARAMETER_DOCUMENT_LIST_ID );
        strDocumentListId = ( strDocumentListId != null ) ? strDocumentListId : CONSTANT_STRING_ZERO;

        int nDocumentCategoryId = Integer.parseInt( strDocumentListId );

        // get template from request
        String strTemplateId = request.getParameter( PARAMETER_TEMPLATE_ID );
        strTemplateId = ( strTemplateId != null ) ? strTemplateId : CONSTANT_STRING_ZERO;

        String strPublishedDate = request.getParameter( PARAMETER_PUBLISHED_DATE );
        strPublishedDate = ( strPublishedDate != null ) ? strPublishedDate : StringUtils.EMPTY;

        Timestamp publishedDate = DateUtil.formatTimestamp( strPublishedDate, AdminUserService.getLocale( request ) );
        Map<String, Object> model = new HashMap<String, Object>( );

        // Criteria
        // Combo of available document list portlets
        ReferenceList listDocumentPortlets = NewsletterDocumentHome.getDocumentListPortlets( pluginDocument );
        ReferenceItem refItem = new ReferenceItem( );
        refItem.setCode( CONSTANT_STRING_ZERO );
        refItem.setName( I18nService.getLocalizedString( LABEL_FRAGMENT_COMBO_ALL_DOCUMENT_LIST_ITEM, locale ) );
        listDocumentPortlets.add( 0, refItem );
        model.put( MARK_COMBO_DOCUMENT_LIST, listDocumentPortlets );

        // re-display the published date field
        model.put( BOOKMARK_START_PUBLISHED_DATE, strPublishedDate );

        // Document list
        Collection<Document> list = NewsletterDocumentHome.findDocumentsByDateAndCategory( nDocumentCategoryId,
                publishedDate, pluginDocument );
        model.put( MARK_DOCUMENT_LIST, list );

        ReferenceList templateList = NewsLetterTemplateHome.getTemplatesListByType(
                NewsletterDocumentTopicService.NEWSLETTER_DOCUMENT_TOPIC_TYPE, pluginNewsletter );
        model.put( MARK_TEMPLATES_LIST, templateList );

        //Replace portal path for editor and document display
        String strWebappUrl = AppPathService.getBaseUrl( request );
        model.put( NewsLetterConstants.WEBAPP_PATH_FOR_LINKSERVICE, strWebappUrl );
        model.put( MARK_INPUT, request.getParameter( PARAMETER_INPUT ) );
        model.put( PARAMETER_DOCUMENT_LIST_ID, strDocumentListId );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECT_DOCUMENTS, locale, model );

        return template.getHtml( );
    }

    /**
     * Search for a list of documents
     * Actually does the same as getHtmlSelectorUI
     * @param request the http request
     * @return the found documents
     */
    public String doSearchDocuments( HttpServletRequest request )
    {
        return getInsertServiceSelectorUI( request );
    }

    /**
     * Insert the selected documents as a piece of html code into the html
     * editor
     * @param request the http request
     * @return the html code to display (this code uses a javascript to close
     *         the selection window and insert the document list into the
     *         editor)
     */
    public String doInsert( HttpServletRequest request )
    {
        Plugin pluginNewsletter = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );

        String strBaseUrl = AppPathService.getBaseUrl( request );
        String strInput = request.getParameter( PARAMETER_INPUT );
        Map<String, Object> model = new HashMap<String, Object>( );
        String strTemplateId = request.getParameter( PARAMETER_TEMPLATE_ID );
        strTemplateId = ( strTemplateId != null ) ? strTemplateId : CONSTANT_STRING_ZERO;

        if ( StringUtils.isEmpty( strTemplateId ) || !StringUtils.isNumeric( strTemplateId )
                || StringUtils.equals( strTemplateId, CONSTANT_STRING_ZERO ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_DOCUMENT_TEMPLATE, AdminMessage.TYPE_STOP );
        }

        int nTemplateId = Integer.parseInt( strTemplateId );
        String[] strDocumentsIdsList = request.getParameterValues( PARAMETER_DOCUMENTS_LIST );

        if ( ( strDocumentsIdsList == null ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_DOCUMENT_CHOSEN, AdminMessage.TYPE_STOP );
        }

        Locale locale = AdminUserService.getLocale( request );
        Collection<String> documentsList = new ArrayList<String>( );

        // retrieves the html template in order to use it to display the list of documents
        String strPathDocumentTemplate = NewsletterUtils.getHtmlTemplatePath( nTemplateId, pluginNewsletter );

        for ( int i = 0; i < strDocumentsIdsList.length; i++ )
        {
            int nDocumentId = Integer.parseInt( strDocumentsIdsList[i] );
            Document document = DocumentHome.findByPrimaryKey( nDocumentId );
            String strDocumentHtmlCode = NewsletterDocumentService.getInstance( ).fillTemplateWithDocumentInfos(
                    strPathDocumentTemplate, document, locale, strBaseUrl, AdminUserService.getAdminUser( request ) );
            documentsList.add( strDocumentHtmlCode );
        }

        model.put( MARK_DOCUMENTS_LIST, documentsList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_INSERT_DOCEMENTS, locale, model );
        template.substitute( NewsLetterConstants.WEBAPP_PATH_FOR_LINKSERVICE, strBaseUrl );

        String strContent = template.getHtml( );

        // We check if we need to unsecure files of the document to include them as links in the content
        if ( _newsletterService.useUnsecuredImages( ) )
        {
            String strUnsecuredFolder = _newsletterService.getUnsecuredImagefolder( );
            String strUnsecuredFolderPath = _newsletterService.getUnsecuredFolderPath( );
            strContent = NewsletterDocumentUtils.rewriteImgUrls( strContent, AppPathService.getBaseUrl( ),
                    _newsletterService.getUnsecuredWebappUrl( ), strUnsecuredFolderPath, strUnsecuredFolder );
        }

        return insertUrl( request, strInput, StringEscapeUtils.escapeJavaScript( strContent ) );
    }
}
