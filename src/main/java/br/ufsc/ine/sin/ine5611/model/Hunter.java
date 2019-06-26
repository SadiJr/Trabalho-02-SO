package br.ufsc.ine.sin.ine5611.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.thread.DogThread;

public class Hunter {
	private static final Logger LOGGER = LogManager.getLogger(Hunter.class);
	
	private Color color;
	private int coins;
	private DogThread[] dogs = new DogThread[2];
	private DogThread runningDog;
	private String basicMessage;

	public Hunter(Color color) {
		this.color = color;
		dogs[0] = new DogThread(1, color, this);
		dogs[1] = new DogThread(2, color, this);
		runningDog = dogs[0];
		basicMessage = "Cão " + color.name() + " " + runningDog.getId();
	}

	public void switchDogs() {
		LOGGER.info(basicMessage + " entregando moedas ao dono");
		runningDog.interrupt();
		coins += runningDog.getCoins();
		runningDog.setCoins(0);
		if(dogs[0].equals(runningDog)) {
			dogs[1].setFirstNode(true);
			dogs[1].start();
			runningDog = dogs[1];
			LOGGER.info(basicMessage + " entrando no bosque");
		} else {
			dogs[0].setFirstNode(true);
			dogs[0].start();
			runningDog = dogs[0];
			LOGGER.info(basicMessage + " entrando no bosque");
		}
	}

	public DogThread getRunningDog() {
		return runningDog;
	}
	
	public int getTotalCoins() {
		return coins + runningDog.getCoins();
	}

	public int getCoins() {
		return coins;
	}

	public Color getColor() {
		return color;
	}

	public void addFirstNode(Node firstNode) {
		for (DogThread dogThread : dogs) {
			LOGGER.info("Adicionando cão " + color.name() + " à entrada da floresta.");
			dogThread.setNode(firstNode);
		}
	}
	
	public void stopDogs() {
		for (DogThread dogThread : dogs) {
			dogThread.interrupt();
		}
	}
}