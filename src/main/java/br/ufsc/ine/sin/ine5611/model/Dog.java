package br.ufsc.ine.sin.ine5611.model;

import br.ufsc.ine.sin.ine5611.enums.Color;

public class Dog {
	
	private int id;
	private Color color;
	private int coins;
	private Hunter hunter;

	public Dog(int id, Color color, Hunter hunter) {
		this.id = id;
		this.color = color;
		this.hunter = hunter;
		coins = 0;
	}

	public int getId() {
		return id;
	}

	public Color getColor() {
		return color;
	}

	public int getCoins() {
		return coins;
	}
	
	public void addCoins(int quantity) {
		coins += quantity;
		
	}
}