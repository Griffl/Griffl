package de.griffl.proofofconcept.communication;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;
/**
 * 
 * @author sebastianschneider
 *
 */
public class PDFdeletedEvent implements Event {
	
	public interface PDFdeletedListener extends Listener{
		@ListenerMethod
		public void pdfDeleted(PDFdeletedEvent event);
	}
}
