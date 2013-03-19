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
     * Returns a collection any portlet containing at least a published document
     * @param plugin The plugin
     * @return the portlets in form of Collection
     */
    public static ReferenceList getDocumentLists( Plugin plugin )
    {
        return _dao.selectDocumentTypePortlets( plugin );
    }

    /**
     * Associate a topic to a newsletter
     * @param nTopicId the topic identifier
     * @param nDocumentCategoryId the id of the document category to associate
     * @param plugin the Plugin
     */
    public static void associateNewsLetterDocumentList( int nTopicId, int nDocumentCategoryId, Plugin plugin )
    {
        _dao.associateNewsLetterDocumentList( nTopicId, nDocumentCategoryId, plugin );
    }

    /**
     * Removes the relationship between a list of topics and a newsletter
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    public static void removeNewsLetterDocumentList( int nNewsLetterId, Plugin plugin )
    {
        _dao.deleteNewsLetterDocumentList( nNewsLetterId, plugin );
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
