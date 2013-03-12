package fr.paris.lutece.plugins.newsletter.modules.document.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * DAO implementation for newsletter document
 */
public class NewsletterDocumentDAO implements INewsletterDocumentDAO
{
    private static final String SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_SECTION = " SELECT id_section, id_template FROM newsletter_document_section WHERE id_section = ? ";
    private static final String SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_SECTION = " INSERT INTO newsletter_document_section(id_section, id_template) VALUES (?,?) ";
    private static final String SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_SECTION = " UPDATE newsletter_document_section SET id_template = ? WHERE id_section = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_SECTION = " DELETE FROM newsletter_document_section WHERE id_section = ? ";

    private static final String SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_LIST_DOCUMENT = "SELECT DISTINCT a.id_document , a.code_document_type, a.date_creation , a.date_modification, a.title, a.document_summary FROM document a INNER JOIN document_published b ON a.id_document=b.id_document INNER JOIN core_portlet c ON b.id_portlet=c.id_portlet WHERE c.id_portlet_type='DOCUMENT_LIST_PORTLET' ";
    private static final String SQL_QUERY_DOCUMENT_TYPE_PORTLET = " SELECT DISTINCT id_portlet , name FROM core_portlet WHERE id_portlet_type='DOCUMENT_LIST_PORTLET'  ";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST = "INSERT INTO newsletter_document_category ( id_section , id_category ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_SELECTALL_ID_DOCUMENT = " SELECT a.id_document FROM document_category_link a WHERE a.id_category = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST = "DELETE FROM newsletter_document_category WHERE id_section = ?";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS = "SELECT DISTINCT id_category FROM newsletter_document_category WHERE id_section = ?";
    private static final String SQL_FILTER_DATE_MODIF = " a.date_modification >=? ";
    private static final String SQL_FILTER_ID_PORTLET = " c.id_portlet = ? ";

    private static final String CONSTANT_AND = " AND ";
    private static final String CONSTANT_ORDER_BY_DATE_MODIF = " ORDER BY a.date_modification DESC ";

    /**
     * {@inheritDoc}
     */
    @Override
    public NewsletterDocument findByPrimaryKey( int nIdSection, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_SECTION, plugin );
        daoUtil.setInt( 1, nIdSection );
        daoUtil.executeQuery( );
        NewsletterDocument section = null;
        if ( daoUtil.next( ) )
        {
            section = new NewsletterDocument( );
            section.setId( daoUtil.getInt( 1 ) );
            section.setIdTemplate( daoUtil.getInt( 2 ) );
        }
        daoUtil.free( );
        return section;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDocumentSection( NewsletterDocument section, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_SECTION, plugin );
        daoUtil.setInt( 1, section.getIdTemplate( ) );
        daoUtil.setInt( 2, section.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocumentSection( int nIdSection, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_SECTION, plugin );
        daoUtil.setInt( 1, nIdSection );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDocumentSection( NewsletterDocument section, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_SECTION, plugin );
        daoUtil.setInt( 1, section.getId( ) );
        daoUtil.setInt( 2, section.getIdTemplate( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> selectDocumentsByDateAndCategory( int nPortletId, Timestamp dateLastSending, Plugin plugin )
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
            daoUtil.setInt( nIndex++, nPortletId );
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
    public ReferenceList selectDocumentTypePortlets( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DOCUMENT_TYPE_PORTLET, plugin );
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
    public void associateNewsLetterDocumentList( int nSectionId, int nDocumentCategoryId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST, plugin );
        daoUtil.setInt( 1, nSectionId );
        daoUtil.setInt( 2, nDocumentCategoryId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    //    /**
    //     * Select a list of Id Documents for a specified category
    //     * @param nIdCategory The category name
    //     * @return The array of Id Document
    //     */
    //    public int[] selectAllIdDocument( int nIdCategory )
    //    {
    //        Collection<Integer> listIdDocument = new ArrayList<Integer>( );
    //        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID_DOCUMENT );
    //        daoUtil.setInt( 1, nIdCategory );
    //        daoUtil.executeQuery( );
    //
    //        while ( daoUtil.next( ) )
    //        {
    //            listIdDocument.add( daoUtil.getInt( 1 ) );
    //        }
    //
    //        daoUtil.free( );
    //
    //        // Convert ArrayList to Int[]
    //        int[] arrayIdDocument = new int[listIdDocument.size( )];
    //        int i = 0;
    //
    //        for ( Integer nIdDocument : listIdDocument )
    //        {
    //            arrayIdDocument[i++] = nIdDocument.intValue( );
    //        }
    //
    //        return arrayIdDocument;
    //    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentList( int nSectionId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST, plugin );

        daoUtil.setInt( 1, nSectionId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] selectNewsletterCategoryIds( int nSectionId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS, plugin );

        daoUtil.setInt( 1, nSectionId );

        daoUtil.executeQuery( );

        List<Integer> list = new ArrayList<Integer>( );

        while ( daoUtil.next( ) )
        {
            int nResultId = daoUtil.getInt( 1 );
            list.add( new Integer( nResultId ) );
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

}
