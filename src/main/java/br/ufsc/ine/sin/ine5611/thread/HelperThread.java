package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.model.Forest;
import br.ufsc.ine.sin.ine5611.model.Node;

public class HelperThread extends Thread implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(HelperThread.class);

	private Forest forest;
	private boolean running;

	public HelperThread(Forest forest) {
		LOGGER.info("Criando thread helper");
		this.forest = forest;
	}

	@Override
	public void run() {
		while (running) {
			if(forest.lock()) {
				LOGGER.info("Iniciando verificação de potes vazios");
				LOGGER.info("Lock with helper thread");
				forest.addCoinsToNode();
				forest.unlock();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					LOGGER.error(e, e);
				}
			}
		}
		LOGGER.info("Help Thread Died");
		forest.unlock();
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}