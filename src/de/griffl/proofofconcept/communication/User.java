package de.griffl.proofofconcept.communication;

import java.io.Serializable;

import de.griffl.proofofconcept.pdf.Color;
/**
 * 
 * @author Sebastian Schneider
 *
 */
public class User implements Serializable{
	private String name;
	private Color color;
	
	public User(){
		
	}
	
	public User(String name, Color color){
		this.name = name;
		this.color = color;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setColor(Color color){
		this.color = color;
	}
	public Color getColor(){
		return color;
	}
	
	public String toString(){
		return name;
	}
}
