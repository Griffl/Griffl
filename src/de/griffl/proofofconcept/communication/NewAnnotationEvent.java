package de.griffl.proofofconcept.communication;

import com.vaadin.ui.Component;
import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

import de.griffl.proofofconcept.pdf.PDFAnnotation;

public class NewAnnotationEvent implements Event {
	
	private PDFAnnotation annotation;
	
	public NewAnnotationEvent(PDFAnnotation annotation){
		this.annotation = annotation;
	}
	
	public PDFAnnotation getPDFAnnotation(){
		return annotation;
	}

	public interface NewAnnotationListener extends Listener {
		@ListenerMethod
		void newAnnotationCreated(NewAnnotationEvent event);
	}

}
