package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.model.Forest;
import br.ufsc.ine.sin.ine5611.model.Hunter;
import br.ufsc.ine.sin.ine5611.model.Node;

public class DogThread extends Thread implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(DogThread.class);

	private int id;
	private Color color;
	private int coins;
	private Hunter hunter;
	private Node node;
	private String basicMessage;
	private boolean running;
	private Forest forest;

	public DogThread(int id, Color color, Hunter hunter, Forest forest) {
		this.id = id;
		this.color = color;
		this.hunter = hunter;
		coins = 0;
		basicMessage = "Cão " + color.name() + " " + id;
		running = true;
		this.forest = forest;
	}

	public long getId() {
		return id;
	}

	public Color getColor() {
		return color;
	}

	public int getCoins() {
		return coins;
	}

	public void addCoins(int quantity) {
		LOGGER.info(basicMessage + " encontrou " + quantity + " moedas no pote " + node.getId());
		coins += quantity;
		LOGGER.info(basicMessage + " agora possuí " + coins + " moedas");
		if (coins >= 20)
			hunter.switchDogs();
	}

	@Override
	public void run() {
		LOGGER.info(basicMessage + " iniciando a caçada!");
		while (running) {
			forest.getCoins(node, this);
		}
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setCoins(int i) {
		coins = i;
	}
}