/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.newsletter.modules.document.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.service.category.CategoryService;
import fr.paris.lutece.plugins.document.service.category.CategoryService.CategoryDisplay;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Home for newsletter document
 */
public final class NewsletterDocumentHome
{
    private static INewsletterDocumentDAO _dao = SpringContextService
            .getBean( "newsletter-document.newsletterDocumentDAO" );

    /**
     * Private constructor
     */
    private NewsletterDocumentHome( )
    {
    }

    /**
     * Get a newsletter document topic from its id
     * @param nIdTopic the id of the topic to get
     * @param plugin The plugin
     * @return The topic, or null if no topic was found
     */
    public static NewsletterDocument findByPrimaryKey( int nIdTopic, Plugin plugin )
    {
        return _dao.findByPrimaryKey( nIdTopic, plugin );
    }

    /**
     * Update a newsletter document topic
     * @param topic The topic to update
     * @param plugin The plugin
     */
    public static void updateDocumentTopic( NewsletterDocument topic, Plugin plugin )
    {
        _dao.updateDocumentTopic( topic, plugin );
    }

    /**
     * Remove a newsletter document topic from the database
     * @param nIdTopic The id of the newsletter document topic to remove
     * @param plugin The plugin
     */
    public static void deleteDocumentTopic( int nIdTopic, Plugin plugin )
    {
        _dao.deleteDocumentTopic( nIdTopic, plugin );
    }

    /**
     * Insert a new newsletter document topic into the database
     * @param topic The newsletter document topic to insert
     * @param plugin the plugin
     */
    public static void createDocumentTopic( NewsletterDocument topic, Plugin plugin )
    {
        _dao.createDocumentTopic( topic, plugin );
    }

    /**
     * Returns the list of documents published by date and by topic
     * @param nDocumentCategoryId the id of the category
     * @param dtDernierEnvoi the date of the last sending
     * @param plugin The plugin
     * @return a collection of document
     */
    public static Collection<Document> findDocumentsByDateAndCategory( int nDocumentCategoryId,
            Timestamp dtDernierEnvoi, Plugin plugin )
    {
        return _dao.selectDocumentsByDateAndCategory( nDocumentCategoryId, dtDernierEnvoi, plugin );
    }

    /**
     * Fetches all the categories defined
     * @param user the current user
     * @return A list of all categories
     */
    public static ReferenceList getAllCategories( AdminUser user )
    {
        ReferenceList list = new ReferenceList( );
        Collection<CategoryDisplay> listCategoriesDisplay = CategoryService.getAllCategoriesDisplay( user );

        for ( CategoryDisplay category : listCategoriesDisplay )
        {
            list.addItem( category.getCategory( ).getId( ), category.getCategory( ).getDescription( ) );
        }

        return list;
    }

    /**
     * Returns the collection of any portlet containing at least a published
     * document
     * @param plugin The plugin
     * @return the portlets list
     */
    public static ReferenceList getDocumentListPortlets( Plugin plugin )
    {
        return _dao.selectDocumentListPortlets( );
    }

    /**
     * Associate a document category to a newsletter topic
     * @param nTopicId the topic identifier
     * @param nDocumentCategoryId the id of the document category to associate
     * @param plugin the Plugin
     */
    public static void associateNewsLetterDocumentCategory( int nTopicId, int nDocumentCategoryId, Plugin plugin )
    {
        _dao.associateNewsLetterDocumentCategory( nTopicId, nDocumentCategoryId, plugin );
    }

    /**
     * Removes the relationship between a list of document categories and a
     * newsletter topic
     * @param nTopicId the newsletter identifier
     * @param plugin the Plugin
     */
    public static void removeNewsLetterDocumentCategories( int nTopicId, Plugin plugin )
    {
        _dao.deleteNewsLetterDocumentCategories( nTopicId, plugin );
    }

    /**
     * loads the list of categories of the newsletter
     * @param nTopicId the topic identifier
     * @param plugin the plugin
     * @return the array of categories id
     */
    public static int[] findNewsletterCategoryIds( int nTopicId, Plugin plugin )
    {
        return _dao.selectNewsletterCategoryIds( nTopicId, plugin );
    }

    /**
     * Associate a new portlet to a newsletter topic
     * @param nTopicId the topic id
     * @param nPortletId the portlet identifier
     * @param plugin the newsletter document plugin
     */
    public static void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin )
    {
        _dao.associateNewsLetterDocumentPortlet( nTopicId, nPortletId, plugin );
    }

    /**
     * Remove the relationship between a topic and the list of portlets
     * @param nTopicId the topic id
     * @param plugin the newsletter document plugin
     */
    public static void removeNewsLetterDocumentPortlet( int nTopicId, Plugin plugin )
    {
        _dao.deleteNewsLetterDocumentPortlet( nTopicId, plugin );
    }

    /**
     * loads the list of document list portlets linked to the newsletter
     * @param nTopicId the topic identifier
     * @param plugin the plugin
     * @return the array of portlets id
     */
    public static int[] findNewsletterPortletsIds( int nTopicId, Plugin plugin )
    {
        return _dao.selectNewsletterPortletsIds( nTopicId, plugin );
    }

    /**
     * Get the list of id of published documents associated with a given
     * collection of portlets.
     * @param nPortletsIds The list of portlet ids.
     * @param datePublishing TODO
     * @param plugin The document plugin
     * @return The list of documents id.
     */
    public static List<Integer> getPublishedDocumentsIdsListByPortletIds( int[] nPortletsIds, Date datePublishing, Plugin plugin )
    {
        return _dao.getPublishedDocumentsIdsListByPortletIds( nPortletsIds, datePublishing, plugin );
    }

    /**
     * Check if a template is used by a topic
     * @param nIdNewsletterTemplate The id of the template
     * @param plugin The newsletter plugin
     * @return True if the template is used by a topic, false otherwise
     */
    public static boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin )
    {
        return _dao.findTemplate( nIdNewsletterTemplate, plugin );
    }
}
