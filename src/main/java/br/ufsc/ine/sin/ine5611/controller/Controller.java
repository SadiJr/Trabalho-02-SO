package br.ufsc.ine.sin.ine5611.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.model.Forest;
import br.ufsc.ine.sin.ine5611.model.Hunter;
import br.ufsc.ine.sin.ine5611.thread.HelperThread;

public class Controller {
	private static final Logger LOGGER = LogManager.getLogger(Controller.class);
	
	private boolean winner = false;
	private Hunter[] hunters = new Hunter[3];
	
	public void init() {
		LOGGER.info("Iniciando floresta e caçadores");
		

		hunters[0] = new Hunter(Color.BLUE);
		hunters[1] = new Hunter(Color.GREEN);
		hunters[2] = new Hunter(Color.YELLOW);

		Forest forest = new Forest();

		HelperThread helperThread = new HelperThread(forest);

		for (Hunter hunter : hunters) {
			hunter.addFirstNode(forest.getFirstNode());
		}
		

		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (Hunter hunter : hunters) {
			executor.execute(hunter.getRunningDog());
		}
		executor.execute(helperThread);

		Runnable r = () -> {
			while(!winner) {
				for (Hunter hunter : hunters) {
					if(hunter.getTotalCoins() >= 50)
						winner = true;
				}
			}
			
			for (Hunter hunter : hunters) {
				hunter.stopDogs();
			}
			LOGGER.info("A caçada terminou! Iniciando a verificação de qual caçador foi o vencedor");
			verifyWinner();
		};
		new Thread(r).start();
	}

	private void verifyWinner() {
		int max = 0;
		int med = 0;
		
		Hunter first = null;
		Hunter second = null;
		Hunter third = null;
		
		for (Hunter hunter : hunters) {
			int totalCoins = hunter.getTotalCoins();
			if(totalCoins > max) {
				max = totalCoins;
				first = hunter;
			} else if (totalCoins > med) {
				med = totalCoins;
				second = hunter;
			} else {
				third = hunter;
			}
		}
		
		LOGGER.info("Posições:"
				+ "\n1º Colocado: Caçador " + first.getColor().name() + " com um total de " + first.getTotalCoins() + " moedas"
				+ "\n2º Colocado: Caçador " + second.getColor().name() + " com um total de " + second.getTotalCoins() + " moedas"
				+ "\n3º Colocado: Caçador " + third.getColor().name() + " com um total de " + third.getTotalCoins() + " moedas");
	}

}
