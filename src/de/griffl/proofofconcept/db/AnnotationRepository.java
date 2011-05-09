package de.griffl.proofofconcept.db;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;

import de.griffl.proofofconcept.pdf.*;

/**
 * 
 * @author Sebastian Schneider
 *
 */
public class AnnotationRepository extends CouchDbRepositorySupport<PDFAnnotation> {

	public AnnotationRepository(Class<PDFAnnotation> type,
			CouchDbConnector db) {
		super(type, db);
		initStandardDesignDocument();
	}
	
	@GenerateView
	public List<PDFAnnotation> findByPdfid(String pdfid){
		return queryView("by_pdfid", pdfid);
	}

}
