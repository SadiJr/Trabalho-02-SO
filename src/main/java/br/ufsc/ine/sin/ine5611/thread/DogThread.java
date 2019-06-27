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
		LOGGER.info(getBasicMessage() + " encontrou " + quantity + " moedas no pote " + node.getId());
		coins += quantity;
		LOGGER.info(getBasicMessage() + " agora possuí " + coins + " moedas");

		hunter.verifyWinner(node);

		if (coins >= 20) {
			node.setDog(null);
			hunter.switchDogs();
		}
	}

	@Override
	public void run() {
		LOGGER.info(getBasicMessage() + " iniciando a caçada!");
		while (running) {
			if (forest.lock()) {
				LOGGER.info("Lock with the thread " + getBasicMessage());

				LOGGER.info(
						"Verifiyng if exists god in node " + node.getId() + " = " + forest.existsDogOnNode(node, this));
				if (forest.existsDogOnNode(node, this)) {
					if (forest.existsCoins(node)) {
						forest.getCoins(node, this);
						if (running) {
							forest.unlock();
							try {
								sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (running) {
								boolean moving = true;
								while (moving) {
									if (forest.lock()) {
										while (!forest.moveDog(node, this)) {
											forest.unlock();
											try {
												sleep(1000);
											} catch (InterruptedException e) {
												e.printStackTrace();
												return;
											}
										}
										moving = false;
										forest.unlock();
									} else {
										forest.unlock();
										try {
											sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
											return;
										}
									}
								}
							}
							forest.unlock();
							try {
								sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							forest.unlock();
						}
					} else {
						node.setDog(null);
						node.addSleepingDog(this);
						forest.unlock();
						try {
							while (sleeping)
								sleep(1);
						} catch (InterruptedException e) {
							LOGGER.error(e, e);
						}
						LOGGER.info(basicMessage + " acordado pela HelperThread.");
					}
				} else {
					forest.unlock();
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						LOGGER.error(e, e);
						Thread.currentThread().interrupt();
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
}