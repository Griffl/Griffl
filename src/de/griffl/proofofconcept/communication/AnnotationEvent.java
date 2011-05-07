package de.griffl.proofofconcept.communication;

import de.griffl.proofofconcept.pdf.PDFAnnotation;


public class AnnotationEvent extends BasicEvent implements Cloneable {
	
	PDFAnnotation annotation;
	
	public AnnotationEvent(PDFAnnotation anno) {
		super();
		this.annotation = anno;
	}

	public PDFAnnotation getAnnotation(){
		return annotation;
	}
	
}
