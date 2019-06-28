package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.model.Forest;

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
			LOGGER.info("Iniciando verificação de potes vazios");
			forest.run(true, null);
			try {
				LOGGER.info("Helper Thread dormindo");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LOGGER.error(e,e);
			}
		}
		LOGGER.info("Help Thread Died");
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}