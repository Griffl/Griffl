package de.griffl.proofofconcept.communication;

import java.awt.Color;

public class User {
	private String name;
	private Color color;
	
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
