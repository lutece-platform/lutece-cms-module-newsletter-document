package fr.paris.lutece.plugins.newsletter.modules.document.util;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.newsletter.util.NewsLetterConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class NewsletterDocumentUtils
{
    private static final String MARK_VIRTUAL_HOSTS = "virtual_hosts";
    private static final String PROPERTY_VIRTUAL_HOST = "virtualHost.";
    private static final String SUFFIX_BASE_URL = ".baseUrl";

    /**
     * Fills a given document template with the document data
     * 
     * @return the html code corresponding to the document data
     * @param strBaseUrl The base url of the portal
     * @param strTemplatePath The path of the template file
     * @param document the object gathering the document data
     * @param nPortletId the portlet id
     * @param plugin the plugin needed to retrieve properties
     * @param locale the locale used to build the template
     */
    public static String fillTemplateWithDocumentInfos( String strTemplatePath, Document document, int nPortletId,
            Plugin plugin, Locale locale, String strBaseUrl )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( NewsLetterConstants.MARK_DOCUMENT, document );

        try
        {
            if ( AppPathService.getAvailableVirtualHosts( ) != null )
            {
                ReferenceList hostKeysList = AppPathService.getAvailableVirtualHosts( );
                ReferenceList list = new ReferenceList( );

                for ( int i = 0; i < hostKeysList.size( ); i++ )
                {
                    list.addItem(
                            hostKeysList.get( i ).getName( ),
                            AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST + hostKeysList.get( i ).getCode( )
                                    + SUFFIX_BASE_URL ) );
                }

                model.put( MARK_VIRTUAL_HOSTS, list );
            }
        }
        catch ( NullPointerException e )
        {
            AppLogService.debug( e );
        }

        model.put( NewsLetterConstants.MARK_BASE_URL, strBaseUrl );
        model.put( NewsLetterConstants.MARK_DOCUMENT_THUMBNAIL, document.getThumbnail( ) );
        model.put( NewsLetterConstants.MARK_DOCUMENT_PORTLET_ID, nPortletId );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplatePath, locale, model );

        return template.getHtml( );
    }

    /**
     * Rewrite relatives url to absolutes urls
     * 
     * @param strContent The content to analyze
     * @param strBaseUrl The base url
     * @return The converted content
     */
    public static String rewriteUrls( String strContent, String strBaseUrl )
    {
        HtmlDocumentNewsletter doc = new HtmlDocumentNewsletter( strContent, strBaseUrl );
        doc.convertAllRelativesUrls( HtmlDocumentNewsletter.ELEMENT_IMG );
        doc.convertAllRelativesUrls( HtmlDocumentNewsletter.ELEMENT_A );
        doc.convertAllRelativesUrls( HtmlDocumentNewsletter.ELEMENT_FORM );
        doc.convertAllRelativesUrls( HtmlDocumentNewsletter.ELEMENT_CSS );
        doc.convertAllRelativesUrls( HtmlDocumentNewsletter.ELEMENT_JAVASCRIPT );

        return doc.getContent( );
    }

    /**
     * Rewrite secured omg urls to absolutes urls
     * 
     * @param strContent The content to analyze
     * @param strBaseUrl The base url
     * @return The converted content
     */
    public static String rewriteImgUrls( String strContent, String strBaseUrl, String strUnsecuredBaseUrl,
            String strUnsecuredFolderPath, String strUnsecuredFolder )
    {
        HtmlDocumentNewsletter doc = new HtmlDocumentNewsletter( strContent, strBaseUrl );
        doc.convertUrlsToUnsecuredUrls( HtmlDocumentNewsletter.ELEMENT_IMG, strUnsecuredBaseUrl,
                strUnsecuredFolderPath, strUnsecuredFolder );
        doc.convertUrlsToUnsecuredUrls( HtmlDocumentNewsletter.ELEMENT_A, strUnsecuredBaseUrl, strUnsecuredFolderPath,
                strUnsecuredFolder );

        return doc.getContent( );
    }
}
