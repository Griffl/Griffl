package de.griffl.proofofconcept.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonNode;
import org.ektorp.CouchDbConnector;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;

import de.griffl.proofofconcept.db.ChangeEvent.ChangeListener;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public class ChangeLookup implements Runnable {
	private Logger logger = Logger.getLogger(ChangeLookup.class.getName());
	private ChangesCommand cmd = new ChangesCommand.Builder()
											.includeDocs(true)
											.build();
	private ChangesFeed feed;
	private Thread thread;
	private ChangeListener listener;
	public ChangeLookup(CouchDbConnector connector, ChangeListener listener){
		
		// this.listener = listener;
		 feed = connector.changesFeed(cmd);
		 thread = new Thread(this);
		 thread.start();

		
	}
	
	public void run() {
		logger.info("Thread started >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		while (feed.isAlive()) {
		    DocumentChange change;
			try {
				change = feed.next();
			    String docId = change.getId();
			    String rev = change.getRevision();
			    String[] versionArray = rev.split("-");
			    int version = Integer.parseInt(versionArray[0]);
			    DocumentState state;
			    if(change.isDeleted())
			    	state = DocumentState.DELETED;
			    else 	if (version == 1)
			    			state = DocumentState.CREATED;
			    		else
			    			state = DocumentState.UPDATED;
			    
			  //  listener.changes(new ChangeEvent(docId,state));
			    logger.info(state.toString()+" an Dokument ID="+docId+" Version="+version);
			    if(!feed.isAlive()){
			    	logger.info("Feed has been killed !!!!!!!!!");
			    }
			} catch (InterruptedException e) {
				logger.info("Exception has interrupted the thread");
				logger.log(Level.INFO, this.getClass().getName()+" Thread was interrupted", e);
			}
		
		logger.info("Thread stopped feed is alive ="+feed.isAlive()+" |||||||||||||||||||||||||||||||||||||||||||");
		}
	}

	public void setFeedCancel(){
		feed.cancel();
	}
}
