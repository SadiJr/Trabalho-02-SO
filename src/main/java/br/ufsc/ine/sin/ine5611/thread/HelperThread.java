package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.model.Forest;
import br.ufsc.ine.sin.ine5611.model.Node;

public class HelperThread extends Thread implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(HelperThread.class);

	private Forest forest;

	public HelperThread(Forest forest) {
		LOGGER.info("Criando thread helper");
		this.forest = forest;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			LOGGER.info("Iniciando verificação de potes vazios");
			for (Node node : forest.getNodes()) {
				if (!node.existCoins()) {
					LOGGER.info("Encontrado um pote vazio (" + node.getId() + ")! Adicionando uma moeda nele");
					node.addCoin();
				}

				if (node.verifyExistsSleepingDogs()) {
					node.getSleepingDogs().forEach(DogThread::start);
					notifyAll();
				}
				try {
					sleep(200);
				} catch (InterruptedException e) {
					LOGGER.error(e, e);
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}