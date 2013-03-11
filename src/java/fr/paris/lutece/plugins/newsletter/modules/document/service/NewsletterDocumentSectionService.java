package fr.paris.lutece.plugins.newsletter.modules.document.service;

import fr.paris.lutece.plugins.document.service.category.CategoryService;
import fr.paris.lutece.plugins.document.service.category.CategoryService.CategoryDisplay;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplateHome;
import fr.paris.lutece.plugins.newsletter.business.section.NewsletterSection;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocumentHome;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocumentSection;
import fr.paris.lutece.plugins.newsletter.service.NewsletterPlugin;
import fr.paris.lutece.plugins.newsletter.service.section.INewsletterSectionService;
import fr.paris.lutece.plugins.newsletter.service.section.NewsletterSectionService;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

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
    /**
     * NEwsletter document section type
     */
    public static final String NEWSLETTER_DOCUMENT_SECTION_TYPE = "NEWSLETTER_DOCUMENT";

    // PARAMETERS
    private static final String PARAMETER_CATEGORY_LIST_ID = "category_list_id";
    private static final String PARAMETER_NEWSLETTER_NO_CATEGORY = "newsletter_no_category";

    // PROPERTIES
    private static final String PROPERTY_NO_CATEGORY = "no_category";

    // MESSAGES
    private static final String MESSAGE_NEWSLETTER_DOCUMENT_SECTION_TYPE_NAME = "module.newsletter.document.sectionType.name";

    // MARKS
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_TEMPLATES_LIST = "templates_list";

    // TEMPLATES
    private static final String TEMPLATE_MODIFY_NEWSLETTER_DOCUMENT_SECTION_CONFIG = "admin/plugins/newsletter/modules/document/modify_newsletter_document_section_config.html";

    private Plugin _newsletterDocumentPlugin;
    private Plugin _newsletterPlugin;
    private NewsletterSectionService _newsletterSectionService;

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
    public String getConfigurationPage( NewsletterSection newsletterSection, String strBaseUrl, AdminUser user,
            Locale locale )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        Collection<CategoryDisplay> listCategoriesDisplay = new ArrayList<CategoryDisplay>( );
        listCategoriesDisplay = CategoryService.getAllCategoriesDisplay( user );

        model.put( MARK_CATEGORY_LIST, listCategoriesDisplay );
        model.put( MARK_TEMPLATES_LIST, NewsLetterTemplateHome.getTemplatesListByType(
                NEWSLETTER_DOCUMENT_SECTION_TYPE, getNewsletterPlugin( ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_NEWSLETTER_DOCUMENT_SECTION_CONFIG,
                locale, model );
        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration( Map<String, String[]> mapParameters, NewsletterSection newsletterSection,
            AdminUser user, Locale locale )
    {
        // TODO Auto-generated method stub
        String[] strCategoryIds = mapParameters.get( PARAMETER_CATEGORY_LIST_ID );
        String strNoCategory = NewsletterUtils.getStringFromStringArray( mapParameters
                .get( PARAMETER_NEWSLETTER_NO_CATEGORY ) );

        NewsletterDocumentHome
                .removeNewsLetterDocumentList( newsletterSection.getId( ), getNewsletterDocumentPlugin( ) );

        if ( ( strCategoryIds != null ) && !strCategoryIds.equals( PROPERTY_NO_CATEGORY ) )
        {
            // recreate the category list with the new selection
            for ( int i = 0; i < strCategoryIds.length; i++ )
            {
                int nCategoryId = Integer.parseInt( strCategoryIds[i] );
                NewsletterDocumentHome.associateNewsLetterDocumentList( newsletterSection.getId( ), nCategoryId,
                        getNewsletterDocumentPlugin( ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewsletterSection( NewsletterSection newsletterSection, AdminUser user, Locale locale )
    {
        NewsletterDocumentSection section = new NewsletterDocumentSection( );
        section.setId( newsletterSection.getId( ) );
        section.setIdTemplate( 0 );
        NewsletterDocumentHome.createDocumentSection( section, getNewsletterDocumentPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNewsletterSection( int nNewsletterSectionId )
    {
        // removes relationship between the newsletter and document list
        NewsletterDocumentHome.removeNewsLetterDocumentList( nNewsletterSectionId, getNewsletterDocumentPlugin( ) );

        // Remove the newsletter document section
        NewsletterDocumentHome.deleteDocumentSection( nNewsletterSectionId, getNewsletterDocumentPlugin( ) );
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

    private Plugin getNewsletterDocumentPlugin( )
    {
        if ( _newsletterDocumentPlugin == null )
        {
            _newsletterDocumentPlugin = PluginService.getPlugin( NewsletterDocumentPlugin.PLUGIN_NAME );
        }
        return _newsletterDocumentPlugin;
    }

    /**
     * Get the newsletter section service instance of this service
     * @return The newsletter section service of this service
     */
    private NewsletterSectionService getNewsletterSectionService( )
    {
        if ( _newsletterSectionService == null )
        {
            _newsletterSectionService = NewsletterSectionService.getService( );
        }
        return _newsletterSectionService;
    }

    /**
     * Get the newsletter section service instance of this service
     * @return The newsletter section service of this service
     */
    private Plugin getNewsletterPlugin( )
    {
        if ( _newsletterPlugin == null )
        {
            _newsletterPlugin = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );
        }
        return _newsletterPlugin;
    }
}
