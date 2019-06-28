package br.ufsc.ine.sin.ine5611.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.model.Forest;
import br.ufsc.ine.sin.ine5611.model.Hunter;
import br.ufsc.ine.sin.ine5611.thread.DogThread;
import br.ufsc.ine.sin.ine5611.thread.HelperThread;

public class Controller {
	private static final Logger LOGGER = LogManager.getLogger(Controller.class);

	private static final String COM_UM_TOTAL_DE = " com um total de ";
	private static final String MOEDAS = " moedas";

	private Hunter[] hunters = new Hunter[3];
	private HelperThread helperThread;
	private boolean winner;
	
	private Hunter first = null;
	private Hunter second = null;
	private Hunter third = null;

	public void init() {
		LOGGER.info("Iniciando floresta e caçadores");

		Forest forest = new Forest();
		hunters[0] = new Hunter(Color.BLUE, forest, this);
		hunters[1] = new Hunter(Color.GREEN, forest, this);
		hunters[2] = new Hunter(Color.YELLOW, forest, this);

		helperThread = new HelperThread(forest);

		for (Hunter hunter : hunters) {
			hunter.addFirstNode(forest.getFirstNode());
		}

		winner = false;
		for (Hunter hunter : hunters) {
			hunter.getRunningDog().setRunning(true);
			hunter.getRunningDog().start();
		}
		helperThread.setRunning(true);
		helperThread.start();
	}

	private synchronized void printWinners() {
		LOGGER.info("\n\n\n\n\nPosições:" + "\n1º Colocado: Caçador " + first.getColor().name() + COM_UM_TOTAL_DE
				+ first.getTotalCoins() + MOEDAS + "\n2º Colocado: Caçador " + second.getColor().name()
				+ COM_UM_TOTAL_DE + second.getTotalCoins() + MOEDAS + "\n3º Colocado: Caçador "
				+ third.getColor().name() 
				+ COM_UM_TOTAL_DE + third.getTotalCoins() + MOEDAS + "\n\n\n\n\n");
	}

	public synchronized void stopAll() {
		LOGGER.info("Obtendo os colocados");
		getWinners();
		helperThread.setRunning(false);
		helperThread.interrupt();
		for (Hunter h : hunters) {
			h.stopDogs();
		}
		printWinners();
	}

	private synchronized void getWinners() {
		int max = 49;
		int med = 0;
		
		LOGGER.info("Iniciando a análise dos candidatos à 1º lugar");
		
		for (Hunter hunter : hunters) {
			int totalCoins = hunter.getTotalCoins();
			LOGGER.info("Caçador " + hunter.getColor().name() + " conseguiu " + totalCoins + " moedas");
			if (totalCoins > max) {
				max = totalCoins;
				if(first != null) {
					if(second != null) {
						if(second.getTotalCoins() < totalCoins) {
							third = second;
							second = hunter;
						} else {
							third = hunter;
						}
					} else {
						second = first;
					}
				} else {
					first = hunter;
				}
				LOGGER.info("1º Colocado é " + hunter.getColor().name());
			} else if (totalCoins > med) {
				med = totalCoins;
				if(second != null) {
					if(second.getTotalCoins() > totalCoins) {
						third = hunter;
					} else {
						third = second;
						second = hunter;
					}
				} else {
					second = hunter;
				}
				LOGGER.info("2º Colocado é " + hunter.getColor().name());
			} else {
				third = hunter;
				LOGGER.info("3º Colocado é " + hunter.getColor().name());
			}
		}
	}

	public synchronized void setWinner(boolean b) {
		this.winner = b;
	}

	public boolean getWinner() {
		return winner;
	}

	public synchronized void printState() {
		for (Hunter hunter : hunters) {
			LOGGER.info("\n\nCaçador " + hunter.getColor().name() + " com cão rodando " + hunter.getRunningDog().getBasicMessage());
			LOGGER.info("Estados das Threads do caçador " + hunter.getColor().name() + ":");
			for (DogThread dog : hunter.getDogs()) {
				LOGGER.info("Cão " + dog.getBasicMessage() + ": Running " + dog.isRunning() + " e alive = " + dog.isAlive() + " e interrompida " + dog.isInterrupted());
				LOGGER.info(dog.getBasicMessage() + " informações ndo etado da thread na JVM = " + dog.getState().name() +"\n\n");
			}
		}
	}
}