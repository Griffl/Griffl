package de.griffl.proofofconcept.pdf;

import java.util.HashMap;
import java.util.logging.Logger;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.github.wolfie.blackboard.Blackboard;

import de.griffl.proofofconcept.ProofofconceptApplication;
import de.griffl.proofofconcept.communication.AnnotationCreatedEvent;
import de.griffl.proofofconcept.communication.PDFdeletedEvent;
import de.griffl.proofofconcept.communication.AnnotationCreatedEvent.AnnotationCreatedListener;
import de.griffl.proofofconcept.communication.AnnotationDeletedEvent;
import de.griffl.proofofconcept.communication.AnnotationDeletedEvent.AnnotationDeletedListener;
import de.griffl.proofofconcept.communication.AnnotationUpdatedEvent;
import de.griffl.proofofconcept.communication.AnnotationUpdatedEvent.AnnotationUpdatedListener;
import de.griffl.proofofconcept.communication.PDFdeletedEvent.PDFdeletedListener;
import de.griffl.proofofconcept.communication.UserLoggedInEvent;
import de.griffl.proofofconcept.communication.UserLoggedInEvent.UserLoggedInListener;
import de.griffl.proofofconcept.communication.UserLoggedOffEvent;
import de.griffl.proofofconcept.communication.UserLoggedOffEvent.UserLoggedOffListener;
import de.griffl.proofofconcept.db.AnnotationRepository;
import de.griffl.proofofconcept.db.ChangeEvent;
import de.griffl.proofofconcept.db.ChangeEvent.ChangeListener;
import de.griffl.proofofconcept.db.ChangeLookup;
import de.griffl.proofofconcept.db.DBsettings;
import de.griffl.proofofconcept.db.DocumentState;
import de.griffl.proofofconcept.db.PDFRepository;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public enum BlackboardManager implements ChangeListener{
	INSTANCE;
	
	private Logger logger = Logger.getLogger(BlackboardManager.class.getName());
	
	private  HashMap<String, Blackboard> blackboardManager = new HashMap<String,Blackboard>(); 
	
	private  HttpClient httpClient = new StdHttpClient.Builder()
		.host(DBsettings.HOST)
		.port(DBsettings.PORT)
		.build();
		
	private  CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	private  CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
	
	private PDFRepository  pdfRepository = new PDFRepository(PDFDocument.class, dbC);
	private AnnotationRepository annotationRepository = new AnnotationRepository(PDFAnnotation.class, dbC);
//	private ChangeLookup lookup = new ChangeLookup(dbC, this);
	
	public synchronized void addDocument(PDFDocument pdfDoc){
		pdfRepository.add(pdfDoc);
		
	}
	
	public synchronized PDFDocument getDocument(String pdfID, ProofofconceptApplication listener){
		if (pdfRepository.contains(pdfID)){
			if(blackboardManager.containsKey(pdfID)){
			Blackboard bb = blackboardManager.get(pdfID);
			bb.removeListener(listener);
			bb.addListener(listener);
			
			return pdfRepository.get(pdfID);
			} else {
				Blackboard bb = new Blackboard();
				// Register all evenets
				bb.register(AnnotationCreatedListener.class, AnnotationCreatedEvent.class);
				bb.register(AnnotationUpdatedListener.class, AnnotationUpdatedEvent.class);
				bb.register(AnnotationDeletedListener.class, AnnotationDeletedEvent.class);
				
				bb.register(PDFdeletedListener.class, PDFdeletedEvent.class);
				
				bb.register(UserLoggedInListener.class, UserLoggedInEvent.class);
				bb.register(UserLoggedOffListener.class, UserLoggedOffEvent.class);
				
				
				bb.addListener(listener);
				blackboardManager.put(pdfID,bb);
				return pdfRepository.get(pdfID);
			}
		}
		return null;
	}
	
	public synchronized void deletePDFDocument(PDFDocument doc){
		pdfRepository.remove(doc);
		//blackboardManager.remove(doc.getId());
	}
	public synchronized void addAnnotation(PDFAnnotation pdfAnno){
		
		annotationRepository.add(pdfAnno);
		logger.info("PDFAnnotation ID="+pdfAnno.getId()+ " zu PDF_ID="+pdfAnno.getPdfid()+" hizugefügt");
		blackboardManager.get(pdfAnno.getPdfid()).fire(new AnnotationCreatedEvent(pdfAnno));
		
	}
	
	public synchronized Blackboard getBlackboard(String pdfID){
		return blackboardManager.get(pdfID);
	}
	
	public boolean containsPDF(String pdfID){
		return pdfRepository.contains(pdfID);
	}

	public void changes(ChangeEvent event) {
		logger.info("changes Methode gerufen");
		String docId = event.getDocId();
		DocumentState state = event.getDocumentState();
		
		
		switch(state){
		case DELETED: 
				//TODO figure out how it works zwischen PDFDocument und PDFAnnotation unterscheiden
		case UPDATED: 
				PDFAnnotation anno= annotationRepository.get(docId);
				String pdfID = anno.getPdfid();
				blackboardManager.get(pdfID).fire(new AnnotationUpdatedEvent(anno));
				break;
		case CREATED:
				PDFAnnotation anno2 = annotationRepository.get(docId);
				String pdfID2 = anno2.getPdfid();
				blackboardManager.get(pdfID2).fire(new AnnotationCreatedEvent(anno2));
				break;
		}
	}
	
}
