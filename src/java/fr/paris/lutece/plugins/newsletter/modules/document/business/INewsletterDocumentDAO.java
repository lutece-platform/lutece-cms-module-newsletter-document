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
    NewsletterDocument findByPrimaryKey( int nIdSection, Plugin plugin );

    /**
     * Update a newsletter document section
     * @param section The section to update
     * @param plugin The plugin
     */
    void updateDocumentSection( NewsletterDocument section, Plugin plugin );

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
    void createDocumentSection( NewsletterDocument section, Plugin plugin );

    /**
     * Select the list of documents published since the last sending of the
     * newsletter
     * @return a list of documents
     * @param nCategoryId The id of the category
     * @param dateLastSending the date of the last newsletter sending
     * @param plugin the Plugin
     */
    Collection<Document> selectDocumentsByDateAndCategory( int nCategoryId, Timestamp dateLastSending, Plugin plugin );

    /**
     * Returns the list of the portlets which are document portlets
     * @param plugin the Plugin
     * @return the list in form of a Collection object
     */
    ReferenceList selectDocumentTypePortlets( Plugin plugin );

    /**
     * Associate a new topic to a newsletter
     * @param nSectionId the section id
     * @param nDocumentCategoryId the topic identifier
     * @param plugin the Plugin
     */
    void associateNewsLetterDocumentList( int nSectionId, int nDocumentCategoryId, Plugin plugin );

    /**
     * Remove the relationship between a newsletter and the list of documents
     * @param nSectionId the section id
     * @param plugin the Plugin
     */
    void deleteNewsLetterDocumentList( int nSectionId, Plugin plugin );

    /**
     * loads the list of categories linked to the newsletter
     * @param nSectionId the section id
     * @param plugin the plugin
     * @return the array of categories id
     */
    int[] selectNewsletterCategoryIds( int nSectionId, Plugin plugin );

    /**
     * Check if a template is used by a section
     * @param nIdNewsletterTemplate The id of the template
     * @param plugin The newsletter plugin
     * @return True if the template is used by a section, false otherwise
     */
    boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin );

}
