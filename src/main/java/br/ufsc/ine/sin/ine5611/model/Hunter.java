package br.ufsc.ine.sin.ine5611.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.controller.Controller;
import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.thread.DogThread;

public class Hunter {
	private static final Logger LOGGER = LogManager.getLogger(Hunter.class);

	private Controller controller;
	private Color color;
	private int coins;
	private DogThread[] dogs = new DogThread[2];
	private DogThread runningDog;
	private String basicMessage;
	private Forest forest;

	public Hunter(Color color, Forest forest, Controller controller) {
		this.color = color;
		this.forest = forest;
		this.controller = controller;
		dogs[0] = new DogThread(1, color, this, forest);
		dogs[1] = new DogThread(2, color, this, forest);
		runningDog = dogs[0];
		basicMessage = "Cão " + color.name() + " " + runningDog.getId();
	}

	public void switchDogs() {
		LOGGER.info(basicMessage + " entregando moedas ao dono");
		coins += runningDog.getCoins();
		if (dogs[0].equals(runningDog)) {
			runningDog.interrupt();
			runningDog = dogs[1];
			LOGGER.info(basicMessage + " entrando no bosque");
			dogs[1].start();
		} else {
			runningDog.interrupt();
			LOGGER.info(basicMessage + " entrando no bosque");
			if (dogs[0].isAlive()) {
				dogs[0] = new DogThread(1, color, this, forest);
				runningDog = dogs[0];
				runningDog.start();
			} else {
				runningDog = dogs[0];
				runningDog.start();
			}
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

	public void verifyWinner() {
		if (getTotalCoins() >= 50)
			controller.stopAll(this);
	}

	public void stopDogs() {
		for (DogThread dogThread : dogs) {
			dogThread.interrupt();
		}
	}

	public void printState() {
		controller.printState();
	}
}