package de.griffl.proofofconcept.presenter;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.griffl.proofofconcept.communication.AnnotationMethods;
import de.griffl.proofofconcept.communication.User;
import de.griffl.proofofconcept.communication.UserMethods;
import de.griffl.proofofconcept.pdf.PDFAnnotation;
import de.griffl.proofofconcept.pdf.PDFDocument;

public class PDFViewerPresenter implements Presenter, UserMethods, AnnotationMethods{
	

	public interface Display{
		public Window asWindow();
		public void setPage(Image currentPageIm);
		public Button getForwardButton();
		public Button getBackwardButton();
	}
	private Display display;
	private Document pdfDoc;
	
	
	private int currentPage = 0;
	
	public PDFViewerPresenter(Display display, PDFDocument doc){
		this.display = display;
		pdfDoc = new Document();
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
//		app.addWindow(display.asWindow());
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

	

}
