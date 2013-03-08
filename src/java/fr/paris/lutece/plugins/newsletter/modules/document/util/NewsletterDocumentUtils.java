package fr.paris.lutece.plugins.newsletter.modules.document.util;



/**
 * @author vbroussard
 * 
 */
public class NewsletterDocumentUtils
{

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
     * @param strUnsecuredBaseUrl The unsecured base URL
     * @param strUnsecuredFolderPath The unsecured folder path
     * @param strUnsecuredFolder The unsecured folder
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
