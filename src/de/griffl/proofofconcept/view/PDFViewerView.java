package de.griffl.proofofconcept.view;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.vaadin.artur.icepush.ICEPush;
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

import de.griffl.proofofconcept.ProofofconceptApplication;
import de.griffl.proofofconcept.communication.User;
import de.griffl.proofofconcept.pdf.BlackboardManager;
import de.griffl.proofofconcept.pdf.PDFAnnotation;
import de.griffl.proofofconcept.presenter.PDFViewerPresenter.Display;

public class PDFViewerView extends Window implements Display {
	
	private Button forward = new Button(">");
	private Button backward = new Button ("<");
	
	private AbsoluteLayout pdfLayout = new AbsoluteLayout();
	
	private HorizontalSplitPanel workingGround = new HorizontalSplitPanel();
	private HorizontalSplitPanel commentUserGround = new HorizontalSplitPanel();
	
	private final Panel marginalRight = new Panel();
	
	private VerticalLayout userGround = new VerticalLayout();
	
	private int commentID = 1;
	private Tree commentList = new Tree();
	
	
	private Embedded embPage;
	
	
	VerticalLayout basicVerticalLayout = new VerticalLayout();
	
	//private static final Logger logger = Logger.getLogger(PDFViewerView.class.getName());
	private ProofofconceptApplication app;
	private String name;
	public PDFViewerView(String name,ICEPush push, ProofofconceptApplication app){
				super(name);
				this.name = name;
				setTheme("proofofconcepttheme");
				//pdfLayout.setStyleName("v-generated-body");
				addComponent(push);
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
				commentUserGround.addComponent(userGround);
				// nur zum Aussehen
				userGround.addComponent(new Label("Liste aller User"), 0);
				CheckBox testUser = new CheckBox("Sebastian Schneider");
				testUser.setValue(true);
				userGround.addComponent(testUser, 1);
				userGround.addComponent(new CheckBox("Stefanie Grewenig"), 2);
				userGround.addComponent(new CheckBox("Max Mustermann"), 3);
				//
				
//				commentUserGround.setWidth("100%");
//				commentUserGround.setHeight("100%");
				commentUserGround.setSplitPosition(300, Sizeable.UNITS_PIXELS, true);
				workingGround.addComponent(commentUserGround);
			
				basicVerticalLayout.addComponent(workingGround);

				addComponent(basicVerticalLayout);
				
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
	}


	public Button getForwardButton() {
		return forward;
	}


	public Button getBackwardButton() {
		return backward;
	}
	
	
	public AbsoluteLayout getPDFLayout()	{
		return pdfLayout;
	}
	
	public Embedded getEmbPage()	{
		return embPage;
	}
	
	public Tree getCommentList()	{
		return commentList;
	}
	
//	public HorizontalSplitPanel getWorkingGround()	{
//		return workingGround;
//	}
//	
//	public HorizontalSplitPanel getCommentUserGround()	{
//		return commentUserGround;
//	}
//	
//	public VerticalLayout getUserGround()	{
//		return userGround;
//	}
	

	
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
