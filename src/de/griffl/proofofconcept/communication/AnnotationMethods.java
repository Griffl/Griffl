package de.griffl.proofofconcept.communication;

import de.griffl.proofofconcept.pdf.PDFAnnotation;

public interface AnnotationMethods {
	public void showNewAnnotation(PDFAnnotation anno);
	public void updateAnnotation(PDFAnnotation anno);
	public void deleteAnnotation(PDFAnnotation anno);
}
