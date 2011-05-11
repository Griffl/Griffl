package de.griffl.proofofconcept.db;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;

import de.griffl.proofofconcept.pdf.*;

/**
 * 
 * @author Sebastian Schneider
 *
 */
public class AnnotationRepository extends CouchDbRepositorySupport<PDFAnnotation> {
	
	private CouchDbConnector db;
	public AnnotationRepository(Class<PDFAnnotation> type,
			CouchDbConnector db) {
		super(type, db);
		initStandardDesignDocument();
		this.db = db;
	}
	
	@GenerateView
	public List<PDFAnnotation> findByPdfid(String pdfid){
		return queryView("by_pdfid", pdfid);
	}
	@View( name = "max_annotationID", 
			map = 	"function(doc) {" +
						"if(doc.type == \"annotation\"){" +
						"emit(doc.pdfid,doc.annotationID)}" +
					"}",
			reduce = "function(keys, values){ " +
						"var max = 0;" +
						"for(var i=0; i < values.length;i++){" +
							"if(max <= values[i])" +
								"max = values[i];" +
						"}" +
					"return max;}")
	public int getMaxAnnotationID(String pdfid){
		ViewQuery v = new ViewQuery();
		v.viewName("max_annotationID");
		v.designDocId("_design/PDFAnnotation");
		v.limit(1);
		v.group(true);
		v.groupLevel(1);
		v.key(pdfid);
		ViewResult r = db.queryView(v);
		return r.getRows().get(0).getValueAsInt();
	}

}
