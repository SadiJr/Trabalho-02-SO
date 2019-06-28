package br.ufsc.ine.sin.ine5611.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.model.Forest;
import br.ufsc.ine.sin.ine5611.model.Hunter;
import br.ufsc.ine.sin.ine5611.model.Node;
import br.ufsc.ine.sin.ine5611.thread.HelperThread;

public class Controller {
	private static final Logger LOGGER = LogManager.getLogger(Controller.class);

	private Hunter[] hunters = new Hunter[3];
	private HelperThread helperThread;
	private Forest forest;

	public void init() {
		LOGGER.info("Iniciando floresta e caçadores");

		forest = new Forest();
		hunters[0] = new Hunter(Color.BLUE, forest, this);
		hunters[1] = new Hunter(Color.GREEN, forest, this);
		hunters[2] = new Hunter(Color.YELLOW, forest, this);

		helperThread = new HelperThread(forest);

		for (Hunter hunter : hunters) {
			hunter.addFirstNode(forest.getFirstNode());
		}

		for (Hunter hunter : hunters) {
			hunter.getRunningDog().setRunning(true);
			hunter.getRunningDog().start();
		}
		helperThread.setRunning(true);
		helperThread.start();
	}

	private void printWinners() {
		int max = 0;
		int med = 0;

		Hunter first = null;
		Hunter second = null;
		Hunter third = null;

		for (Hunter hunter : hunters) {
			int totalCoins = hunter.getTotalCoins();
			if (totalCoins > max) {
				max = totalCoins;
				first = hunter;
			} else if (totalCoins > med) {
				med = totalCoins;
				second = hunter;
			} else {
				third = hunter;
			}
		}

		LOGGER.info("Posições:" + "\n1º Colocado: Caçador " + first.getColor().name() + " com um total de "
				+ first.getTotalCoins() + " moedas" + "\n2º Colocado: Caçador " + second.getColor().name()
				+ " com um total de " + second.getTotalCoins() + " moedas" + "\n3º Colocado: Caçador "
				+ third.getColor().name() 
				+ " com um total de " + third.getTotalCoins() + " moedas");
	}

	public void stopAll(Hunter hunter) {
		printWinners();
		for (Hunter h : hunters) {
			h.stopDogs();
		}
		helperThread.setRunning(false);
	}
}