package de.griffl.proofofconcept.presenter;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Logger;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.vaadin.overlay.TextOverlay;

import com.vaadin.Application;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.griffl.proofofconcept.ProofofconceptApplication;
import de.griffl.proofofconcept.communication.AnnotationMethods;
import de.griffl.proofofconcept.communication.User;
import de.griffl.proofofconcept.communication.UserMethods;
import de.griffl.proofofconcept.pdf.BlackboardManager;
import de.griffl.proofofconcept.pdf.PDFAnnotation;
import de.griffl.proofofconcept.pdf.PDFDocument;

public class PDFViewerPresenter implements Presenter, UserMethods, AnnotationMethods{
	

	public interface Display{
		public Window asWindow();
		public void setPage(Image currentPageIm);
		public Button getForwardButton();
		public Button getBackwardButton();
		
		public AbsoluteLayout getPDFLayout();
//		public HorizontalSplitPanel getWorkingGround();
//		public HorizontalSplitPanel getCommentUserGround();
//		public VerticalLayout getUserGround();
		
		public Embedded getEmbPage();
		public Tree getCommentList();
	}
	
	private Logger logger = Logger.getLogger(PDFViewerPresenter.class.getName());
	
	private Display display;
	private Document pdfDoc;
	
	private int currentPage = 0;
	
	private ProofofconceptApplication app;
	
	private String pdfID;
	
	public PDFViewerPresenter(Display display, PDFDocument doc, ProofofconceptApplication app, String pdfID){
		this.display = display;
		pdfDoc = new Document();
		this.app = app;
		this.pdfID = pdfID;
		InputStream in = new ByteArrayInputStream(doc.getDocument());
		try {
			pdfDoc.setInputStream(in, "Name :)");
		} catch (PDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PDFSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bind();

		
		
		Image im = pdfDoc.getPageImage(currentPage, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0, 1);
		display.setPage(im);
		
	}
	public Window go(Application app) {
		return display.asWindow();
	}
	
	
	private void bind()	{

		display.getForwardButton().addListener(new ClickListener(){

			public void buttonClick(ClickEvent event) {
				if (currentPage < pdfDoc.getNumberOfPages()-1)	{
					System.out.println(currentPage+" vor erhšhung - forward");
					currentPage++;
					System.out.println(currentPage+" nach erhšhung um eins - forward");
					Image im = pdfDoc.getPageImage(currentPage, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0, 1);
					display.setPage(im);
					
				}
//				if (currentPage == pdfDoc.getNumberOfPages()-1)	{
//					currentPage++;
//					Image im = pdfDoc.getPageImage(currentPage, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0, 1);
//					display.setPage(im);
//					display.getForwardButton().setEnabled(false);
//				}
			}
			
		});
		
		display.getBackwardButton().addListener(new ClickListener()	{

			public void buttonClick(ClickEvent event) {
				if (currentPage > 0) 	{
					System.out.println(currentPage+" vor -1 - backward");
					currentPage = currentPage-1;
					System.out.println(currentPage+" nach -1 - backward");
					Image im = pdfDoc.getPageImage(currentPage, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0, 1);
					display.setPage(im);
				}
//				if (currentPage == 1)	{
//					currentPage = currentPage-1;
//					Image im = pdfDoc.getPageImage(currentPage, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0, 1);
//					display.setPage(im);
//					display.getBackwardButton().setEnabled(false);
//				}
				
			}
			
		});
		
		display.getPDFLayout().addListener(new LayoutClickListener() {
			public void layoutClick(LayoutClickEvent event) {
				
				int xPosAbs = event.getClientX();
				int yPosAbs = event.getClientY();
					
				int xPosRel = event.getRelativeX();
				int yPosRel = event.getRelativeY();
				
				logger.info("pdf clicked " + "AbsPos: " + xPosAbs+" "+yPosAbs+" RelPos: "+xPosRel+" "+yPosRel);
				createNewAnnotation(xPosAbs, yPosAbs, xPosRel, yPosRel);

			}

		});


	}
	public void showNewAnnotation(PDFAnnotation anno) {
		// TODO Auto-generated method stub
		
	}
	public void updateAnnotation(PDFAnnotation anno) {
		// TODO Auto-generated method stub
		
	}
	public void deleteAnnotation(PDFAnnotation anno) {
		// TODO Auto-generated method stub
		
	}
	public void showNewUser(User user) {
		// TODO Auto-generated method stub
		
	}
	public void updateLoginStatus(User user, boolean loggedIn) {
		// TODO Auto-generated method stub
		
	}
	public void removeUser(User user) {
		// TODO Auto-generated method stub
		
	}

	
	private void createNewAnnotation(final int xPosAbs, final int yPosAbs, final int xPosRel, final int yPosRel) {
		
		logger.info("inside createNewAnnotation Methode");
		
		// creates the Window for the comment
		final Window comment = new Window("Kommentar");
		comment.setHeight("200px");
		comment.setWidth("200px");
		
		final TextArea editor = new TextArea();
		editor.setValue("Schreiben Sie ein Kommentar");

		final Button saveCommentButton = new Button("speichern");
		
		comment.addComponent(editor);
		comment.addComponent(saveCommentButton);

		comment.setPositionX(xPosAbs);
		comment.setPositionY(yPosAbs);
		logger.info("before addWindow");
		display.asWindow().addWindow(comment);
		logger.info("after addWindow");
		
		// Listener for saveButton
		saveCommentButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				logger.info("save Button clicked");
				
				// create new annotation on click
				PDFAnnotation anno = new PDFAnnotation();
				logger.info("new PDFAnnotation");
				
				anno.setPdfid(pdfID);
				
				anno.setxPosAbs(xPosAbs);
				anno.setyPosAbs(yPosAbs);
				anno.setxPosRel(xPosRel);
				anno.setyPosRel(yPosRel);
				logger.info("position set");
				
				anno.setPage(currentPage);
				logger.info("currentPage "+currentPage);
				
				Object user =  app.getUser();
				logger.info("get User");
				if (user != null)
					logger.info("user not null");
				anno.setUser((User) user);
				logger.info("user added");

				String commentContent = (String) editor.getValue();
				logger.info(""+commentContent);
				anno.setCommentContent(commentContent);
				
				if (anno != null)
					logger.info("anno not null");
				
				// creats annotation in the db
				BlackboardManager.INSTANCE.addAnnotation(anno);
				logger.info("after adding anno");

				// gets annotation ID from db
				int annoID = anno.getAnnotationID();
				logger.info("annoID: "+annoID);
				
				createAnnoDot(annoID, xPosRel, yPosRel);
				createAnnoTreeItem(annoID, anno.getUser().getName(), anno.getDateCreated(), commentContent);
				
				display.asWindow().getWindow().removeWindow(comment);
				
			}

		});
		
	}
	
	// Create new Item in CommentList Tree
	public void createAnnoTreeItem(int annoID, String user, Date date, String commentContent) {
		String head = "Kommentar Nr: "+annoID+" von "+user+" "+date;
		String content = commentContent;
		display.getCommentList().addItem(head);
		display.getCommentList().addItem(content);
		display.getCommentList().setParent(content, head);
		display.getCommentList().setChildrenAllowed(content, false);
	}

	// Creats the dot over the PDF Document
	public void createAnnoDot(int annoID, int xPosRel, int yPosRel) {
		TextOverlay commentDot = new TextOverlay(display.getEmbPage(), ""+annoID);
		commentDot.getOverlay().setStyleName("commentDot-style");
		
		commentDot.setComponentAnchor(Alignment.TOP_LEFT);
		commentDot.setOverlayAnchor(Alignment.TOP_LEFT);
		
		commentDot.setXOffset(xPosRel);
		commentDot.setYOffset(yPosRel);
		
		display.getPDFLayout().addComponent(commentDot);
	}
	

}
