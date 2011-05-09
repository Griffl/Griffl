package de.griffl.proofofconcept.pdf;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

import de.griffl.proofofconcept.communication.User;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public class PDFAnnotation extends CouchDbDocument {
	private String id;	// Unique ID in the database
	private String revision;
	private String pdfid;
	private String type ="annotation";
	
	private int xPosRel;
	private int yPosRel;
	private int xPosAbs;
	private int yPosAbs;
	
	private int page;
	
	private int annotationID; // Increasing number within one PDF document
	
	private String commentContent;
	
	private Date dateCreated;
	
	private User user;
	
	public int getxPosRel() {
		return xPosRel;
	}
	public void setxPosRel(int xPosRel) {
		this.xPosRel = xPosRel;
	}
	public int getyPosRel() {
		return yPosRel;
	}
	public void setyPosRel(int yPosRel) {
		this.yPosRel = yPosRel;
	}
	public int getxPosAbs() {
		return xPosAbs;
	}
	public void setxPosAbs(int xPosAbs) {
		this.xPosAbs = xPosAbs;
	}
	public int getyPosAbs() {
		return yPosAbs;
	}
	public void setyPosAbs(int yPosAbs) {
		this.yPosAbs = yPosAbs;
	}
	public int getAnnotationID() {
		return annotationID;
	}
	public void setAnnotationID(int annotationID) {
		this.annotationID = annotationID;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setPage(int page){
		this.page = page;
	}
	
	public int getPage(){
		return page;
	}
	
	public void setPdfid(String pdfid){
		this.pdfid = pdfid;
	}
	
	public String getPdfid(){
		return pdfid;
	}
	
	@JsonProperty("_id")
	public String getId() {
		return id;
	}
	@JsonProperty("_id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}
	@JsonProperty("_rev")
	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	@JsonProperty("date_created")
    public Date getDateCreated() {
            return dateCreated;
    }
    
    @JsonProperty("date_created")
    public void setDateCreated(Date dateCreated) {
            this.dateCreated = dateCreated;
    }
	
}
