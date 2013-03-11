package fr.paris.lutece.plugins.newsletter.modules.document.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.sql.Timestamp;
import java.util.Collection;


/**
 * Interface for NewsletterDocumentDAO
 */
public interface INewsletterDocumentDAO
{

    /**
     * Get a newsletter document section from its id
     * @param nIdSection the id of the section to get
     * @param plugin The plugin
     * @return The section, or null if no section was found
     */
    NewsletterDocumentSection findByPrimaryKey( int nIdSection, Plugin plugin );

    /**
     * Update a newsletter document section
     * @param section The section to update
     * @param plugin The plugin
     */
    void updateDocumentSection( NewsletterDocumentSection section, Plugin plugin );

    /**
     * Remove a newsletter document section from the database
     * @param nIdSection The id of the newsletter document section to remove
     * @param plugin The plugin
     */
    void deleteDocumentSection( int nIdSection, Plugin plugin );

    /**
     * Insert a new newsletter document section into the database
     * @param section The newsletter document section to insert
     * @param plugin the plugin
     */
    void createDocumentSection( NewsletterDocumentSection section, Plugin plugin );

    /**
     * Select the list of documents published since the last sending of the
     * newsletter
     * @return a list of documents
     * @param nCategoryId The id of the category
     * @param dateLastSending the date of the last newsletter sending
     * @param plugin the Plugin
     */
    Collection<Document> selectDocumentsByDateAndList( int nCategoryId, Timestamp dateLastSending, Plugin plugin );

    /**
     * Returns the list of the portlets which are document portlets
     * @param plugin the Plugin
     * @return the list in form of a Collection object
     */
    ReferenceList selectDocumentTypePortlets( Plugin plugin );

    /**
     * Associate a new topic to a newsletter
     * 
     * @param nNewsLetterId the newsletter identifier
     * @param nDocumentListId the topic identifier
     * @param plugin the Plugin
     */
    void associateNewsLetterDocumentList( int nNewsLetterId, int nDocumentListId, Plugin plugin );

    /**
     * Remove the relationship between a newsletter and the list of documents
     * 
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    void deleteNewsLetterDocumentList( int nNewsLetterId, Plugin plugin );
}
