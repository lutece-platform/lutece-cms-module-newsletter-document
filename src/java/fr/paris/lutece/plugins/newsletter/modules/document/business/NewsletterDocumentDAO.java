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

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.publication.DocumentPublication;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * DAO implementation for newsletter document
 */
public class NewsletterDocumentDAO implements INewsletterDocumentDAO
{
    private static final String SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_TOPIC = " SELECT id_topic, id_template, use_categories FROM newsletter_document_topic WHERE id_topic = ? ";
    private static final String SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_TOPIC = " INSERT INTO newsletter_document_topic(id_topic, id_template, use_categories) VALUES (?,?,?) ";
    private static final String SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_TOPIC = " UPDATE newsletter_document_topic SET id_template = ?, use_categories = ? WHERE id_topic = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_TOPIC = " DELETE FROM newsletter_document_topic WHERE id_topic = ? ";

    private static final String SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_LIST_DOCUMENT = "SELECT DISTINCT a.id_document , a.code_document_type, a.date_creation , a.date_modification, a.title, a.document_summary FROM document a INNER JOIN document_published b ON a.id_document=b.id_document INNER JOIN core_portlet c ON b.id_portlet=c.id_portlet WHERE c.id_portlet_type='DOCUMENT_LIST_PORTLET' ";
    private static final String SQL_QUERY_DOCUMENT_TYPE_PORTLET = " SELECT DISTINCT id_portlet , name FROM core_portlet WHERE id_portlet_type='DOCUMENT_LIST_PORTLET'  ";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST = "INSERT INTO newsletter_document_category ( id_topic , id_category ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST = "DELETE FROM newsletter_document_category WHERE id_topic = ?";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS = "SELECT id_category FROM newsletter_document_category WHERE id_topic = ?";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_PORTLET = "INSERT INTO newsletter_document_portlet ( id_topic , id_portlet ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_PORTLET_IDS = " SELECT id_portlet FROM newsletter_document_portlet WHERE id_topic = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_PORTLET = "DELETE FROM newsletter_document_portlet WHERE id_topic = ?";
    private static final String SQL_QUERY_SELECT_BY_PORTLET_ID_AND_STATUS = " SELECT DISTINCT pub.id_document FROM document_published pub, document doc "
            + "WHERE doc.id_document=pub.id_document AND pub.status = ? AND doc.date_modification >= ? AND pub.id_portlet IN ";
    private static final String SQL_QUERY_FIND_TEMPLATE = " SELECT count(id_template) FROM newsletter_document_topic WHERE id_template = ? ";
    private static final String SQL_FILTER_DATE_MODIF = " a.date_modification >=? ";
    private static final String SQL_FILTER_ID_PORTLET = " c.id_portlet = ? ";

    private static final String CONSTANT_AND = " AND ";
    private static final String CONSTANT_OPEN_PARENTHESIS = "(";
    private static final String CONSTANT_CLOSE_PARENTHESIS = ")";
    private static final String CONSTANT_QUESTION_MARK = "?";
    private static final String CONSTANT_COMMA = ",";
    private static final String CONSTANT_ORDER_BY_DATE_MODIF = " ORDER BY a.date_modification DESC ";

    /**
     * {@inheritDoc}
     */
    @Override
    public NewsletterDocument findByPrimaryKey( int nIdTopic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, nIdTopic );
        daoUtil.executeQuery( );
        NewsletterDocument topic = null;
        if ( daoUtil.next( ) )
        {
            topic = new NewsletterDocument( );
            topic.setId( daoUtil.getInt( 1 ) );
            topic.setIdTemplate( daoUtil.getInt( 2 ) );
            topic.setUseDocumentCategories( daoUtil.getBoolean( 3 ) );
        }
        daoUtil.free( );
        return topic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDocumentTopic( NewsletterDocument topic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, topic.getIdTemplate( ) );
        daoUtil.setBoolean( 2, topic.getUseDocumentCategories( ) );
        daoUtil.setInt( 3, topic.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocumentTopic( int nIdTopic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, nIdTopic );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDocumentTopic( NewsletterDocument topic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, topic.getId( ) );
        daoUtil.setInt( 2, topic.getIdTemplate( ) );
        daoUtil.setBoolean( 3, topic.getUseDocumentCategories( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> selectDocumentsByDateAndCategory( int nPortletId, Timestamp dateLastSending,
            Plugin plugin )
    {
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_LIST_DOCUMENT );

        sbSql.append( CONSTANT_AND );
        sbSql.append( SQL_FILTER_DATE_MODIF );
        if ( nPortletId > 0 )
        {
            sbSql.append( CONSTANT_AND );
            sbSql.append( SQL_FILTER_ID_PORTLET );
        }
        sbSql.append( CONSTANT_ORDER_BY_DATE_MODIF );

        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
        int nIndex = 1;
        daoUtil.setTimestamp( nIndex++, dateLastSending );
        if ( nPortletId > 0 )
        {
            daoUtil.setInt( nIndex, nPortletId );
        }

        daoUtil.executeQuery( );

        List<Document> list = new ArrayList<Document>( );

        while ( daoUtil.next( ) )
        {
            Document document = new Document( );
            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setDateCreation( daoUtil.getTimestamp( 3 ) );
            document.setDateModification( daoUtil.getTimestamp( 4 ) );
            document.setTitle( daoUtil.getString( 5 ) );
            document.setSummary( daoUtil.getString( 6 ) );
            list.add( document );
        }

        daoUtil.free( );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList selectDocumentListPortlets( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DOCUMENT_TYPE_PORTLET );
        daoUtil.executeQuery( );

        ReferenceList list = new ReferenceList( );

        while ( daoUtil.next( ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateNewsLetterDocumentCategory( int nTopicId, int nCategoryId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.setInt( 2, nCategoryId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentCategories( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST, plugin );

        daoUtil.setInt( 1, nTopicId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] selectNewsletterCategoryIds( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.executeQuery( );

        List<Integer> list = new ArrayList<Integer>( );

        while ( daoUtil.next( ) )
        {
            int nResultId = daoUtil.getInt( 1 );
            list.add( Integer.valueOf( nResultId ) );
        }

        int[] nIdsArray = new int[list.size( )];

        for ( int i = 0; i < list.size( ); i++ )
        {
            Integer nId = list.get( i );
            nIdsArray[i] = nId.intValue( );
        }

        daoUtil.free( );

        return nIdsArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_PORTLET, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.setInt( 2, nPortletId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentPortlet( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_PORTLET, plugin );

        daoUtil.setInt( 1, nTopicId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] selectNewsletterPortletsIds( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_PORTLET_IDS, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.executeQuery( );

        List<Integer> list = new ArrayList<Integer>( );

        while ( daoUtil.next( ) )
        {
            int nResultId = daoUtil.getInt( 1 );
            list.add( Integer.valueOf( nResultId ) );
        }

        int[] nIdsArray = new int[list.size( )];

        for ( int i = 0; i < list.size( ); i++ )
        {
            Integer nId = list.get( i );
            nIdsArray[i] = nId.intValue( );
        }

        daoUtil.free( );

        return nIdsArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getPublishedDocumentsIdsListByPortletIds( int[] nPortletsIds, Date datePublishing,
            Plugin plugin )
    {
        List<Integer> listIds = new ArrayList<Integer>( );
        if ( nPortletsIds == null || nPortletsIds.length == 0 )
        {
            return listIds;
        }
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_BY_PORTLET_ID_AND_STATUS );
        sbSql.append( CONSTANT_OPEN_PARENTHESIS );

        for ( int i = 0; i < nPortletsIds.length; i++ )
        {
            sbSql.append( CONSTANT_QUESTION_MARK );
            if ( i + 1 < nPortletsIds.length )
            {
                sbSql.append( CONSTANT_COMMA );
            }
        }
        sbSql.append( CONSTANT_CLOSE_PARENTHESIS );

        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, DocumentPublication.STATUS_PUBLISHED );
        daoUtil.setTimestamp( nIndex++, new Timestamp( datePublishing.getTime( ) ) );
        for ( int nPortletId : nPortletsIds )
        {
            daoUtil.setInt( nIndex++, nPortletId );
        }
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listIds.add( daoUtil.getInt( 1 ) );
        }
        daoUtil.free( );
        return listIds;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TEMPLATE, plugin );
        daoUtil.setInt( 1, nIdNewsletterTemplate );
        daoUtil.executeQuery( );

        boolean bRes = false;
        if ( daoUtil.next( ) )
        {
            bRes = daoUtil.getInt( 1 ) > 0;
        }

        daoUtil.free( );

        return bRes;
    }
}
