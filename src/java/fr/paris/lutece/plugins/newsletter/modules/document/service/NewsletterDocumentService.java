package fr.paris.lutece.plugins.newsletter.modules.document.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentFilter;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.service.publishing.PublishingService;
import fr.paris.lutece.plugins.newsletter.modules.document.business.NewsletterDocumentHome;
import fr.paris.lutece.plugins.newsletter.modules.document.util.NewsletterDocumentUtils;
import fr.paris.lutece.plugins.newsletter.service.NewsletterPlugin;
import fr.paris.lutece.plugins.newsletter.service.NewsletterService;
import fr.paris.lutece.plugins.newsletter.util.NewsLetterConstants;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portlet.PortletService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Newsletter document service. This class implements the singleton design
 * pattern.
 */
public class NewsletterDocumentService
{
    private static final String FULLSTOP = ".";

    private static final String MARK_IMG_PATH = "img_path";
    private static final String MARK_DOCUMENT_PORTLETS_COLLEC = "portlets_collec";
    private static final String MARK_DOCUMENT = "document";

    private static NewsletterDocumentService _singleton = new NewsletterDocumentService( );
    private NewsletterService _newsletterService = NewsletterService.getService( );

    /**
     * Returns the instance of the singleton
     * @return The instance of the singleton
     */
    public static NewsletterDocumentService getInstance( )
    {
        return _singleton;
    }

    /**
     * Copy specified document's type file into a given folder
     * @param document the document
     * @param strFileType the file type
     * @param strDestFolderPath the destination folder
     * @return name of the copy file or null if there is no copied file
     */
    public String copyFileFromDocument( Document document, String strFileType, String strDestFolderPath )
    {
        List<DocumentAttribute> listDocumentAttribute = document.getAttributes( );
        String strFileName = null;

        for ( DocumentAttribute documentAttribute : listDocumentAttribute )
        {
            // if binary or is a strFileType
            if ( documentAttribute.isBinary( ) && documentAttribute.getValueContentType( ).contains( strFileType ) )
            {
                byte[] tabByte = documentAttribute.getBinaryValue( );
                // fileName is composed from documentID+ documentAttributeId + documentAttributeOrder + "." + fileExtension
                strFileName = NewsletterDocumentUtils.formatInteger( document.getId( ), 5 )
                        + NewsletterDocumentUtils.formatInteger( documentAttribute.getId( ), 5 )
                        + NewsletterDocumentUtils.formatInteger( documentAttribute.getAttributeOrder( ), 5 ) + FULLSTOP
                        + StringUtils.substringAfterLast( documentAttribute.getTextValue( ), FULLSTOP );

                FileOutputStream fos = null;

                try
                {
                    new File( strDestFolderPath ).mkdir( );

                    File file = new File( strDestFolderPath + strFileName );
                    fos = new FileOutputStream( file );
                    IOUtils.write( tabByte, fos );
                }
                catch ( IOException e )
                {
                    AppLogService.error( e );
                }
                catch ( Exception e )
                {
                    AppLogService.error( e );
                }
                finally
                {
                    IOUtils.closeQuietly( fos );
                }
            }
        }

        return strFileName;
    }

    /**
     * Generate the html code for documents corresponding to the documents
     * associated with the section and to a given publishing date
     * @param nSectionId the section to generate
     * @param nTemplateId the document id to use
     * @param datePublishing minimum date of publishing of documents. Documents
     *            published before this date will not be considered
     * @param strBaseUrl the url of the portal
     * @param user The current admin user
     * @param locale The locale
     * @return the html code for the document list of null if no document
     *         template available
     */
    public String generateDocumentsList( int nSectionId, int nTemplateId, Timestamp datePublishing, String strBaseUrl,
            AdminUser user, Locale locale )
    {
        Plugin pluginNewsLetter = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );
        int[] arrayCategoriesIds = NewsletterDocumentHome.findNewsletterCategoryIds( nSectionId, pluginNewsLetter );
        String strTemplatePath = NewsletterUtils.getHtmlTemplatePath( nTemplateId, pluginNewsLetter );

        if ( strTemplatePath == null )
        {
            return null;
        }

        DocumentFilter documentFilter = new DocumentFilter( );

        if ( ( arrayCategoriesIds.length > 0 ) && ( arrayCategoriesIds[0] > -1 ) )
        {
            documentFilter.setCategoriesId( arrayCategoriesIds );
        }

        Collection<Document> listDocuments = PublishingService.getInstance( ).getPublishedDocumentsSinceDate(
                datePublishing, documentFilter, locale );

        StringBuffer sbDocumentLists = new StringBuffer( );

        // get html from templates
        for ( Document document : listDocuments )
        {
            String strContent = fillTemplateWithDocumentInfos( strTemplatePath, document, locale, strBaseUrl, user );
            sbDocumentLists.append( strContent );
        }

        return sbDocumentLists.toString( );
    }

    /**
     * Fills a given document template with the document data
     * @return the html code corresponding to the document data
     * @param strBaseUrl The base url of the portal
     * @param strTemplatePath The path of the template file
     * @param document the object gathering the document data
     * @param locale the locale used to build the template
     * @param user The current user
     */
    public String fillTemplateWithDocumentInfos( String strTemplatePath, Document document, Locale locale,
            String strBaseUrl, AdminUser user )
    {
        Collection<Portlet> porletCollec = PublishingService.getInstance( ).getPortletsByDocumentId(
                Integer.toString( document.getId( ) ) );
        porletCollec = PortletService.getInstance( ).getAuthorizedPortletCollection( porletCollec, user );
        //the document insert in the buffer must be publish in a authorized portlet
        if ( porletCollec.size( ) > 0 )
        {
            //the document insert in the buffer must be publish in a authorized portlet
            Map<String, Object> model = new HashMap<String, Object>( );
            model.put( MARK_DOCUMENT, document );

            // if noSecuredImg is true, it will copy all document's picture in a no secured folder
            if ( _newsletterService.useUnsecuredImages( ) )
            {
                String strImgFolder = _newsletterService.getUnsecuredImagefolder( );
                String pictureName = NewsletterDocumentService.getInstance( ).copyFileFromDocument( document,
                        NewsLetterConstants.CONSTANT_IMG_FILE_TYPE,
                        _newsletterService.getUnsecuredFolderPath( ) + strImgFolder );
                if ( pictureName != null )
                {
                    model.put( MARK_IMG_PATH, _newsletterService.getUnsecuredWebappUrl( ) + strImgFolder + pictureName );
                }
            }

            model.put( NewsLetterConstants.MARK_BASE_URL, strBaseUrl );
            model.put( MARK_DOCUMENT_PORTLETS_COLLEC, porletCollec );

            HtmlTemplate template = AppTemplateService.getTemplate( strTemplatePath, locale, model );

            return template.getHtml( );
        }
        return StringUtils.EMPTY;
    }
}
