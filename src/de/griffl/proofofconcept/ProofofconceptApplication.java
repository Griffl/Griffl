package de.griffl.proofofconcept;

import java.awt.Color;
import java.util.logging.Logger;
import org.vaadin.artur.icepush.ICEPush;

import com.vaadin.Application;
import com.vaadin.ui.Window;

import de.griffl.proofofconcept.communication.NewAnnotationEvent;
import de.griffl.proofofconcept.communication.NewAnnotationEvent.NewAnnotationListener;
import de.griffl.proofofconcept.communication.User;

import de.griffl.proofofconcept.pdf.BlackboardManager;
import de.griffl.proofofconcept.pdf.PDFAnnotation;
import de.griffl.proofofconcept.pdf.PDFDocument;
import de.griffl.proofofconcept.presenter.MainWindowPresenter;
import de.griffl.proofofconcept.presenter.PDFViewerPresenter;
import de.griffl.proofofconcept.view.MainWindowView;
import de.griffl.proofofconcept.view.PDFViewerView;


public class ProofofconceptApplication extends Application implements NewAnnotationListener{
	
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


	public void newAnnotationCreated(NewAnnotationEvent event) {
		PDFAnnotation anno = event.getPDFAnnotation();
		pvv.creatCommentDot(anno.getxPosRel(), anno.getyPosRel());
		push.push();
	}



}
