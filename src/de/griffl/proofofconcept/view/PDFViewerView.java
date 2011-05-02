package de.griffl.proofofconcept.view;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.vaadin.cssinject.CSSInject;
import org.vaadin.overlay.CustomClickableOverlay;
import org.vaadin.overlay.CustomOverlay;
import org.vaadin.overlay.ImageOverlay;
import org.vaadin.overlay.OverlayClickListener;
import org.vaadin.overlay.TextOverlay;

import com.vaadin.Application;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.griffl.proofofconcept.presenter.PDFViewerPresenter.Display;

public class PDFViewerView extends Window implements Display {
	
	private Button forward = new Button(">");
	private Button backward = new Button ("<");
	
	private final Panel marginalRight = new Panel();
	private int commentID = 1;
	private Tree commentList = new Tree();
	
	HorizontalSplitPanel workingGround = new HorizontalSplitPanel();
	HorizontalSplitPanel commentUserGround = new HorizontalSplitPanel();
	
	VerticalLayout userGround = new VerticalLayout();
	
	AbsoluteLayout pdfLayout = new AbsoluteLayout();
	
	VerticalLayout basicVerticalLayout = new VerticalLayout();
	
	Embedded embPage;
	
	//private static final Logger logger = Logger.getLogger(PDFViewerView.class.getName());
	private Application app;
	
	public PDFViewerView(String name, Application app){
				super(name);
		
				setTheme("proofofconcepttheme");
				//pdfLayout.setStyleName("v-generated-body");

				// Header with Applicationname
				Label appName = new Label("griffl");
				appName.setHeight("20px");
				basicVerticalLayout.addComponent(appName);
				
				
				// Menubar
				HorizontalLayout menubarLayout = new HorizontalLayout();
				menubarLayout.setHeight("30px");
				
				Button comment = new Button("Kommentar");
				comment.setWidth("100px");
				Button marker = new Button("Marker");
				marker.setWidth("100px");
				Button cross = new Button("Durchstreicher");
				cross.setWidth("100px");
				
				menubarLayout.addComponent(comment);
				menubarLayout.addComponent(marker);
				menubarLayout.addComponent(cross);
				basicVerticalLayout.addComponent(menubarLayout);
				
				
				
				// Navigationbar
				HorizontalLayout navigationbar = new HorizontalLayout();
				navigationbar.addComponent(backward);
				navigationbar.addComponent(forward);
				basicVerticalLayout.addComponent(navigationbar);


				
				
				// Working Ground = pdfGround + marginalRight
				pdfLayout.setHeight("700px");
				pdfLayout.setWidth("600px");

				workingGround.setSplitPosition(650, Sizeable.UNITS_PIXELS, true);
				workingGround.setWidth("1300px");
				workingGround.setHeight("550px");
				
				workingGround.addComponent(pdfLayout);
				
				marginalRight.addComponent(commentList);
				
				commentUserGround.addComponent(marginalRight);
//				userGround.setWidth("100%");
//				userGround.setHeight("100%");
				commentUserGround.addComponent(userGround);
				// nur zum Aussehen
				CheckBox testUser = new CheckBox("Sebastian Schneider");
				testUser.setValue(true);
				userGround.addComponent(testUser);
				userGround.addComponent(new CheckBox("Stefanie Grewenig"));
				userGround.addComponent(new CheckBox("Max Mustermann"));
				//
				
//				commentUserGround.setWidth("100%");
//				commentUserGround.setHeight("100%");
				commentUserGround.setSplitPosition(300, Sizeable.UNITS_PIXELS, true);
				workingGround.addComponent(commentUserGround);
			
				basicVerticalLayout.addComponent(workingGround);

				addComponent(basicVerticalLayout);
				
				//pdfLayout.addComponent(pdfGround);
				// Listener for pdfGround
				pdfLayout.addListener(new LayoutClickListener() {

					public void layoutClick(LayoutClickEvent event) {
						int xpos = event.getClientX();
						int ypos = event.getClientY();
						
						int xposRel = event.getRelativeX();
						int yposRel = event.getRelativeY();
						
						openCommentWindow(xpos, ypos, xposRel, yposRel);
						
					}
					
				});
				
	
				this.app = app;	
				
		}


	
	
	public Window asWindow() {
		return this;
	}

	// Sets current Page Image
	public void setPage(Image currentPageIm) {	
		Resource page = createStreamResource(currentPageIm);
		embPage = new Embedded("Seite",page);
		pdfLayout.addComponent(embPage);
	

		
		//pdfGround.setIcon(page);
		//requestRepaint();
	}


	public Button getForwardButton() {
		return forward;
	}


	public Button getBackwardButton() {
		return backward;
	}
	
	
	
	
	private void openCommentWindow(final int xpos, final int ypos, final int xposRel, final int yposRel) {
		
		final Window comment = new Window("Kommentar");
		comment.setHeight("200px");
		comment.setWidth("200px");
		
		final TextArea editor = new TextArea();
		editor.setValue("Schreiben Sie ein Kommentar");

		final Button saveComment = new Button("speichern");
		
		saveComment.addListener(new ClickListener() {
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				String commentContent = (String) "("+commentID+") "+ editor.getValue();
				String commentHead =  new String ("Kommentar Nr: " + commentID+ " erstellt von Sebastian Schneider am 02.05.2011");
				commentList.addItem(commentHead);
				commentList.addItem(commentContent);
				commentList.setParent(commentContent, commentHead);
				commentList.setChildrenAllowed(commentContent, false);
				
				creatCommentDot(xposRel, yposRel);
				
				commentID++;
			
			comment.getParent().removeWindow(comment);
				
			}

		});
		
		comment.addComponent(editor);
		comment.addComponent(saveComment);

		comment.setPositionX(xpos);
		comment.setPositionY(ypos);
		addWindow(comment);
		
	}
	
	private void creatCommentDot(int xposRel, int yposRel) {
//		Button commentDot = new Button (""+commentID);
//		pdfLayout.addComponent(commentDot, "top:"+xpos+"px; left:"+ypos+"px");
//		cssPdf.setValue(".custom-style { background-color: red; }");
//		pdfLayout.addComponent(cssPdf);
		//spdfGround.setStyleName("v-generated-body");
		
		Resource commentIcon = new ClassResource("/Users/Taffy/Documents/Griffel/PoC_Griffl/Griffl/WebContent/VAADIN/themes/proofofconcepttheme/images/icon.png", app);
		String commentText = new String(""+ commentID);
		
		TextOverlay commentDot = new TextOverlay(embPage, commentText);
		commentDot.getOverlay().setStyleName("commentDot-style");
		
		commentDot.setComponentAnchor(Alignment.TOP_LEFT);
		commentDot.setOverlayAnchor(Alignment.TOP_LEFT);
		
		commentDot.setXOffset(xposRel);
		commentDot.setYOffset(yposRel);
		
		commentDot.setClickListener(new OverlayClickListener() {

			public void overlayClicked(CustomClickableOverlay overlay) {
				final Window comment = new Window("Kommentar");
				comment.setHeight("200px");
				comment.setWidth("200px");
				
				final TextArea editor = new TextArea();
				editor.setValue("hier");
				
				comment.addComponent(editor);
				
				comment.setPositionX(overlay.getXOffset());
				comment.setPositionY(overlay.getYOffset());
				addWindow(comment);
				
			}
			
		});	
		pdfLayout.addComponent(commentDot);
		//pdfLayout.addComponent(nothing);

		
		
	}
	
	
	private Resource createStreamResource(final Image im) {
		StreamResource.StreamSource curIm = new StreamResource.StreamSource() {
			private ByteArrayOutputStream imagebuffer = null;
			
			public InputStream getStream() {
				imagebuffer = new ByteArrayOutputStream();
				try {
					ImageIO.write((RenderedImage) im, "png", imagebuffer);
				} catch (IOException e) {
					return null;
				}
				return new ByteArrayInputStream(imagebuffer.toByteArray());
			}
		};
		return new StreamResource(curIm, "myImage.png", app);
	}
	
	
	

}
