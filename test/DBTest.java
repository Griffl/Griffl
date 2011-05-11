import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import de.griffl.proofofconcept.db.AnnotationRepository;
import de.griffl.proofofconcept.db.DBsettings;
import de.griffl.proofofconcept.db.PDFRepository;
import de.griffl.proofofconcept.pdf.*;


public class DBTest {
	
	private  HttpClient httpClient = new StdHttpClient.Builder()
	.host(DBsettings.HOST)
	.port(DBsettings.PORT)
	.build();
	
	private  CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	private  CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
	
	public PDFRepository pdfRepository = new PDFRepository(PDFDocument.class, dbC);
	public AnnotationRepository annotationRepository = new AnnotationRepository(PDFAnnotation.class, dbC);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBTest test = new DBTest();
		PDFDocument doc = new PDFDocument();
		File f = new File("test/diplom.pdf");
		System.out.println(f.length());
		byte[] buffer = new byte[(int)f.length()];
		InputStream in = null;
		try {
			in = new FileInputStream(f);
			in.read(buffer);
			in.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.setDocument(buffer);
		List<PDFAnnotation> annotations = null;
		try {
		test.pdfRepository.add(pdfDoc);
		
		System.out.println("PDF_ID="+pdfDoc.getId());
		
		for(int i = 1; i<13; i++){
			PDFAnnotation anno = new PDFAnnotation();
			anno.setPdfid(pdfDoc.getId());
			anno.setCommentContent("Kommentar "+i);
			anno.setAnnotationID(i);
			anno.setDateCreated(new Date());
			test.annotationRepository.add(anno);
			System.out.println("Kommentar "+i+" hinzugefŸgt");
		}
		annotations = test.annotationRepository.findByPdfid(pdfDoc.getId());
		int maxAnno = test.annotationRepository.getMaxAnnotationID(pdfDoc.getId());
		System.out.println("PDF_ID="+pdfDoc.getId()+" hat "+annotations.size()+" Annotationen");
		System.out.println("maximale annotationID="+maxAnno);
		} finally {
			
		}
		
	}

}
