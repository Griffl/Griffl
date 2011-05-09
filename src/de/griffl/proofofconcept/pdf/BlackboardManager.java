package de.griffl.proofofconcept.pdf;

import java.util.HashMap;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.github.wolfie.blackboard.Blackboard;

import de.griffl.proofofconcept.communication.NewAnnotationEvent;
import de.griffl.proofofconcept.communication.NewAnnotationEvent.NewAnnotationListener;
import de.griffl.proofofconcept.db.AnnotationRepository;
import de.griffl.proofofconcept.db.DBsettings;
import de.griffl.proofofconcept.db.PDFRepository;

public enum BlackboardManager {
	INSTANCE;
	
	private  HashMap<String, Blackboard> blackboardManager = new HashMap<String,Blackboard>(); 
	
	private  HttpClient httpClient = new StdHttpClient.Builder()
		.host(DBsettings.HOST)
		.port(DBsettings.PORT)
		.build();
		
	private  CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	private  CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
	
	private PDFRepository  pdfRepository = new PDFRepository(PDFDocument.class, dbC);
	private AnnotationRepository annotationRepository = new AnnotationRepository(PDFAnnotation.class, dbC);
	
	public void addDocument(PDFDocument pdfDoc, NewAnnotationListener listener){
		pdfRepository.add(pdfDoc);
		Blackboard bb = new Blackboard();
		// Register all evenets
		bb.register(NewAnnotationListener.class, NewAnnotationEvent.class);
		
		bb.addListener(listener);
		blackboardManager.put(pdfDoc.getId(),bb);
	}
	
	public PDFDocument getDocument(String pdfID, NewAnnotationListener listener){
		if (pdfRepository.contains(pdfID)){
			Blackboard bb = blackboardManager.get(pdfID);
			bb.addListener(listener);
			
			return pdfRepository.get(pdfID);
		}
		return null;
	}
	
	public void addAnnotation(PDFAnnotation pdfAnno){
		annotationRepository.add(pdfAnno);
		
		blackboardManager.get(pdfAnno.getPdfid()).fire(new NewAnnotationEvent(pdfAnno));
		
	}
	
	public Blackboard getBlackboard(String pdfID){
		return blackboardManager.get(pdfID);
	}
	
	public boolean containsPDF(String pdfID){
		return pdfRepository.contains(pdfID);
	}
}
