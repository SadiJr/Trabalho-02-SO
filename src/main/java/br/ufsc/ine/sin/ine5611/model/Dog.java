package br.ufsc.ine.sin.ine5611.model;

import br.ufsc.ine.sin.ine5611.enums.Color;

public abstract class Dog extends Thread {
	
	private Color color;
	
	public Dog(Color color) {
		super();
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}