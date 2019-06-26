package br.ufsc.ine.sin.ine5611.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private int id;
	private int coins;
	private boolean dogOnNode;
	private List<Dog> sleepingDogs;
	private List<Node> nexts;
	
	public Node(int id) {
		this.id = id;
		coins = 4;
		setDogOnNode(false);
		sleepingDogs = new ArrayList<>();
		nexts = new ArrayList<>();
	}
	
	public int getId() {
		return id;
	}
	
	public List<Dog> getSleepingDogs() {
		return sleepingDogs;
	}
	
	public void addSleepingDog(Dog dog) {
		sleepingDogs.add(dog);
	}
	
	public boolean verifyExistsSleepingDogs() {
		return !sleepingDogs.isEmpty();
	}
	
	public boolean existCoins() {
		return coins > 0;
	}
	
	public int getCoins() {
		if(coins >= 3) {
			coins -= 3;
			return 3;
		}
		int quantity = coins;
		coins = 0;
		return quantity;
	}

	public boolean isDogOnNode() {
		return dogOnNode;
	}

	public void setDogOnNode(boolean dogOnNode) {
		this.dogOnNode = dogOnNode;
	}

	public List<Node> getNexts() {
		return nexts;
	}
	
	public void addNext(Node node) {
		nexts.add(node);
	}
}