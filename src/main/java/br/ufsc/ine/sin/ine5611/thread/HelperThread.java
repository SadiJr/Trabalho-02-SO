package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.model.Forest;

public class HelperThread extends Thread implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(HelperThread.class);
	
	private Forest forest;
	
	public HelperThread(Forest forest) {
		this.forest = forest;
	}
	
	@Override
	public void run() {
		LOGGER.info("Iniciando verificação de potes vazios");
		
	}
}