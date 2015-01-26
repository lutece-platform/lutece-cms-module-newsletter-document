package fr.paris.lutece.plugins.newsletter.modules.document.service;

import fr.paris.lutece.plugins.document.service.DocumentPlugin;
import fr.paris.lutece.plugins.newsletter.business.NewsLetter;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterHome;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplate;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplateHome;
import fr.paris.lutece.plugins.newsletter.business.topic.NewsletterTopic;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocument;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocumentHome;
import fr.paris.lutece.plugins.newsletter.service.NewsletterPlugin;
import fr.paris.lutece.plugins.newsletter.service.NewsletterService;
import fr.paris.lutece.plugins.newsletter.service.topic.INewsletterTopicService;
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
 * The newsletter document topic service
 */
public class NewsletterDocumentTopicService implements INewsletterTopicService
{
    /**
     * Newsletter document topic type
     */
    public static final String NEWSLETTER_DOCUMENT_TOPIC_TYPE = "NEWSLETTER_DOCUMENT";

    // PARAMETERS
    private static final String PARAMETER_CATEGORY_LIST_ID = "category_list_id";
    private static final String PARAMETER_TEMPLATE_ID = "template_id";
    private static final String PARAMETER_PORTLETS_ID = "portlets_id";

    // PROPERTIES
    private static final String CONSTANT_UNCATEGORIZED_DOCUMENTS_KEY = "-1";

    // MESSAGES AND LABELS
    private static final String LABEL_MODIFY_UNCATEGORIZED_DOCUMENTS = "module.newsletter.document.modify_document_topic.uncategorizedDocuments.label";
    private static final String MESSAGE_NEWSLETTER_DOCUMENT_TOPIC_TYPE_NAME = "module.newsletter.document.topicType.name";

    // MARKS
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_DOCUMENT_LIST_PORTLETS = "document_list_portlets";
    private static final String MARK_TEMPLATES_LIST = "templates_list";
    private static final String MARK_NEWSLETTER_DOCUMENT = "newsletterDocument";
    private static final String MARK_IMG_PATH = "img_path";
    private static final String MARK_USE_CATEGORIES = "use_categories";

    // TEMPLATES
    private static final String TEMPLATE_MODIFY_NEWSLETTER_DOCUMENT_TOPIC_CONFIG = "admin/plugins/newsletter/modules/document/modify_newsletter_document_topic_config.html";

    private Plugin _newsletterDocumentPlugin;
    private Plugin _newsletterPlugin;
    private NewsletterService _newsletterService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewsletterTopicTypeCode( )
    {
        return NEWSLETTER_DOCUMENT_TOPIC_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewsletterTopicTypeName( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_NEWSLETTER_DOCUMENT_TOPIC_TYPE_NAME, locale );
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
    public String getConfigurationPage( NewsletterTopic newsletterTopic, String strBaseUrl, AdminUser user,
            Locale locale )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        // We get the categories associated with the topic
        int[] arrayCategoryListIds = NewsletterDocumentHome.findNewsletterCategoryIds( newsletterTopic.getId( ),
                getNewsletterDocumentPlugin( ) );

        // We get the list of categories available for this topic type
        ReferenceList listCategoryList = NewsletterDocumentHome.getAllCategories( user );
        listCategoryList.addItem( CONSTANT_UNCATEGORIZED_DOCUMENTS_KEY,
                I18nService.getLocalizedString( LABEL_MODIFY_UNCATEGORIZED_DOCUMENTS, locale ) );
        String[] strSelectedCategoryList = new String[arrayCategoryListIds.length];

        for ( int i = 0; i < arrayCategoryListIds.length; i++ )
        {
            strSelectedCategoryList[i] = String.valueOf( arrayCategoryListIds[i] );
        }
        // We check categories associated with this topic
        listCategoryList.checkItems( strSelectedCategoryList );

        // We get the list of document list portlets containing published documents
        ReferenceList listDocumentPortlets = NewsletterDocumentHome.getDocumentListPortlets( getDocumentPlugin( ) );
        int[] arrayPortletIds = NewsletterDocumentHome.findNewsletterPortletsIds( newsletterTopic.getId( ),
                getNewsletterDocumentPlugin( ) );
        String[] strSelectedPortlets = new String[arrayPortletIds.length];

        for ( int i = 0; i < arrayPortletIds.length; i++ )
        {
            strSelectedPortlets[i] = String.valueOf( arrayPortletIds[i] );
        }
        // We check portlets associated with this topic
        listDocumentPortlets.checkItems( strSelectedPortlets );

        NewsletterDocument newsletterDocument = NewsletterDocumentHome.findByPrimaryKey( newsletterTopic.getId( ),
                getNewsletterDocumentPlugin( ) );

        String strPathImageTemplate = getNewsletterService( ).getImageFolderPath( strBaseUrl );

        model.put( MARK_CATEGORY_LIST, listCategoryList );
        model.put( MARK_DOCUMENT_LIST_PORTLETS, listDocumentPortlets );
        model.put( MARK_NEWSLETTER_DOCUMENT, newsletterDocument );
        model.put( MARK_TEMPLATES_LIST, NewsLetterTemplateHome.getTemplatesCollectionByType(
                NEWSLETTER_DOCUMENT_TOPIC_TYPE, getNewsletterPlugin( ) ) );
        model.put( MARK_IMG_PATH, strPathImageTemplate );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_NEWSLETTER_DOCUMENT_TOPIC_CONFIG,
                locale, model );
        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration( Map<String, String[]> mapParameters, NewsletterTopic newsletterTopic,
            AdminUser user, Locale locale )
    {
        String[] strCategoryIds = mapParameters.get( PARAMETER_CATEGORY_LIST_ID );

        NewsletterDocumentHome.removeNewsLetterDocumentCategories( newsletterTopic.getId( ),
                getNewsletterDocumentPlugin( ) );

        if ( ( strCategoryIds != null ) )
        {
            // recreate the category list with the new selection
            for ( int i = 0; i < strCategoryIds.length; i++ )
            {
                int nCategoryId = Integer.parseInt( strCategoryIds[i] );
                NewsletterDocumentHome.associateNewsLetterDocumentCategory( newsletterTopic.getId( ), nCategoryId,
                        getNewsletterDocumentPlugin( ) );
            }
        }

        NewsletterDocumentHome
                .removeNewsLetterDocumentPortlet( newsletterTopic.getId( ), getNewsletterDocumentPlugin( ) );
        String[] strPortletsIds = mapParameters.get( PARAMETER_PORTLETS_ID );
        if ( ( strPortletsIds != null ) )
        {
            // recreate the category list with the new selection
            for ( int i = 0; i < strPortletsIds.length; i++ )
            {
                int nPortletId = Integer.parseInt( strPortletsIds[i] );
                NewsletterDocumentHome.associateNewsLetterDocumentPortlet( newsletterTopic.getId( ), nPortletId,
                        getNewsletterDocumentPlugin( ) );
            }
        }

        String strTemplateId = NewsletterUtils.getStringFromStringArray( mapParameters.get( PARAMETER_TEMPLATE_ID ) );
        if ( StringUtils.isNumeric( strTemplateId ) )
        {
            NewsletterDocument newsletterDocument = NewsletterDocumentHome.findByPrimaryKey( newsletterTopic.getId( ),
                    getNewsletterDocumentPlugin( ) );

            String strUseCategories = NewsletterUtils
                    .getStringFromStringArray( mapParameters.get( MARK_USE_CATEGORIES ) );
            boolean bUseCategories = Boolean.parseBoolean( strUseCategories );
            newsletterDocument.setUseDocumentCategories( bUseCategories );

            newsletterDocument.setIdTemplate( Integer.parseInt( strTemplateId ) );
            NewsletterDocumentHome.updateDocumentTopic( newsletterDocument, getNewsletterDocumentPlugin( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewsletterTopic( NewsletterTopic newsletterTopic, AdminUser user, Locale locale )
    {
        NewsletterDocument topic = new NewsletterDocument( );
        topic.setId( newsletterTopic.getId( ) );

        List<NewsLetterTemplate> listTemplates = NewsLetterTemplateHome.getTemplatesCollectionByType(
                NEWSLETTER_DOCUMENT_TOPIC_TYPE, getNewsletterPlugin( ) );
        if ( listTemplates != null && listTemplates.size( ) > 0 )
        {
            // We default to the first template
            topic.setIdTemplate( listTemplates.get( 0 ).getId( ) );
        }
        else
        {
            topic.setIdTemplate( 0 );
        }
        topic.setUseDocumentCategories( true );
        NewsletterDocumentHome.createDocumentTopic( topic, getNewsletterDocumentPlugin( ) );
        NewsletterDocumentHome.associateNewsLetterDocumentCategory( newsletterTopic.getId( ),
                Integer.parseInt( CONSTANT_UNCATEGORIZED_DOCUMENTS_KEY ), getNewsletterDocumentPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNewsletterTopic( int nNewsletterTopicId )
    {
        // removes relationship between the topic and document list
        NewsletterDocumentHome.removeNewsLetterDocumentCategories( nNewsletterTopicId, getNewsletterDocumentPlugin( ) );

        // removes relationship between the topic and portlets
        NewsletterDocumentHome.removeNewsLetterDocumentPortlet( nNewsletterTopicId, getNewsletterDocumentPlugin( ) );

        // Remove the newsletter document topic
        NewsletterDocumentHome.deleteDocumentTopic( nNewsletterTopicId, getNewsletterDocumentPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtmlContent( NewsletterTopic newsletterTopic, AdminUser user, Locale locale )
    {
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( newsletterTopic.getIdNewsletter( ),
                getNewsletterPlugin( ) );
        NewsletterDocument newsletterDocument = NewsletterDocumentHome.findByPrimaryKey( newsletterTopic.getId( ),
                getNewsletterDocumentPlugin( ) );
        String strContent = NewsletterDocumentService.getInstance( ).generateDocumentsList( newsletterDocument,
                newsletterDocument.getIdTemplate( ), newsletter.getDateLastSending( ), AppPathService.getProdUrl(""),
                user, locale );

        return strContent;
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
     * Get the newsletter document plugin
     * @return The newsletter document plugin
     */
    private Plugin getNewsletterDocumentPlugin( )
    {
        if ( _newsletterDocumentPlugin == null )
        {
            _newsletterDocumentPlugin = PluginService.getPlugin( NewsletterDocumentPlugin.PLUGIN_NAME );
        }
        return _newsletterDocumentPlugin;
    }

    /**
     * Get the newsletter plugin
     * @return The newsletter plugin
     */
    private Plugin getNewsletterPlugin( )
    {
        if ( _newsletterPlugin == null )
        {
            _newsletterPlugin = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );
        }
        return _newsletterPlugin;
    }

    /**
     * Get the document plugin
     * @return The document plugin
     */
    private Plugin getDocumentPlugin( )
    {
        return PluginService.getPlugin( DocumentPlugin.PLUGIN_NAME );
    }
}
