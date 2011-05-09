package de.griffl.proofofconcept.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;

public class ChangeLookup implements Runnable {
	private ChangesCommand cmd = new ChangesCommand.Builder()
		.includeDocs(true)
		.build();

	private ChangesFeed feed;
	
	private Thread thread;
	
	public ChangeLookup(CouchDbConnector cdbConnect){
		feed = cdbConnect.changesFeed(cmd);
		thread = new Thread(this);
		thread.start();
	}
		
	public void run() {
		while(feed.isAlive()){
			DocumentChange change;
			try {
				change = feed.next();
				String docId = change.getId();
				System.out.println("Neues Dokument bzw. Šnderung an ID="+docId);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		   
		}
	}
	
	public void setFeedAlive(boolean alive){
		if (!alive)
			feed.cancel();
		else
			thread.start();
	}

}
