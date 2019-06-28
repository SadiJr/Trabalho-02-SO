package br.ufsc.ine.sin.ine5611.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.ufsc.ine.sin.ine5611.enums.Color;
import br.ufsc.ine.sin.ine5611.model.Forest;
import br.ufsc.ine.sin.ine5611.model.Hunter;
import br.ufsc.ine.sin.ine5611.model.Node;

public class DogThread extends Thread {
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
		setBasicMessage("Cão " + color.name() + " " + id);
		LOGGER.info("Criando Thread " + basicMessage);
		this.id = id;
		this.color = color;
		this.hunter = hunter;
		coins = 0;
		this.forest = forest;
		this.sleeping = false;
		this.running = true;
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
		hunter.verifyWinner();
		if (coins >= 20) {
				node.setDog(null);
				hunter.switchDogs();
		}
	}

	@Override
	public void run() {
		LOGGER.info(getBasicMessage() + " iniciando a caçada! Status da thread " + getState());
		hunter.setRunningDog(this);
		while (running && !hunter.existsWinner()) {
			forest.run(false, this);
			if (sleeping) {
				try {
					LOGGER.info(basicMessage + " dormindo por não ter encontrado nenhuma moeda no pote");
					sleep(600);
				} catch (InterruptedException e) {
					LOGGER.error(basicMessage + " foi acordado pela Thread Helper", e);
					sleeping = false;
					continue;
				}
			}
			try {
				LOGGER.info(basicMessage + " dormindo");
				sleep(100);
			} catch (InterruptedException e) {
				if(hunter.existsWinner()) {
					LOGGER.info(basicMessage + " foi acordado para encerrar, uma vez que já existe vencedor. Running = " + running);
					continue;
				}
				hunter.printState();
				LOGGER.error(basicMessage + " teve seu sono interrompido!", e);
			}
			if(!running) {
				LOGGER.info(basicMessage + "Troca de contexto de threads. Running = " + running);
				continue;
			}
			
			while (!forest.moveDog(node, this)) {
				try {
					LOGGER.info(basicMessage + " dormindo por não conseguir saltar para outro pote");
					sleep(300);
				} catch (InterruptedException e) {
					if(hunter.existsWinner())
						continue;
					LOGGER.error(e, e);
				}
			}
			try {
				LOGGER.info(basicMessage + " dormindo após conseguir saltar para outro pote");
				sleep(100);
			} catch (InterruptedException e) {
				if(hunter.existsWinner())
					continue;
				LOGGER.error(e, e);
			}
		}
		LOGGER.info("Deu ruim com a thread " + basicMessage);
		hunter.printState();
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

	public synchronized Node getNode() {
		return node;
	}
}