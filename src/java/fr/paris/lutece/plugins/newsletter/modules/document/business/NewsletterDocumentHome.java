package fr.paris.lutece.plugins.newsletter.modules.document.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.service.category.CategoryService;
import fr.paris.lutece.plugins.document.service.category.CategoryService.CategoryDisplay;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Home for newsletter document
 */
public class NewsletterDocumentHome
{
    private static INewsletterDocumentDAO _dao = SpringContextService
            .getBean( "newsletter-document.newsletterDocumentDAO" );

    /**
     * Returns the list of documents published by date and by topic
     * 
     * @param nDocumentListId the list identifier
     * @param dtDernierEnvoi the date of the last sending
     * @param plugin The plugin
     * @return a collection of document
     */
    public static Collection<Document> findDocumentsByDateAndList( int nDocumentListId, Timestamp dtDernierEnvoi,
            Plugin plugin )
    {
        return _dao.selectDocumentsByDateAndList( nDocumentListId, dtDernierEnvoi, plugin );
    }

    /**
     * Fetches all the categories defined
     * @param user the current user
     * @return A list of all categories
     */
    public static ReferenceList getAllCategories( AdminUser user )
    {
        ReferenceList list = new ReferenceList( );
        Collection<CategoryDisplay> listCategoriesDisplay = new ArrayList<CategoryDisplay>( );
        listCategoriesDisplay = CategoryService.getAllCategoriesDisplay( user );

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
     * 
     * @param nNewsLetterId the newsletter identifier
     * @param nDocumentListId the topic identifier
     * @param plugin the Plugin
     */
    public static void associateNewsLetterDocumentList( int nNewsLetterId, int nDocumentListId, Plugin plugin )
    {
        _dao.associateNewsLetterDocumentList( nNewsLetterId, nDocumentListId, plugin );
    }

    /**
     * Removes the relationship between a list of topics and a newsletter
     * 
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    public static void removeNewsLetterDocumentList( int nNewsLetterId, Plugin plugin )
    {
        _dao.deleteNewsLetterDocumentList( nNewsLetterId, plugin );
    }
}
