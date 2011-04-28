package de.griffl.proofofconcept;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.vaadin.Application;

import de.griffl.proofofconcept.db.DBsettings;
import de.griffl.proofofconcept.presenter.MainWindowPresenter;
import de.griffl.proofofconcept.view.MainWindowView;


public class ProofofconceptApplication extends Application {
//public static final ThreadLocal<CouchDbConnector> db = new ThreadLocal<CouchDbConnector>();
	private static  HttpClient httpClient = new StdHttpClient.Builder()
	.host(DBsettings.HOST)
	.port(DBsettings.PORT)
	.build();
	
	private static CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	public static CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
	
	@Override
	public void init() {
		 HttpClient httpClient = new StdHttpClient.Builder()
		.host(DBsettings.HOST)
		.port(DBsettings.PORT)
		.build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
		//db.set(dbC);
		
		MainWindowView mwv = new MainWindowView("Hauptfenster");
		MainWindowPresenter mwp = new MainWindowPresenter(mwv);
		 
		mwp.go(this);
	}

}
