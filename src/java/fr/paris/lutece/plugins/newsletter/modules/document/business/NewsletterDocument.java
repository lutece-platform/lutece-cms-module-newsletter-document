/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
