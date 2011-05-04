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
import de.griffl.proofofconcept.db.DBsettings;
import de.griffl.proofofconcept.db.PDFRepository;
import de.griffl.proofofconcept.pdf.PDFDocument;
import de.griffl.proofofconcept.presenter.MainWindowPresenter;
import de.griffl.proofofconcept.presenter.PDFViewerPresenter;
import de.griffl.proofofconcept.view.MainWindowView;
import de.griffl.proofofconcept.view.PDFViewerView;

// Test
// 2nd Test comment
public class ProofofconceptApplication extends Application implements EventUpdateListener{
//public static final ThreadLocal<CouchDbConnector> db = new ThreadLocal<CouchDbConnector>();
	private static  HttpClient httpClient = new StdHttpClient.Builder()
	.host(DBsettings.HOST)
	.port(DBsettings.PORT)
	.build();
	
	private static CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	public static CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
	public static PDFRepository repository = new PDFRepository(PDFDocument.class, dbC);
	
	public EventController eventController;
	private ICEPush push;
	public PDFViewerView pvv;
	
	private static int number = 0;
	
	private Logger logger = Logger.getLogger(ProofofconceptApplication.class.getName());
	
	@Override
	public void init() {
		 HttpClient httpClient = new StdHttpClient.Builder()
		.host(DBsettings.HOST)
		.port(DBsettings.PORT)
		.build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
		//db.set(dbC);
		
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
		
		if(super.getWindow(name) == null && repository.contains(name)){
			
			PDFDocument doc = repository.get(name);
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
		
		pvv.creatCommentDot(event.getxPosRel(), event.getyPosRel());
		User u = (User) app.getUser();
		logger.info("eventUpdate called from app: "+u.getName());
		push.push();
	}

}
