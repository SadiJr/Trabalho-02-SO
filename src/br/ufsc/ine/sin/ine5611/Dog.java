package br.ufsc.ine.sin.ine5611;

public abstract class Dog implements Runnable {
	
	private Color color;
	
	public Dog(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}