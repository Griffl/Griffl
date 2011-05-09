package de.griffl.proofofconcept;

import java.awt.Color;
import java.util.logging.Logger;
import org.vaadin.artur.icepush.ICEPush;

import com.vaadin.Application;
import com.vaadin.ui.Window;

import de.griffl.proofofconcept.communication.AnnotationCreatedEvent;
import de.griffl.proofofconcept.communication.AnnotationCreatedEvent.AnnotationCreatedListener;
import de.griffl.proofofconcept.communication.AnnotationDeletedEvent;
import de.griffl.proofofconcept.communication.AnnotationDeletedEvent.AnnotationDeletedListener;
import de.griffl.proofofconcept.communication.AnnotationUpdatedEvent;
import de.griffl.proofofconcept.communication.AnnotationUpdatedEvent.AnnotationUpdatedListener;
import de.griffl.proofofconcept.communication.PDFdeletedEvent;
import de.griffl.proofofconcept.communication.PDFdeletedEvent.PDFdeletedListener;
import de.griffl.proofofconcept.communication.User;
import de.griffl.proofofconcept.communication.UserLoggedInEvent;
import de.griffl.proofofconcept.communication.UserLoggedInEvent.UserLoggedInListener;
import de.griffl.proofofconcept.communication.UserLoggedOffEvent;
import de.griffl.proofofconcept.communication.UserLoggedOffEvent.UserLoggedOffListener;

import de.griffl.proofofconcept.pdf.BlackboardManager;
import de.griffl.proofofconcept.pdf.PDFAnnotation;
import de.griffl.proofofconcept.pdf.PDFDocument;
import de.griffl.proofofconcept.presenter.MainWindowPresenter;
import de.griffl.proofofconcept.presenter.PDFViewerPresenter;
import de.griffl.proofofconcept.view.MainWindowView;
import de.griffl.proofofconcept.view.PDFViewerView;

/**
 * 
 * @author Sebastian Schneider
 *
 */
public class ProofofconceptApplication extends Application implements 
												AnnotationCreatedListener, 
												AnnotationUpdatedListener, 
												AnnotationDeletedListener, 
												PDFdeletedListener, 
												UserLoggedInListener, 
												UserLoggedOffListener{
	
	private ICEPush push;
	public PDFViewerView pvv;
	
	private static int number = 0;
	
	private Logger logger = Logger.getLogger(ProofofconceptApplication.class.getName());
	
	@Override
	public void init() {
		
		number ++;
		
		setUser(new User("Benutzer "+number, Color.BLACK));
		push = new ICEPush();
		
		MainWindowView mwv = new MainWindowView("Hauptfenster");
		MainWindowPresenter mwp = new MainWindowPresenter(mwv, this);
		 
		mwp.go(this);
		setTheme("proofofconcepttheme");
	}
	
	
	public Window getWindow(String name){
		
		if(super.getWindow(name) == null && BlackboardManager.INSTANCE.containsPDF(name)){
			
			PDFDocument doc = BlackboardManager.INSTANCE.getDocument(name, this);
			pvv = new PDFViewerView(name, push, this);
			PDFViewerPresenter pvp = new PDFViewerPresenter(pvv, doc);
			
			Window pdfWindow = pvp.go(this);	
			addWindow(pdfWindow);
			
			return pdfWindow;
		}
		
		return super.getWindow(name);
		
	}


//	public void eventUpdated(ProofofconceptApplication app,
//			AnnotationEvent event) {
//		PDFAnnotation anno = event.getAnnotation();
//		pvv.creatCommentDot(anno.getxPosRel(), anno.getyPosRel());
//		User u = (User) app.getUser();
//		logger.info("eventUpdate called from app: "+u.getName());
//		push.push();
//	}


	public void annotationCreated(AnnotationCreatedEvent event) {
		PDFAnnotation anno = event.getPDFAnnotation();
		pvv.creatCommentDot(anno.getxPosRel(), anno.getyPosRel());
		push.push();
	}


	public void userLoggedOff(UserLoggedOffEvent event) {
		// TODO Auto-generated method stub
		
	}


	public void annotationDeleted(AnnotationDeletedEvent event) {
		// TODO Auto-generated method stub
		
	}


	public void pdfDeleted(PDFdeletedEvent event) {
		// TODO Auto-generated method stub
		
	}


	public void userLoggedIn(UserLoggedInEvent event) {
		// TODO Auto-generated method stub
		
	}


	public void annotationUpdated(AnnotationUpdatedEvent event) {
		// TODO Auto-generated method stub
		
	}



}
