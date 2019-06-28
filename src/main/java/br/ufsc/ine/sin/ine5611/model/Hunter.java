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
		runningDog.interrupt();

		printStateOfThreadsAfter();

		LOGGER.info("Variável runningDog é " + runningDog.getBasicMessage());
		if (dogs[0].equals(runningDog)) {
			LOGGER.info("Substituindo " + runningDog.getBasicMessage() + " por " + dogs[1].getBasicMessage());
			LOGGER.info(dogs[1].getBasicMessage() + " entrando no bosque");
			runningDog = dogs[1];
			LOGGER.info("Estado da Thread " + runningDog.getBasicMessage() + " depois da troca de contexto:\nRunning -> "
					+ runningDog.isRunning() + "; Alive -> " + runningDog.isAlive() + "; Interrupted -> "
					+ runningDog.isInterrupted() + "; State -> " + runningDog.getState() + "\n");
			if (runningDog.getState().equals(State.NEW))
				runningDog.start();

			if (runningDog.getState().equals(State.TERMINATED)) {
				dogs[1] = new DogThread(1, color, this, forest);
				dogs[1].setRunning(true);
				dogs[1].setSleeping(false);
				runningDog = dogs[1];
				runningDog.start();
			}
		} else {
			LOGGER.info("Substituindo " + runningDog.getBasicMessage() + " por " + dogs[0].getBasicMessage());
			LOGGER.info(dogs[0].getBasicMessage() + " entrando no bosque");
			LOGGER.info("Estado da Thread " + runningDog.getBasicMessage() + " depois da troca de contexto:\nRunning -> "
					+ runningDog.isRunning() + "; Alive -> " + runningDog.isAlive() + "; Interrupted -> "
					+ runningDog.isInterrupted() + "; State -> " + runningDog.getState() + "\n");
			if (dogs[0].getState().equals(State.NEW)) {
				LOGGER.info("Iniciando thread " + dogs[0].getBasicMessage());
				dogs[0].start();
			}

			if (runningDog.getState().equals(State.TERMINATED)) {
				dogs[0] = new DogThread(2, color, this, forest);
				dogs[0].setRunning(true);
				dogs[0].setSleeping(false);
				dogs[0].start();
				runningDog = dogs[0];
			}
			runningDog = dogs[0];
		}
		LOGGER.info("Variável runningDog agora é " + runningDog.getBasicMessage());
		printStateOfThreadBefore();
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

	public synchronized boolean verifyWinner(Node node) {
		if (getTotalCoins() >= 50) {
			LOGGER.info("Temos um vencedor!");
			controller.setWinner(true);
			node.setDog(null);
			controller.stopAll();
		}
		return controller.getWinner();
	}

	public synchronized void stopDogs() {
		for (DogThread dogThread : dogs) {
			dogThread.setRunning(false);
			dogThread.setSleeping(false);
			forest.setDogThreadWorking(false, dogThread);
			dogThread.interrupt();
			try {
				dogThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean existsWinner() {
		return controller.getWinner();
	}

	public synchronized void printState() {
//		controller.printState();
	}

	public DogThread[] getDogs() {
		return dogs;
	}
}