package de.griffl.proofofconcept.communication;

import com.vaadin.ui.Component;
import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

import de.griffl.proofofconcept.pdf.PDFAnnotation;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public class AnnotationCreatedEvent implements Event {
	
	private PDFAnnotation annotation;
	
	public AnnotationCreatedEvent(PDFAnnotation annotation){
		this.annotation = annotation;
	}
	
	public PDFAnnotation getPDFAnnotation(){
		return annotation;
	}

	public interface AnnotationCreatedListener extends Listener {
		@ListenerMethod
		void annotationCreated(AnnotationCreatedEvent event);
	}

}
