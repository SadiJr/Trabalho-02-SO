package br.ufsc.ine.sin.ine5611.model;

import java.util.ArrayList;
import java.util.List;

import br.ufsc.ine.sin.ine5611.thread.DogThread;

public class Node {
	private int id;
	private int coins;
	private List<DogThread> sleepingDogs;
	private List<Node> nexts;
	private DogThread dog;

	public Node(int id) {
		this.id = id;
		coins = 4;
		sleepingDogs = new ArrayList<>();
		nexts = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public synchronized List<DogThread> getSleepingDogs() {
		return sleepingDogs;
	}

	public synchronized void addSleepingDog(DogThread dog) {
		sleepingDogs.add(dog);
		dog.setSleeping(true);
	}

	public boolean verifyExistsSleepingDogs() {
		return !sleepingDogs.isEmpty();
	}

	public synchronized boolean existCoins() {
			return coins > 0;
	}

	public synchronized int getCoins() {
			if (coins >= 3) {
				coins -= 3;
				return 3;
			}
			int quantity = coins;
			coins = 0;
			return quantity;
	}

	public synchronized boolean nodeOcuppedByAnotherDog(DogThread d) {
		return dog != null && !dog.equals(d);
	}

	public synchronized List<Node> getNexts() {
		return nexts;
	}

	public void addNext(Node node) {
		nexts.add(node);
	}

	public synchronized void addCoin() {
			coins += 1;
	}

	public synchronized void setDog(DogThread dog) {
		this.dog = dog;
	}

	public synchronized DogThread getDog() {
		return dog;
	}


	public synchronized void removSleepingDog(List<DogThread> sleepingDogsToRemove) {
		sleepingDogs.removeAll(sleepingDogsToRemove);
	}
}