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
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Interface for NewsletterDocumentDAO
 */
public interface INewsletterDocumentDAO
{

    /**
     * Get a newsletter document topic from its id
     * @param nIdTopic the id of the topic to get
     * @param plugin The plugin
     * @return The topic, or null if no topic was found
     */
    NewsletterDocument findByPrimaryKey( int nIdTopic, Plugin plugin );

    /**
     * Update a newsletter document topic
     * @param topic The topic to update
     * @param plugin The plugin
     */
    void updateDocumentTopic( NewsletterDocument topic, Plugin plugin );

    /**
     * Remove a newsletter document topic from the database
     * @param nIdTopic The id of the newsletter document topic to remove
     * @param plugin The plugin
     */
    void deleteDocumentTopic( int nIdTopic, Plugin plugin );

    /**
     * Insert a new newsletter document topic into the database
     * @param topic The newsletter document topic to insert
     * @param plugin the plugin
     */
    void createDocumentTopic( NewsletterDocument topic, Plugin plugin );

    /**
     * Select the list of documents published since the last sending of the
     * newsletter
     * @return a list of documents
     * @param nCategoryId The id of the category
     * @param dateLastSending the date of the last newsletter sending
     * @param plugin the document Plugin
     */
    Collection<Document> selectDocumentsByDateAndCategory( int nCategoryId, Timestamp dateLastSending, Plugin plugin );

    /**
     * Returns the list of the portlets which are document portlets
     * @return the list in form of a Collection object
     */
    ReferenceList selectDocumentListPortlets( );

    /**
     * Associate a new category to a newsletter topic
     * @param nTopicId the topic id
     * @param nDocumentCategoryId the document category identifier
     * @param plugin the newsletter document plugin
     */
    void associateNewsLetterDocumentCategory( int nTopicId, int nDocumentCategoryId, Plugin plugin );

    /**
     * Remove the relationship between a topic and the list of documents
     * @param nTopicId the topic id
     * @param plugin the newsletter document plugin
     */
    void deleteNewsLetterDocumentCategories( int nTopicId, Plugin plugin );

    /**
     * loads the list of categories linked to the newsletter
     * @param nTopicId the topic id
     * @param plugin the newsletter document plugin
     * @return the array of categories id
     */
    int[] selectNewsletterCategoryIds( int nTopicId, Plugin plugin );

    /**
     * Associate a new portlet to a newsletter topic
     * @param nTopicId the topic id
     * @param nPortletId the portlet identifier
     * @param plugin the newsletter document plugin
     */
    void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin );

    /**
     * Remove the relationship between a topic and the list of portlets
     * @param nTopicId the topic id
     * @param plugin the newsletter document plugin
     */
    void deleteNewsLetterDocumentPortlet( int nTopicId, Plugin plugin );

    /**
     * loads the list of document list portlets linked to the newsletter
     * @param nTopicId the topic id
     * @param plugin the newsletter document plugin
     * @return the array of categories id
     */
    int[] selectNewsletterPortletsIds( int nTopicId, Plugin plugin );

    /**
     * Get the list of id of published documents associated with a given
     * collection of portlets.
     * @param nPortletsIds The list of portlet ids.
     * @param datePublishing TODO
     * @param plugin The document plugin
     * @return The list of documents id.
     */
    List<Integer> getPublishedDocumentsIdsListByPortletIds( int[] nPortletsIds, Date datePublishing, Plugin plugin );

    /**
     * Check if a template is used by a topic
     * @param nIdNewsletterTemplate The id of the template
     * @param plugin The newsletter plugin
     * @return True if the template is used by a topic, false otherwise
     */
    boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin );
}
