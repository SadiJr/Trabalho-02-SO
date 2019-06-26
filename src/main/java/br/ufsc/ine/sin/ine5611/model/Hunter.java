package br.ufsc.ine.sin.ine5611.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;

public class Hunter {
	private static final Logger LOGGER = LogManager.getLogger(Hunter.class);
	
	private Color color;
	private int coins;
	private Dog[] dogs = new Dog[2];

	public Hunter(Color color) {
		this.color = color;
		dogs[0] = new Dog(1, color, this);
		dogs[1] = new Dog(2, color, this);
	}

}
