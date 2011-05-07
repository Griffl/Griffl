package de.griffl.proofofconcept.communication;

import java.awt.Color;
import java.io.Serializable;

public class User implements Serializable{
	private String name;
	private Color color;
	
	public User(){
		
	}
	public User(String name, Color color){
		this.name = name;
		this.color = color;
	}
	
	public String getName(){
		return name;
	}
	
	public Color getColor(){
		return color;
	}
}
