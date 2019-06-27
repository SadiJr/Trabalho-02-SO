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
	private Forest forest;

	public Hunter(Color color, Forest forest, Controller controller) {
		this.color = color;
		this.forest = forest;
		this.controller = controller;
		dogs[0] = new DogThread(1, color, this, forest);
		dogs[1] = new DogThread(2, color, this, forest);
		runningDog = dogs[0];
	}

	public void switchDogs() {
		LOGGER.info(runningDog.getBasicMessage() + " entregando moedas ao dono");
		coins += runningDog.getCoins();
		
		LOGGER.info("Caçador " + color.name() + " com um total de " + coins);
		if (dogs[0].equals(runningDog)) {
			runningDog.setRunning(false);
			runningDog.setSleeping(false);
			runningDog = dogs[1];
			LOGGER.info(runningDog.getBasicMessage() + " entrando no bosque");
			runningDog.setRunning(true);
			runningDog.start();
		} else {
			runningDog.setRunning(false);
			runningDog.setSleeping(false);
			runningDog = dogs[0];
			LOGGER.info(runningDog.getBasicMessage() + " entrando no bosque");
			runningDog.setRunning(true);
			runningDog.setSleeping(false);
			runningDog.run();
		}
	}

	public DogThread getRunningDog() {
		return runningDog;
	}

	public synchronized int getTotalCoins() {
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

	public void verifyWinner(Node node) {
		if (getTotalCoins() >= 50) {
			node.setDog(null);
			controller.stopAll(this);
		}
	}

	public void stopDogs() {
		for (DogThread dogThread : dogs) {
			dogThread.setRunning(false);
			dogThread.setSleeping(false);
			try {
				dogThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void printState() {
		controller.printState();
	}
}