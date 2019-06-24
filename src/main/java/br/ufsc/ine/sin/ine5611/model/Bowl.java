package br.ufsc.ine.sin.ine5611.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Bowl {
	private static final Logger LOGGER = LogManager.getLogger(Bowl.class);
	
	private int id;
	private int coins;
	private boolean existAwakeDog;
	private List<Bowl> next;
	private List<Dog> sleepingDogs;
	
	public Bowl(int id) {
		this.id = id;
		this.coins = 4;
		this.next = new ArrayList<>();
		this.sleepingDogs = new ArrayList<>();
	}
	
	public void addCoin() {
		LOGGER.info("Adding a coin in bowl " + id);
		this.coins += 1;
	}
	
	public List<Dog> getSleepingDogs() {
		return sleepingDogs;
	}
	public void setSleepingDogs(List<Dog> sleepingDogs) {
		this.sleepingDogs = sleepingDogs;
	}
	
	public int getCoins() {
		if(coins >= 3) {
			this.coins -= 3;
			return 3;
		}
		int quantity = coins;
		this.coins = 0;
		return quantity;
	}
	
	public List<Bowl> getNext() {
		return next;
	}
	public void setNext(List<Bowl> next) {
		this.next = next;
	}
	
	public void addNext(Bowl bowl) {
		next.add(bowl);
	}

	public boolean isExistAwakeDog() {
		return existAwakeDog;
	}

	public void setExistAwakeDog(boolean existAwakeDog) {
		this.existAwakeDog = existAwakeDog;
	}

	public int getSpecificQuantity(int quantity) {
		this.coins -= quantity;
		return quantity;
	}
}