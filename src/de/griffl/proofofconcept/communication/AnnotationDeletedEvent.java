package de.griffl.proofofconcept.communication;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public class AnnotationDeletedEvent implements Event {

	public interface AnnotationDeletedListener extends Listener{
		@ListenerMethod
		public void annotationDeleted(AnnotationDeletedEvent event);
	}
}
