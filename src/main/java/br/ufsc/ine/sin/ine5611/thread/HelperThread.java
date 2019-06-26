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
		LOGGER.info("Iniciando verificação de potes vazios");
		while (!Thread.currentThread().isInterrupted()) {
			for (Node node : forest.getNodes()) {
				LOGGER.info("Verificando pote " + node.getId());
				if (!node.existCoins()) {
					LOGGER.info("Encontrado um pote vazio (" + node.getId() + ")! Adicionando uma moeda nele");
					node.addCoin();
				}
			}
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				LOGGER.error(e, e);
				Thread.currentThread().interrupt();
			}
		}
	}
}