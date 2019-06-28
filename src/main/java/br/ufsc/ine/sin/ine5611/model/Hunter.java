package br.ufsc.ine.sin.ine5611.model;

import java.lang.Thread.State;

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

	public synchronized void switchDogs() {
		LOGGER.info(runningDog.getBasicMessage() + " entregando moedas ao dono");
		coins += runningDog.getCoins();

		LOGGER.info("Caçador " + color.name() + " com um total de " + coins);
		runningDog.setRunning(false);
		runningDog.setSleeping(false);
		forest.setDogInCriticalSection(null);
		runningDog.interrupt();

		LOGGER.info("Variável runningDog é " + runningDog.getBasicMessage());
		if (dogs[0].equals(runningDog)) {
//			Cachorro que deve entrar = dog[1]
			LOGGER.info("Substituindo " + runningDog.getBasicMessage() + " por " + dogs[1].getBasicMessage());
			LOGGER.info(dogs[1].getBasicMessage() + " entrando no bosque");
			if (dogs[1].getState().equals(State.NEW)) {
				LOGGER.info("Iniciando thread " + dogs[1].getBasicMessage());
				dogs[1].start();
			} else {
				dogs[1] = new DogThread(2, color, this, forest);
				dogs[1].setNode(forest.getFirstNode());
				dogs[1].start();
			}
			LOGGER.info("Estado da Thread " + dogs[1].getBasicMessage() + " depois da troca de contexto:\nRunning -> "
					+ dogs[1].isRunning() + "; Alive -> " + dogs[1].isAlive() + "; Interrupted -> "
					+ dogs[1].isInterrupted() + "; State -> " + dogs[1].getState() + "\n");
		} else {
			LOGGER.info("Substituindo " + runningDog.getBasicMessage() + " por " + dogs[0].getBasicMessage());
			LOGGER.info(dogs[0].getBasicMessage() + " entrando no bosque");
			if (dogs[0].getState().equals(State.NEW)) {
				LOGGER.info("Iniciando thread " + dogs[0].getBasicMessage());
				dogs[0].start();
			} else {
				dogs[0] = new DogThread(1, color, this, forest);
				dogs[0].setNode(forest.getFirstNode());
				dogs[0].start();
			}
			LOGGER.info("Estado da Thread " + dogs[0].getBasicMessage() + " depois da troca de contexto:\nRunning -> "
					+ dogs[0].isRunning() + "; Alive -> " + dogs[0].isAlive() + "; Interrupted -> "
					+ dogs[0].isInterrupted() + "; State -> " + dogs[0].getState() + "\n");
		}
	}

	private void printStateOfThreadBefore() {
		for (DogThread dogThread : dogs) {
			LOGGER.warn("Estado da Thread " + dogThread.getBasicMessage() + " depois da troca de contexto:\nRunning -> "
					+ dogThread.isRunning() + "; Alive -> " + dogThread.isAlive() + "; Interrupted -> "
					+ dogThread.isInterrupted() + "; State -> " + dogThread.getState() + "\n");
		}
	}

	private void printStateOfThreadsAfter() {
		for (DogThread dogThread : dogs) {
			LOGGER.warn("Estado da Thread " + dogThread.getBasicMessage() + " antes da troca de contexto:\nRunning -> "
					+ dogThread.isRunning() + "; Alive -> " + dogThread.isAlive() + "; Interrupted -> "
					+ dogThread.isInterrupted() + "; State -> " + dogThread.getState() + "\n");
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

	public synchronized boolean verifyWinner() {
		if (getTotalCoins() >= 50) {
			LOGGER.info("Temos um vencedor! Que é o caçador " + color.name() + " com um total de " + getTotalCoins());
			controller.setWinner(true);
			controller.stopAll();
		}
		return controller.getWinner();
	}

	public synchronized void stopDogs() {
		for (DogThread dogThread : dogs) {
			dogThread.setRunning(false);
			dogThread.setSleeping(false);
			dogThread.interrupt();
		}
		
	}

	public synchronized boolean existsWinner() {
		return controller.getWinner();
	}

	public synchronized void printState() {
//		controller.printState();
	}

	public DogThread[] getDogs() {
		return dogs;
	}

	public synchronized void setRunningDog(DogThread dogThread) {
		this.runningDog = dogThread;
	}
}