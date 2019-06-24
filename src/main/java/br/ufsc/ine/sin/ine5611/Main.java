package br.ufsc.ine.sin.ine5611;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.controller.Controller;

public class Main {
	private static final Logger LOGGER = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) {
		LOGGER.info("Start Application");
		Controller.getInstance().init();
	}
}