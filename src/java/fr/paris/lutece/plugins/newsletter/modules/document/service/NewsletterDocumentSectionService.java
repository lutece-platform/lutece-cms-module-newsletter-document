package fr.paris.lutece.plugins.newsletter.modules.document.service;

import fr.paris.lutece.plugins.document.service.category.CategoryService;
import fr.paris.lutece.plugins.document.service.category.CategoryService.CategoryDisplay;
import fr.paris.lutece.plugins.newsletter.business.section.NewsletterSection;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocumentHome;
import fr.paris.lutece.plugins.newsletter.service.section.INewsletterSectionService;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * The newsletter document section service
 */
public class NewsletterDocumentSectionService implements INewsletterSectionService
{
    private static final String NEWSLETTER_DOCUMENT_SECTION_TYPE = "NEWSLETTER_DOCUMENT";

    // PARAMETERS
    private static final String PARAMETER_CATEGORY_LIST_ID = "category_list_id";
    private static final String PARAMETER_NEWSLETTER_NO_CATEGORY = "newsletter_no_category";

    // PROPERTIES
    private static final String PROPERTY_NO_CATEGORY = "no_category";

    // MESSAGES
    private static final String MESSAGE_NEWSLETTER_DOCUMENT_SECTION_TYPE_NAME = "module.newsletter.document.sectionType.name";

    // MARKS
    private static final String MARK_CATEGORY_LIST = "category_list";

    private Plugin _newsletterDocumentPlugin;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewsletterSectionTypeCode( )
    {
        return NEWSLETTER_DOCUMENT_SECTION_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewsletterSectionTypeName( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_NEWSLETTER_DOCUMENT_SECTION_TYPE_NAME, locale );
    }

    /**
     * {@inheritDoc}
     * @return Always return true
     */
    @Override
    public boolean hasConfiguration( )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigurationPage( NewsletterSection newsletterSection, AdminUser user, Locale locale )
    {
        // TODO Auto-generated method stub
        Map<String, Object> model = new HashMap<String, Object>( );

        Collection<CategoryDisplay> listCategoriesDisplay = new ArrayList<CategoryDisplay>( );
        listCategoriesDisplay = CategoryService.getAllCategoriesDisplay( user );

        model.put( MARK_CATEGORY_LIST, listCategoriesDisplay );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String saveConfiguration( Map<String, Object> mapParameters, NewsletterSection newsletterSection,
            AdminUser user, Locale locale )
    {
        // TODO Auto-generated method stub
        String[] strCategoryIds = (String[]) mapParameters.get( PARAMETER_CATEGORY_LIST_ID );
        String strNoCategory = NewsletterUtils.getStringFromStringArray( mapParameters
                .get( PARAMETER_NEWSLETTER_NO_CATEGORY ) );

        NewsletterDocumentHome
                .removeNewsLetterDocumentList( newsletterSection.getId( ), getNewsletterDocumentPlugin( ) );

        if ( ( strNoCategory == null ) || !strNoCategory.equals( PROPERTY_NO_CATEGORY ) )
        {
            // recreate the category list with the new selection
            for ( int i = 0; i < strCategoryIds.length; i++ )
            {
                int nCategoryId = Integer.parseInt( strCategoryIds[i] );
                NewsletterDocumentHome.associateNewsLetterDocumentList( newsletterSection.getId( ), nCategoryId,
                        getNewsletterDocumentPlugin( ) );
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewsletterSection( NewsletterSection newsletterSection, AdminUser user, Locale locale )
    {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNewsletterSection( int nNewsletterSectionId )
    {
        // TODO Auto-generated method stub

        // removes relationship between the newsletter and document list
        NewsletterDocumentHome.removeNewsLetterDocumentList( nNewsletterSectionId, getNewsletterDocumentPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtmlContent( NewsletterSection newsletterSection, AdminUser user, Locale locale )
    {
        // TODO Auto-generated method stub
        return StringUtils.EMPTY;
    }

    private synchronized Plugin getNewsletterDocumentPlugin( )
    {
        if ( _newsletterDocumentPlugin == null )
        {
            _newsletterDocumentPlugin = PluginService.getPlugin( NewsletterDocumentPlugin.PLUGIN_NAME );
        }
        return _newsletterDocumentPlugin;
    }

}
