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
    private static final String SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_CATEGORY = "SELECT a.id_document , a.code_document_type, a.date_creation , a.date_modification, a.title,a.document_summary FROM document a"
            + "INNER JOIN  document_published b ON a.id_document=b.id_document  INNER JOIN document_category_link c ON b.id_document=c.id_document WHERE a.date_modification >=? AND c.id_category= ? ORDER BY a.date_modification DESC";
    private static final String SQL_QUERY_DOCUMENT_TYPE_PORTLET = " SELECT DISTINCT id_portlet , name FROM core_portlet WHERE id_portlet_type='DOCUMENT_PORTLET'  ";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST = "INSERT INTO newsletter_category_list ( id_newsletter , id_category_list ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_SELECTALL_ID_DOCUMENT = " SELECT a.id_document FROM document_category_link a WHERE a.id_category = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST = "DELETE FROM newsletter_category_list WHERE id_newsletter = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> selectDocumentsByDateAndList( int nCategoryId, Timestamp dateLastSending )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_CATEGORY );
        daoUtil.setTimestamp( 1, dateLastSending );
        daoUtil.setInt( 2, nCategoryId );

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
    public ReferenceList selectDocumentTypePortlets( )
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
    public void associateNewsLetterDocumentList( int nNewsLetterId, int nDocumentListId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST, plugin );
        daoUtil.setInt( 1, nNewsLetterId );
        daoUtil.setInt( 2, nDocumentListId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Select a list of Id Documents for a specified category
     * @param nIdCategory The category name
     * @return The array of Id Document
     */
    public int[] selectAllIdDocument( int nIdCategory )
    {
        Collection<Integer> listIdDocument = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID_DOCUMENT );
        daoUtil.setInt( 1, nIdCategory );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listIdDocument.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        // Convert ArrayList to Int[]
        int[] arrayIdDocument = new int[listIdDocument.size( )];
        int i = 0;

        for ( Integer nIdDocument : listIdDocument )
        {
            arrayIdDocument[i++] = nIdDocument.intValue( );
        }

        return arrayIdDocument;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentList( int nNewsLetterId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST, plugin );

        daoUtil.setInt( 1, nNewsLetterId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
