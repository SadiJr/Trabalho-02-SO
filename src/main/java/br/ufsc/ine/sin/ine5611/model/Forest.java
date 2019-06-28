package br.ufsc.ine.sin.ine5611.model;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.thread.DogThread;

public class Forest {
	private static final Logger LOGGER = LogManager.getLogger(Forest.class);

	private Node[] nodes;
	private boolean helperThreadWorking;
	private boolean dogThreadWorking;

	public Forest() {
		LOGGER.info("Criando nodos da floresta");
		nodes = new Node[20];
		for (int i = 0; i < 20; i++) {
			nodes[i] = new Node(i + 1);
		}
		createRelationsShip();
	}

	private void createRelationsShip() {
		LOGGER.info("Criando relação entre os nodos.");
		for (Node node : nodes) {
			switch (node.getId() - 1) {
			case 0:
				node.addNext(nodes[1]);
				node.addNext(nodes[14]);
				break;

			case 1:
				node.addNext(nodes[2]);
				node.addNext(nodes[3]);
				node.addNext(nodes[4]);
				break;

			case 2:
				node.addNext(nodes[1]);
				node.addNext(nodes[8]);
				break;

			case 3:
				node.addNext(nodes[1]);
				node.addNext(nodes[8]);
				node.addNext(nodes[9]);
				break;

			case 4:
				node.addNext(nodes[1]);
				node.addNext(nodes[5]);
				break;

			case 5:
				node.addNext(nodes[6]);
				node.addNext(nodes[7]);
				break;

			case 6:
			case 7:
				node.addNext(nodes[5]);
				break;

			case 8:
				node.addNext(nodes[2]);
				node.addNext(nodes[3]);
				node.addNext(nodes[14]);
				node.addNext(nodes[17]);
				break;

			case 9:
				node.addNext(nodes[3]);
				node.addNext(nodes[11]);
				break;

			case 10:
				node.addNext(nodes[11]);
				node.addNext(nodes[13]);
				node.addNext(nodes[16]);
				break;

			case 11:
				node.addNext(nodes[10]);
				node.addNext(nodes[12]);
				break;

			case 12:
				node.addNext(nodes[11]);
				break;

			case 13:
				node.addNext(nodes[10]);
				node.addNext(nodes[15]);
				break;

			case 14:
				node.addNext(nodes[0]);
				node.addNext(nodes[8]);
				break;

			case 15:
				node.addNext(nodes[13]);
				node.addNext(nodes[16]);
				node.addNext(nodes[17]);
				node.addNext(nodes[18]);
				node.addNext(nodes[19]);
				break;

			case 16:
				node.addNext(nodes[15]);
				node.addNext(nodes[10]);
				break;

			case 17:
				node.addNext(nodes[8]);
				node.addNext(nodes[15]);
				node.addNext(nodes[18]);
				break;

			case 18:
				node.addNext(nodes[17]);
				node.addNext(nodes[19]);
				break;

			case 19:
				node.addNext(nodes[15]);
				node.addNext(nodes[18]);
				break;

			default:
				LOGGER.info("Um erro ocorreu na criação das relações dos nodos!");
				break;
			}
		}
	}

	public synchronized void run(boolean isHelperThread, DogThread dog) {
		if (isHelperThread) {
			setHelperThreadWorking(true);
			while(isDogThreadWorking());
			LOGGER.info("Helper thread entrou na seção crítica");
			addCoinsToNode();
			setHelperThreadWorking(false);
		} else {
			setDogThreadWorking(true);
			while(isHelperThreadWorking());
			if(dog.getNode().nodeOcuppedByAnotherDog(dog)) {
				setDogThreadWorking(false);
			} else {
				if(dog.getNode().existCoins()) {
					getCoins(dog.getNode(), dog);
				} else {
					dog.setSleeping(true);
				}
				setHelperThreadWorking(false);
				this.notifyAll();
			}
		}
	}

	public synchronized void getCoins(Node node, DogThread dog) {
		String basicMessage = "Cão " + dog.getColor().name() + " " + dog.getId();
		LOGGER.info(basicMessage + " acessou a região crítica!");
		node.setDog(dog);

		dog.addCoins(node.getCoins());
		LOGGER.info(basicMessage + " dormindo após ter pegado as moedas");
	}

	public synchronized boolean moveDog(Node node, DogThread dog) {
		while (isHelperThreadWorking());
		while(isDogThreadWorking());
		setDogThreadWorking(true);
		String basicMessage = "Cão " + dog.getColor().name() + " " + dog.getId();
		LOGGER.info(basicMessage + " acordou após sua soneca depois de pegar moedas");
		int random = 0;
		for (int i = 0; i < node.getNexts().size(); i++) {
			random = (int) (Math.random() * node.getNexts().size());
			if (!node.getNexts().get(random).nodeOcuppedByAnotherDog(dog)) {
				node.getNexts().get(random).setDog(dog);
				node.setDog(null);
				LOGGER.info("Cão " + dog.getColor().name() + " " + dog.getId() + " dormindo após saltar do pote "
						+ node.getId() + " para o pote " + node.getNexts().get(random).getId());
				dog.setNode(node.getNexts().get(random));
				node.getNexts().get(random).setDog(dog);
				setDogThreadWorking(false);
				return true;
			}
		}
		setDogThreadWorking(false);
		return false;
	}

	public Node getFirstNode() {
		return nodes[0];
	}

	public synchronized void addCoinsToNode() {
		for (Node node : nodes) {
			if (!node.existCoins()) {
				LOGGER.info("Encontrado um pote vazio (" + node.getId() + ")! Adicionando uma moeda nele");
				node.addCoin();

				if (node.verifyExistsSleepingDogs()) {
					node.getSleepingDogs().forEach(d -> {
						LOGGER.info("Helper Thread encontrou cães dormindo no pote " + node.getId());
						LOGGER.info("Cão dormindo = " + d.getBasicMessage());
						d.run();
					});
				}
			}
		}
	}

	public boolean existsCoins(Node node) {
		return node.existCoins();
	}

	public synchronized void setHelperThreadWorking(boolean helperThreadWorking) {
		this.helperThreadWorking = helperThreadWorking;
	}

	public synchronized boolean isDogThreadWorking() {
		return dogThreadWorking;
	}

	public synchronized void setDogThreadWorking(boolean dogThreadWorking) {
		this.dogThreadWorking = dogThreadWorking;
	}
	
	private synchronized boolean isHelperThreadWorking() {
		return helperThreadWorking;
	}
}