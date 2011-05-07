package de.griffl.proofofconcept;

import java.awt.Color;
import java.util.logging.Logger;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.vaadin.artur.icepush.ICEPush;

import com.vaadin.Application;
import com.vaadin.ui.Window;

import de.griffl.proofofconcept.communication.AnnotationEvent;
import de.griffl.proofofconcept.communication.EventController;
import de.griffl.proofofconcept.communication.EventController.EventUpdateListener;
import de.griffl.proofofconcept.communication.User;
import de.griffl.proofofconcept.db.AnnotationRepository;
import de.griffl.proofofconcept.db.DBsettings;
import de.griffl.proofofconcept.db.PDFRepository;
import de.griffl.proofofconcept.pdf.PDFAnnotation;
import de.griffl.proofofconcept.pdf.PDFDocument;
import de.griffl.proofofconcept.presenter.MainWindowPresenter;
import de.griffl.proofofconcept.presenter.PDFViewerPresenter;
import de.griffl.proofofconcept.view.MainWindowView;
import de.griffl.proofofconcept.view.PDFViewerView;


public class ProofofconceptApplication extends Application implements EventUpdateListener{
	
	private static   HttpClient httpClient = new StdHttpClient.Builder()
	.host(DBsettings.HOST)
	.port(DBsettings.PORT)
	.build();
	
	private static CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	private static CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
	
	public static final ThreadLocal<PDFRepository>  pdfRepository = new ThreadLocal<PDFRepository>();
	public static final ThreadLocal<AnnotationRepository>annotationRepository = new ThreadLocal<AnnotationRepository>();
	
	//public static final PDFRepository  pdfRepository = new PDFRepository(PDFDocument.class, dbC);
	//public static final AnnotationRepository annotationRepository = new AnnotationRepository(PDFAnnotation.class, dbC);

	
	public EventController eventController;
	private ICEPush push;
	public PDFViewerView pvv;
	
	private static int number = 0;
	
	private Logger logger = Logger.getLogger(ProofofconceptApplication.class.getName());
	
	@Override
	public void init() {
		 pdfRepository.set(new PDFRepository(PDFDocument.class, dbC));
		 annotationRepository.set(new AnnotationRepository(PDFAnnotation.class, dbC));
		 
		
		number ++;
		
		setUser(new User("Benutzer "+number, Color.BLACK));
		eventController = new EventController(this);
		push = new ICEPush();
		
		MainWindowView mwv = new MainWindowView("Hauptfenster");
		MainWindowPresenter mwp = new MainWindowPresenter(mwv);
		 
		mwp.go(this);
		setTheme("proofofconcepttheme");
	}
	
	
	public Window getWindow(String name){
		
		if(super.getWindow(name) == null && pdfRepository.get().contains(name)){
			
			PDFDocument doc = pdfRepository.get().get(name);
			pvv = new PDFViewerView(name, push, this);
			PDFViewerPresenter pvp = new PDFViewerPresenter(pvv, doc);
			
			Window pdfWindow = pvp.go(this);	
			addWindow(pdfWindow);
			
			return pdfWindow;
		}
		
		return super.getWindow(name);
		
	}


	public void eventUpdated(ProofofconceptApplication app,
			AnnotationEvent event) {
		PDFAnnotation anno = event.getAnnotation();
		pvv.creatCommentDot(anno.getxPosRel(), anno.getyPosRel());
		User u = (User) app.getUser();
		logger.info("eventUpdate called from app: "+u.getName());
		push.push();
	}

}
