package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.model.Hunter;
import br.ufsc.ine.sin.ine5611.model.Node;

public class DogThread extends Thread implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(DogThread.class);

	private int id;
	private Color color;
	private int coins;
	private Hunter hunter;
	private Node node;
	private boolean firstNode;
	private String basicMessage;

	public DogThread(int id, Color color, Hunter hunter) {
		this.id = id;
		this.color = color;
		this.hunter = hunter;
		coins = 0;
		setFirstNode(true);
		basicMessage = "Cão " + color.name() + " " + id;
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
		while (!Thread.currentThread().isInterrupted()) {
			if (firstNode) {
				if (node.existCoins()) {
					collectCoins();
					firstNode = false;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						LOGGER.error(e, e);
					}
				}
			} else {
				if (node.existCoins()) {
					collectCoins();
				} else {
					LOGGER.info(basicMessage + " não encontrou nenhuma moeda no pote " + node.getId());
					LOGGER.info(basicMessage + " dormindo até ser resgatado pela cão médico");
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						LOGGER.error(e, e);
					}

					while (!node.existCoins()) {
						LOGGER.info("Após acordar, o " + basicMessage + " ainda não encontrou nenhuma moeda no pote "
								+ node.getId() + ". Dormindo até ter moedas no pote para coletar");
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							LOGGER.error(e, e);
						}
					}
					collectCoins();
				}

			}
		}
	}

	private void collectCoins() {
		int nodeCoins = node.getCoins();
		addCoins(nodeCoins);

		if (!Thread.currentThread().isInterrupted()) {
			LOGGER.info(basicMessage + " dormindo após coletar " + nodeCoins + " moedas no pote " + node.getId());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOGGER.error(e, e);
			}
			int random = 0;
			do {
				random = (int) (Math.random() * node.getNexts().size());
			} while (node.getNexts().get(random).isDogOnNode());
			node.getNexts().get(random).setDogOnNode(true);
			LOGGER.info(basicMessage + " dormindo após saltar do pote " + node.getId() + " para o pote "
					+ node.getNexts().get(random).getId());
			node = node.getNexts().get(random);
		}
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setFirstNode(boolean firstNode) {
		this.firstNode = firstNode;
	}

	public void setCoins(int i) {
		coins = i;
	}
}