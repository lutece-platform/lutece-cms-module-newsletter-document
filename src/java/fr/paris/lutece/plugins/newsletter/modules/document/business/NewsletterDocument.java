package fr.paris.lutece.plugins.newsletter.modules.document.business;

/**
 * Newsletter document topic class
 */
public class NewsletterDocument
{
    private int _nId;
    private int _nIdTemplate;
    private boolean _bUseDocumentCategories;

    /**
     * Get the id of the topic
     * @return The id of the topic
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Set the id of the topic
     * @param nId The id of the topic
     */
    public void setId( int nId )
    {
        this._nId = nId;
    }

    /**
     * Get the id of the template to apply to documents of this topic
     * @return The id of the template to apply to documents of this topic
     */
    public int getIdTemplate( )
    {
        return _nIdTemplate;
    }

    /**
     * Set the id of the template to apply to documents of this topic
     * @param nIdTemplate The id of the template to apply to documents of this
     *            topic
     */
    public void setIdTemplate( int nIdTemplate )
    {
        this._nIdTemplate = nIdTemplate;
    }

    /**
     * Check if this topic use document categories to get the document list, or
     * document list portlets
     * @return True if this topic use document categories to get the document
     *         list, false if it use portlets.
     */
    public boolean getUseDocumentCategories( )
    {
        return _bUseDocumentCategories;
    }

    /**
     * Set the flag to indicates if this topic use document categories to get
     * the document list, or document list portlets
     * @param bUseDocumentCategories True if this topic use document categories
     *            to get the document list, false if it use portlets.
     */
    public void setUseDocumentCategories( boolean bUseDocumentCategories )
    {
        this._bUseDocumentCategories = bUseDocumentCategories;
    }
}
