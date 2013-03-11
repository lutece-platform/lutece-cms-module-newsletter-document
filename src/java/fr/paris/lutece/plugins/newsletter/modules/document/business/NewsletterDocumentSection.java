package fr.paris.lutece.plugins.newsletter.modules.document.business;

public class NewsletterDocumentSection
{
    private int _nId;
    private int _nIdTemplate;

    /**
     * Get the id of the section
     * @return The id of the section
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Set the id of the section
     * @param nId The id of the section
     */
    public void setId( int nId )
    {
        this._nId = nId;
    }

    /**
     * Get the id of the template to apply to documents of this section
     * @return The id of the template to apply to documents of this section
     */
    public int getIdTemplate( )
    {
        return _nIdTemplate;
    }

    /**
     * Set the id of the template to apply to documents of this section
     * @param nIdTemplate The id of the template to apply to documents of this
     *            section
     */
    public void setIdTemplate( int nIdTemplate )
    {
        this._nIdTemplate = nIdTemplate;
    }
}
