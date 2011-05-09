package de.griffl.proofofconcept.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import de.griffl.proofofconcept.pdf.PDFAnnotation;
import de.griffl.proofofconcept.pdf.PDFDocument;

public enum DocumentManager {
	INSTANCE;
	
	private  HttpClient httpClient = new StdHttpClient.Builder()
	.host(DBsettings.HOST)
	.port(DBsettings.PORT)
	.build();
	
	private  CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	private  CouchDbConnector dbC = new StdCouchDbConnector(DBsettings.DATABASE, dbInstance);
	
	private   PDFRepository  pdfRepository = new PDFRepository(PDFDocument.class, dbC);
	private   AnnotationRepository annotationRepository = new AnnotationRepository(PDFAnnotation.class, dbC);
	
	private ChangesCommand cmd = new ChangesCommand.Builder()
											.includeDocs(true)
											.build();
	
	private ChangesFeed feed = dbC.changesFeed(cmd);
	

	private ChangeLookup changeLookup = new ChangeLookup(dbC);
	
	
	
}
