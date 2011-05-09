package de.griffl.proofofconcept.db;

import com.github.wolfie.blackboard.Event;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public class ChangeEvent implements Event{
	
	private String docId;
	private DocumentState state;
	
	public ChangeEvent(String docId, DocumentState state){
		this.docId = docId;
		this.state = state;
	}
	
	public String getDocId(){
		return docId;
	}
	
	public DocumentState getDocumentState(){
		return state;
	}

	public interface ChangeListener{
		public void changes(ChangeEvent event);
	}
}
