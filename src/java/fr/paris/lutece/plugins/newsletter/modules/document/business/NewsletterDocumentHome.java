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
     * Get a newsletter document section from its id
     * @param nIdSection the id of the section to get
     * @param plugin The plugin
     * @return The section, or null if no section was found
     */
    public static NewsletterDocument findByPrimaryKey( int nIdSection, Plugin plugin )
    {
        return _dao.findByPrimaryKey( nIdSection, plugin );
    }

    /**
     * Update a newsletter document section
     * @param section The section to update
     * @param plugin The plugin
     */
    public static void updateDocumentSection( NewsletterDocument section, Plugin plugin )
    {
        _dao.updateDocumentSection( section, plugin );
    }

    /**
     * Remove a newsletter document section from the database
     * @param nIdSection The id of the newsletter document section to remove
     * @param plugin The plugin
     */
    public static void deleteDocumentSection( int nIdSection, Plugin plugin )
    {
        _dao.deleteDocumentSection( nIdSection, plugin );
    }

    /**
     * Insert a new newsletter document section into the database
     * @param section The newsletter document section to insert
     * @param plugin the plugin
     */
    public static void createDocumentSection( NewsletterDocument section, Plugin plugin )
    {
        _dao.createDocumentSection( section, plugin );
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
     * @param nSectionId the section identifier
     * @param nDocumentCategoryId the id of the document category to associate
     * @param plugin the Plugin
     */
    public static void associateNewsLetterDocumentList( int nSectionId, int nDocumentCategoryId, Plugin plugin )
    {
        _dao.associateNewsLetterDocumentList( nSectionId, nDocumentCategoryId, plugin );
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
     * @param nSectionId the section identifier
     * @param plugin the plugin
     * @return the array of categories id
     */
    public static int[] findNewsletterCategoryIds( int nSectionId, Plugin plugin )
    {
        return _dao.selectNewsletterCategoryIds( nSectionId, plugin );
    }
}
