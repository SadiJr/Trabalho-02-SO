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
	private Forest forest;

	public DogThread(int id, Color color, Hunter hunter, Forest forest) {
		this.id = id;
		this.color = color;
		this.hunter = hunter;
		coins = 0;
		basicMessage = "Cão " + color.name() + " " + id;
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

		hunter.verifyWinner();

		if (coins >= 20) {
			node.setDog(null);
			hunter.switchDogs();
		}
	}

	@Override
	public void run() {
			LOGGER.info(basicMessage + " iniciando a caçada!");
			while (!Thread.currentThread().isInterrupted()) {
				if (forest.lock()) {
					LOGGER.info("Lock with the thread " + basicMessage);
					
					LOGGER.info("Verifiyng if exists god in node " +node.getId() + " = " +  forest.existsDogOnNode(node, this));
					if (forest.existsDogOnNode(node, this)) {
						if (forest.existsCoins(node)) {
							forest.getCoins(node, this);
							forest.unlock();
							try {
								sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							while (!forest.lock());
							while(!forest.moveDog(node, this)) {
								forest.unlock();
								try {
									sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								while (!forest.lock());
							}
							forest.unlock();
							try {
								sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							node.setDog(null);
							node.addSleepingDog(this);
							forest.unlock();
							try {
								sleep(6000);
							} catch (InterruptedException e) {
								LOGGER.error(e,e);
							}
							hunter.printState();
						}
					} else {
						forest.unlock();
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							LOGGER.error(e,e);
						}
					}
				}
			}
			forest.unlock();
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
}