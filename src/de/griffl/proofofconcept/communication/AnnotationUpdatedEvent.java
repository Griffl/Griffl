package de.griffl.proofofconcept.communication;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

import de.griffl.proofofconcept.pdf.PDFAnnotation;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public class AnnotationUpdatedEvent implements Event {
	private PDFAnnotation annotation;
	
	public AnnotationUpdatedEvent(PDFAnnotation annotation){
		this.annotation = annotation;
	}
	
	public PDFAnnotation getPDFAnnotation(){
		return annotation;
	}
	public interface AnnotationUpdatedListener extends Listener{
		@ListenerMethod
		public void annotationUpdated(AnnotationUpdatedEvent event);
	}
}
