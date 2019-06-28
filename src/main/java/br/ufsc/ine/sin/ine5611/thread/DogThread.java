package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LogEvent;

import com.sun.swing.internal.plaf.basic.resources.basic;

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
	private Forest forest;
	private boolean running;
	private boolean sleeping;

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}

	public DogThread(int id, Color color, Hunter hunter, Forest forest) {
		this.id = id;
		this.color = color;
		this.hunter = hunter;
		coins = 0;
		setBasicMessage("Cão " + color.name() + " " + id);
		this.forest = forest;
		this.sleeping = false;
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

	public synchronized void addCoins(int quantity) {
		LOGGER.info(getBasicMessage() + " encontrou " + quantity + " moedas no pote " + node.getId());
		coins += quantity;
		LOGGER.info(getBasicMessage() + " agora possuí " + coins + " moedas");

		hunter.verifyWinner(node);

		if (coins >= 20) {
			node.setDog(null);
			forest.setHelperThreadWorking(false);
			hunter.switchDogs();
		}
	}

	@Override
	public void run() {
		LOGGER.info(getBasicMessage() + " iniciando a caçada!");
		while (running) {
			forest.run(false, this);
			if (sleeping) {
				try {
					LOGGER.info(basicMessage + " dormindo por não ter encontrado nenhuma moeda no pote");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOGGER.error(basicMessage + " acordou", e);
					sleeping = false;
					continue;
				}
			}
			try {
				LOGGER.info(basicMessage + " domindo"); 
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error(e, e);
			}

			while (!forest.moveDog(node, this)) {
				try {
					LOGGER.info(basicMessage + " dormindo por não conseguir saltar para outro pote");
					Thread.sleep(200);
				} catch (InterruptedException e) {
					LOGGER.error(e, e);
				}
			}
			try {
				LOGGER.info(basicMessage + " dormindo após conseguir saltar para outro pote");
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error(e, e);
			}
		}
		LOGGER.info("Deu ruim com a thread " + basicMessage);
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setCoins(int i) {
		coins = i;
	}

	public Hunter getHunter() {
		return hunter;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getBasicMessage() {
		return basicMessage;
	}

	public void setBasicMessage(String basicMessage) {
		this.basicMessage = basicMessage;
	}

	public Node getNode() {
		return node;
	}
}