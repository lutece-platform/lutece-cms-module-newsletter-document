package fr.paris.lutece.plugins.newsletter.modules.document.service;

import fr.paris.lutece.plugins.newsletter.business.NewsLetter;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterHome;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplate;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplateHome;
import fr.paris.lutece.plugins.newsletter.business.section.NewsletterSection;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocument;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocumentHome;
import fr.paris.lutece.plugins.newsletter.service.NewsletterPlugin;
import fr.paris.lutece.plugins.newsletter.service.NewsletterService;
import fr.paris.lutece.plugins.newsletter.service.section.INewsletterSectionService;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;
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
    private static final String PARAMETER_TEMPLATE_ID = "template_id";

    // PROPERTIES
    private static final String CONSTANT_UNCATEGORIZED_DOCUMENTS_KEY = "-1";

    // MESSAGES AND LABELS
    private static final String LABEL_MODIFY_UNCATEGORIZED_DOCUMENTS = "module.newsletter.document.modify_document_section.uncategorizedDocuments.label";
    private static final String MESSAGE_NEWSLETTER_DOCUMENT_SECTION_TYPE_NAME = "module.newsletter.document.sectionType.name";

    // MARKS
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_TEMPLATES_LIST = "templates_list";
    private static final String MARK_NEWSLETTER_DOCUMENT = "newsletterDocument";
    private static final String MARK_IMG_PATH = "img_path";

    // TEMPLATES
    private static final String TEMPLATE_MODIFY_NEWSLETTER_DOCUMENT_SECTION_CONFIG = "admin/plugins/newsletter/modules/document/modify_newsletter_document_section_config.html";

    private Plugin _newsletterDocumentPlugin;
    private Plugin _newsletterPlugin;
    private NewsletterService _newsletterService;

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

        // We get the categories associated with the section
        int[] arrayCategoryListIds = NewsletterDocumentHome.findNewsletterCategoryIds( newsletterSection.getId( ),
                getNewsletterDocumentPlugin( ) );

        // We get the list of categories avaiable for this section type
        ReferenceList listCategoryList = NewsletterDocumentHome.getAllCategories( user );
        listCategoryList.addItem( CONSTANT_UNCATEGORIZED_DOCUMENTS_KEY,
                I18nService.getLocalizedString( LABEL_MODIFY_UNCATEGORIZED_DOCUMENTS, locale ) );
        String[] strSelectedCategoryList = new String[arrayCategoryListIds.length];

        for ( int i = 0; i < arrayCategoryListIds.length; i++ )
        {
            strSelectedCategoryList[i] = String.valueOf( arrayCategoryListIds[i] );
        }
        // We check categories associated with this section
        listCategoryList.checkItems( strSelectedCategoryList );

        NewsletterDocument newsletterDocument = NewsletterDocumentHome.findByPrimaryKey( newsletterSection.getId( ),
                getNewsletterDocumentPlugin( ) );

        String strPathImageTemplate = getNewsletterService( ).getImageFolderPath( strBaseUrl );

        model.put( MARK_CATEGORY_LIST, listCategoryList );
        model.put( MARK_NEWSLETTER_DOCUMENT, newsletterDocument );
        model.put( MARK_TEMPLATES_LIST, NewsLetterTemplateHome.getTemplatesCollectionByType(
                NEWSLETTER_DOCUMENT_SECTION_TYPE, getNewsletterPlugin( ) ) );
        model.put( MARK_IMG_PATH, strPathImageTemplate );

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
        String[] strCategoryIds = mapParameters.get( PARAMETER_CATEGORY_LIST_ID );

        NewsletterDocumentHome
                .removeNewsLetterDocumentList( newsletterSection.getId( ), getNewsletterDocumentPlugin( ) );

        if ( ( strCategoryIds != null ) )
        {
            // recreate the category list with the new selection
            for ( int i = 0; i < strCategoryIds.length; i++ )
            {
                int nCategoryId = Integer.parseInt( strCategoryIds[i] );
                NewsletterDocumentHome.associateNewsLetterDocumentList( newsletterSection.getId( ), nCategoryId,
                        getNewsletterDocumentPlugin( ) );
            }
        }

        String strTemplateId = NewsletterUtils.getStringFromStringArray( mapParameters.get( PARAMETER_TEMPLATE_ID ) );
        if ( StringUtils.isNumeric( strTemplateId ) )
        {
            NewsletterDocument newsletterDocument = NewsletterDocumentHome.findByPrimaryKey(
                    newsletterSection.getId( ), getNewsletterDocumentPlugin( ) );
            newsletterDocument.setIdTemplate( Integer.parseInt( strTemplateId ) );
            NewsletterDocumentHome.updateDocumentSection( newsletterDocument, getNewsletterDocumentPlugin( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewsletterSection( NewsletterSection newsletterSection, AdminUser user, Locale locale )
    {
        NewsletterDocument section = new NewsletterDocument( );
        section.setId( newsletterSection.getId( ) );

        List<NewsLetterTemplate> listTemplates = NewsLetterTemplateHome.getTemplatesCollectionByType(
                NEWSLETTER_DOCUMENT_SECTION_TYPE, getNewsletterPlugin( ) );
        if ( listTemplates != null && listTemplates.size( ) > 0 )
        {
            // We default to the first template
            section.setIdTemplate( listTemplates.get( 0 ).getId( ) );
        }
        else
        {
            section.setIdTemplate( 0 );
        }
        NewsletterDocumentHome.createDocumentSection( section, getNewsletterDocumentPlugin( ) );
        NewsletterDocumentHome.associateNewsLetterDocumentList( newsletterSection.getId( ),
                Integer.parseInt( CONSTANT_UNCATEGORIZED_DOCUMENTS_KEY ), getNewsletterDocumentPlugin( ) );
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
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( newsletterSection.getIdNewsletter( ),
                getNewsletterPlugin( ) );
        NewsletterDocument newsletterDocument = NewsletterDocumentHome.findByPrimaryKey( newsletterSection.getId( ),
                getNewsletterDocumentPlugin( ) );
        String strContent = NewsletterDocumentService.getInstance( ).generateDocumentsList(
                newsletterDocument.getId( ), newsletterDocument.getIdTemplate( ), newsletter.getDateLastSending( ),
                AppPathService.getBaseUrl( ), user, locale );

        return strContent;
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
     * Get the newsletter service instance of this service
     * @return The newsletter service of this service
     */
    private NewsletterService getNewsletterService( )
    {
        if ( _newsletterService == null )
        {
            _newsletterService = NewsletterService.getService( );
        }
        return _newsletterService;
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
