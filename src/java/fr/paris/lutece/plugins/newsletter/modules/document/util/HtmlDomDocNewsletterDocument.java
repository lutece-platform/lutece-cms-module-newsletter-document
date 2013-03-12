package fr.paris.lutece.plugins.newsletter.modules.document.util;

import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.newsletter.modules.document.service.NewsletterDocumentService;
import fr.paris.lutece.plugins.newsletter.util.HtmlDomDocNewsletter;
import fr.paris.lutece.plugins.newsletter.util.NewsLetterConstants;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Dom document parser for newsletter document.
 */
public class HtmlDomDocNewsletterDocument extends HtmlDomDocNewsletter
{
    private static final String CONSTANT_IMG = "img";
    private static final String CONSTANT_A = "a";
    private static final String CONSTANT_SUBSTRING_BEGIN = "document?id=";
    private static final String CONSTANT_SUBSTRING_END = "&";

    private static NewsletterDocumentService _newsletterDocumentService = NewsletterDocumentService.getInstance( );

    /**
     * Instantiates an HtmlDocument after having built the DOM tree.
     * @param strHtml The Html code to be parsed.
     * @param strBaseUrl The Base url used to retrieve urls.
     */
    public HtmlDomDocNewsletterDocument( String strHtml, String strBaseUrl )
    {
        super( strHtml, strBaseUrl );
    }

    /**
     * Get the urls of all html elements specified by elementType and convert
     * its to unsecured urls
     * and copy this elements into an unsecured folder
     * @param elementType the type of element to get
     * @param strUnsecuredBaseUrl The unsecured base URL
     * @param strUnsecuredFolderPath The unsecured folder path
     * @param strUnsecuredFolder The unsecured folder
     */
    public void convertUrlsToUnsecuredUrls( ElementUrl elementType, String strUnsecuredBaseUrl,
            String strUnsecuredFolderPath, String strUnsecuredFolder )
    {
        NodeList nodes = getDomDocument( ).getElementsByTagName( elementType.getTagName( ) );

        for ( int i = 0; i < nodes.getLength( ); i++ )
        {
            Node node = nodes.item( i );
            NamedNodeMap attributes = node.getAttributes( );

            // Test if the element matches the required attribute
            if ( elementType.getTestedAttributeName( ) != null )
            {
                String strRel = attributes.getNamedItem( elementType.getTestedAttributeName( ) ).getNodeValue( );

                if ( !elementType.getTestedAttributeValue( ).equals( strRel ) )
                {
                    continue;
                }
            }

            Node nodeAttribute = attributes.getNamedItem( elementType.getAttributeName( ) );

            if ( nodeAttribute != null )
            {
                String strSrc = nodeAttribute.getNodeValue( );

                if ( strSrc.contains( CONSTANT_SUBSTRING_BEGIN )
                        && !strSrc.contains( strUnsecuredBaseUrl + strUnsecuredFolder ) )
                {
                    String strDocumentId = StringUtils.substringBetween( strSrc, CONSTANT_SUBSTRING_BEGIN,
                            CONSTANT_SUBSTRING_END );
                    fr.paris.lutece.plugins.document.business.Document document = DocumentHome
                            .findByPrimaryKey( Integer.valueOf( strDocumentId ) );

                    String strFileName = StringUtils.EMPTY;

                    if ( elementType.getTagName( ).equals( CONSTANT_IMG ) )
                    {
                        strFileName = _newsletterDocumentService
                                .copyFileFromDocument( document, NewsLetterConstants.CONSTANT_IMG_FILE_TYPE,
                                        strUnsecuredFolderPath + strUnsecuredFolder );
                    }
                    else
                    {
                        if ( elementType.getTagName( ).equals( CONSTANT_A ) )
                        {
                            strFileName = _newsletterDocumentService.copyFileFromDocument( document,
                                    NewsLetterConstants.CONSTANT_PDF_FILE_TYPE, strUnsecuredFolderPath
                                            + strUnsecuredFolder );
                        }
                    }

                    nodeAttribute.setNodeValue( strUnsecuredBaseUrl + strUnsecuredFolder + strFileName );
                }
            }
        }
    }
}
