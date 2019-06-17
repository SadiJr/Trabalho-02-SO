package br.ufsc.ine.sin.ine5611;

import java.util.List;

public class Grove {
	private Bowl[] bowls;

	public Grove() throws Exception {
		initializeBowls();
	}

	private void initializeBowls() throws Exception {
		bowls = new Bowl[20];
		for (int i = 0; i < 20; i++) {
			bowls[i] = new Bowl();
		}
		setBowlsRelationship();

	}

	private void setBowlsRelationship() throws Exception {
		for (int i = 0; i < 20; i++) {
			switch (i) {
			case 0:
				bowls[i].addNext(bowls[1]);
				bowls[i].addNext(bowls[14]);
				break;
			case 1:
				bowls[i].addNext(bowls[0]);
				bowls[i].addNext(bowls[2]);
				bowls[i].addNext(bowls[3]);
				bowls[i].addNext(bowls[4]);
				break;
			case 2:
				bowls[i].addNext(bowls[1]);
				bowls[i].addNext(bowls[8]);
				break;
			case 3:
				bowls[i].addNext(bowls[1]);
				bowls[i].addNext(bowls[8]);
				bowls[i].addNext(bowls[9]);
				break;
			case 4:
				bowls[i].addNext(bowls[1]);
				bowls[i].addNext(bowls[5]);
				break;
			case 5:
				bowls[i].addNext(bowls[6]);
				bowls[i].addNext(bowls[7]);
				break;
			case 6:
				bowls[i].addNext(bowls[5]);
				break;
			case 7:
				bowls[i].addNext(bowls[5]);
				break;
			case 8:
				bowls[i].addNext(bowls[2]);
				bowls[i].addNext(bowls[3]);
				bowls[i].addNext(bowls[14]);
				bowls[i].addNext(bowls[17]);
				break;
			case 9:
				bowls[i].addNext(bowls[3]);
				bowls[i].addNext(bowls[11]);
				break;
			case 10:
				bowls[i].addNext(bowls[11]);
				bowls[i].addNext(bowls[13]);
				bowls[i].addNext(bowls[16]);
				break;
			case 11:
				bowls[i].addNext(bowls[9]);
				bowls[i].addNext(bowls[10]);
				bowls[i].addNext(bowls[12]);
				break;
			case 12:
				bowls[i].addNext(bowls[11]);
				break;
			case 13:
			case 16:
				bowls[i].addNext(bowls[10]);
				bowls[i].addNext(bowls[15]);
				break;
			case 14:
				bowls[i].addNext(bowls[0]);
				bowls[i].addNext(bowls[8]);
				break;
			case 15:
				bowls[i].addNext(bowls[13]);
				bowls[i].addNext(bowls[16]);
				bowls[i].addNext(bowls[17]);
				bowls[i].addNext(bowls[19]);
				break;
			case 17:
				bowls[i].addNext(bowls[8]);
				bowls[i].addNext(bowls[15]);
				bowls[i].addNext(bowls[18]);
				break;
			case 18:
				bowls[i].addNext(bowls[17]);
				bowls[i].addNext(bowls[19]);
				break;
			case 19:
				bowls[i].addNext(bowls[15]);
				bowls[i].addNext(bowls[18]);
				break;

			default:
				throw new Exception("A error ocurred in bowl relationships creation!");
			}
		}
	}

	public void hunterDogRun(HunterDog dog) {
		try {
			List<Bowl> next = dog.getBowl().getNext();
			int randomBowl = 0;
			do {
				randomBowl = (int) (Math.random() * next.size());
				if (!bowls[randomBowl].isExistAwakeDog()) {
					dog.setBowl(bowls[randomBowl]);
					
					if (dog.getBowl().getCoins() == 0)
						dog.wait(100);
					pickCoins(dog);

				}
			} while (!bowls[randomBowl].isExistAwakeDog());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void medicDogRun(MedicDog dog) throws Exception {
		for (Bowl bowl : bowls) {
			if (bowl.getCoins() < 0) 
				throw new Exception();
			
			if(bowl.getCoins() == 0) {
				bowl.addCoin();
				
				if(bowl.isExistAwakeDog()) {
					bowl.getSleepingDogs().forEach(Dog::run);
				}
			}
		}
	}

	private synchronized void pickCoins(HunterDog dog) throws Exception {
		if (dog.getBowl().getCoins() < 0)
			throw new Exception("Unepected Error: coins in bowl is less than 0");
		
		if(dog.getBowl().getCoins() + dog.getCoinCount() > 20)
			dog.addCoins(dog.getBowl().getSpecificQuantity(20 - dog.getBowl().getCoins()));
		else
			dog.addCoins(dog.getBowl().getCoins());
	}
}