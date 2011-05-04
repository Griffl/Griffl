package de.griffl.proofofconcept.communication;


public class AnnotationEvent extends BasicEvent implements Cloneable {
	
	private int xPosRel;
	private int yPosRel;
	private int xPosAbs;
	private int yPosAbs;
	
	private int annotationID;
	
	private String commentContent;

	
	public AnnotationEvent(int xPosRel, int yPosRel, int xPosAbs, int yPosAbs, int annotationID,
			String commentContent) {
		super();
		this.xPosRel = xPosRel;
		this.yPosRel = yPosRel;
		this.xPosAbs = xPosAbs;
		this.yPosAbs = yPosAbs;
		this.commentContent = commentContent;
		this.annotationID = annotationID;
	}

	public int getxPosRel() {
		return xPosRel;
	}


	public int getyPosRel() {
		return yPosRel;
	}


	public int getxPosAbs() {
		return xPosAbs;
	}


	public int getyPosAbs() {
		return yPosAbs;
	}

	
	public String getCommentContent() {
		return commentContent;
	}

	
	public int getAnnotationID(){
		return annotationID;
	}
	
}
