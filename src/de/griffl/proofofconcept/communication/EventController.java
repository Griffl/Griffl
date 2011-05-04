package de.griffl.proofofconcept.communication;

import java.util.ArrayList;
import java.util.List;


import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.griffl.proofofconcept.ProofofconceptApplication;


public class EventController {
	
	public interface EventUpdateListener{
		public void eventUpdated(ProofofconceptApplication app, AnnotationEvent event);
	}
	
	
	private static List<EventUpdateListener> listeners = new ArrayList<EventUpdateListener>();
	
	private ProofofconceptApplication app;
	
	public EventController(ProofofconceptApplication app){
		this.app = app;
		addListener(this.app);
	}
	
	private void addListener(EventUpdateListener listener){
		synchronized(EventController.class){
			listeners.add(listener);
		}
	}
	
	public void fireEventUpdated(AnnotationEvent event){
		synchronized (EventController.class) {
            for (EventUpdateListener l : listeners) {
                l.eventUpdated(app, event);
            }
        }
	}
}
