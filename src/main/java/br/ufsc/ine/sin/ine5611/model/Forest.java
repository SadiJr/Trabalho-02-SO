package br.ufsc.ine.sin.ine5611.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.thread.DogThread;

public class Forest {
	private static final Logger LOGGER = LogManager.getLogger(Forest.class);

	private Node[] nodes;
	private boolean helperThreadWorking;
	private boolean dogThreadWorking;

	private DogThread dogInCriticalSection;

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
			if (isDogThreadWorking()) {
				LOGGER.info("Helper Thread aguardando para entrar na seção crítica.");
				return;
			}
			LOGGER.info("Helper thread entrou na seção crítica");
			addCoinsToNode();
			setHelperThreadWorking(false);
		} else {
			while (isHelperThreadWorking() && isDogThreadWorking()) {
				LOGGER.info(dog.getBasicMessage() + " aguardando para entra na seção crítica");
				LOGGER.info("Relatório do " + dog.getBasicMessage() + ": Helper Thread na seção crítica = "
						+ isHelperThreadWorking() + " e outro cão na seção crítica = " + isDogThreadWorking());
			}
			setDogThreadWorking(true, dog);
			setDogInCriticalSection(dog);
			LOGGER.info(dog.getBasicMessage() + " entrou na seção crítica! Atualmente no pote " + dog.getNode().getId());

			if (dog.getNode().nodeOcuppedByAnotherDog(dog)) {
				LOGGER.info(dog.getBasicMessage()
						+ " encontrou outro cão no pote! Saindo da seção crítica e liberando lock");
				setDogInCriticalSection(null);
				setDogThreadWorking(false, dog);
			} else {
				if (dog.getNode().existCoins()) {
					getCoins(dog.getNode(), dog);
				} else {
					LOGGER.info(dog.getBasicMessage() + " não encontrou nenhuma moeda no pote " + dog.getNode().getId()
							+ "! Saindo da seção crítica e liberando lock");
					dog.setSleeping(true);
					dog.getNode().setDog(null);
					dog.getNode().addSleepingDog(dog);
					setDogThreadWorking(false, dog);
					return;
				}
				setDogThreadWorking(false, dog);
				setDogInCriticalSection(null);
			}
			LOGGER.info("Relatório do " + dog.getBasicMessage() + ": Helper Thread na seção crítica = "
					+ isHelperThreadWorking() + " e outro cão na seção crítica = " + isDogThreadWorking());
		}
	}

	public synchronized void getCoins(Node node, DogThread dog) {
		dog.addCoins(node.getCoins());
	}

	public synchronized boolean moveDog(Node node, DogThread dog) {
		while (isHelperThreadWorking() && isDogThreadWorking())
			;
		setDogThreadWorking(true, dog);
		setDogInCriticalSection(dog);
		LOGGER.info(dog.getBasicMessage() + " acordou após sua soneca depois de pegar moedas");
		int random = 0;
		for (int i = 0; i < node.getNexts().size(); i++) {
			random = (int) (Math.random() * node.getNexts().size());
			if (!node.getNexts().get(random).nodeOcuppedByAnotherDog(dog)) {
				node.getNexts().get(random).setDog(dog);
				node.setDog(null);
				LOGGER.info("Cão " + dog.getColor().name() + " " + dog.getId() + " dormindo após saltar do pote "
						+ node.getId() + " para o pote " + node.getNexts().get(random).getId());
				LOGGER.info(dog.getBasicMessage() + " setando pote " + node.getNexts().get(random).getId()
						+ " para si. O pote possuí atualmente o cão"
						+ (node.getNexts().get(random).getDog() == null ? "null"
								: node.getNexts().get(random).getDog().getBasicMessage()));
				dog.setNode(node.getNexts().get(random));
				node.getNexts().get(random).setDog(dog);
				setDogThreadWorking(false, dog);
				return true;
			}
		}
		setDogThreadWorking(false, dog);
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

				List<DogThread> sleepingDogsToRemove = new ArrayList<>();
				if (node.verifyExistsSleepingDogs()) {
					node.getSleepingDogs().forEach(d -> {
						LOGGER.info("Helper Thread encontrou cães dormindo no pote " + node.getId());
						LOGGER.info("Cão dormindo = " + d.getBasicMessage());
						d.interrupt();
						sleepingDogsToRemove.add(d);

					});
					node.removSleepingDog(sleepingDogsToRemove);
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

	public synchronized void setDogThreadWorking(boolean dogThreadWorking, DogThread dog) {
		LOGGER.info(dog.getBasicMessage() + " alterando valor do lock global dos cães de " + this.dogThreadWorking
				+ " para " + dogThreadWorking);
		this.dogThreadWorking = dogThreadWorking;
	}

	private synchronized boolean isHelperThreadWorking() {
		return helperThreadWorking;
	}

	public DogThread getDogInCriticalSection() {
		return dogInCriticalSection;
	}

	public void setDogInCriticalSection(DogThread dogInCriticalSection) {
		this.dogInCriticalSection = dogInCriticalSection;
	}
}