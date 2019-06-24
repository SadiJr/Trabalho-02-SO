package br.ufsc.ine.sin.ine5611;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Grove {
	private static final Logger LOGGER = LogManager.getLogger(Grove.class);

	private Bowl[] bowls;

	public Grove() throws Exception {
		initializeBowls();
	}

	private void initializeBowls() throws Exception {
		LOGGER.info("Creating Bowls");

		setBowls(new Bowl[20]);
		for (int i = 0; i < 20; i++) {
			getBowls()[i] = new Bowl();
		}
		setBowlsRelationship();

	}

	private void setBowlsRelationship() throws Exception {
		LOGGER.info("Creating bowls relantionships");
		for (int i = 0; i < 20; i++) {
			switch (i) {
			case 0:
				getBowls()[i].addNext(getBowls()[1]);
				getBowls()[i].addNext(getBowls()[14]);
				break;
			case 1:
				getBowls()[i].addNext(getBowls()[0]);
				getBowls()[i].addNext(getBowls()[2]);
				getBowls()[i].addNext(getBowls()[3]);
				getBowls()[i].addNext(getBowls()[4]);
				break;
			case 2:
				getBowls()[i].addNext(getBowls()[1]);
				getBowls()[i].addNext(getBowls()[8]);
				break;
			case 3:
				getBowls()[i].addNext(getBowls()[1]);
				getBowls()[i].addNext(getBowls()[8]);
				getBowls()[i].addNext(getBowls()[9]);
				break;
			case 4:
				getBowls()[i].addNext(getBowls()[1]);
				getBowls()[i].addNext(getBowls()[5]);
				break;
			case 5:
				getBowls()[i].addNext(getBowls()[6]);
				getBowls()[i].addNext(getBowls()[7]);
				break;
			case 6:
				getBowls()[i].addNext(getBowls()[5]);
				break;
			case 7:
				getBowls()[i].addNext(getBowls()[5]);
				break;
			case 8:
				getBowls()[i].addNext(getBowls()[2]);
				getBowls()[i].addNext(getBowls()[3]);
				getBowls()[i].addNext(getBowls()[14]);
				getBowls()[i].addNext(getBowls()[17]);
				break;
			case 9:
				getBowls()[i].addNext(getBowls()[3]);
				getBowls()[i].addNext(getBowls()[11]);
				break;
			case 10:
				getBowls()[i].addNext(getBowls()[11]);
				getBowls()[i].addNext(getBowls()[13]);
				getBowls()[i].addNext(getBowls()[16]);
				break;
			case 11:
				getBowls()[i].addNext(getBowls()[9]);
				getBowls()[i].addNext(getBowls()[10]);
				getBowls()[i].addNext(getBowls()[12]);
				break;
			case 12:
				getBowls()[i].addNext(getBowls()[11]);
				break;
			case 13:
			case 16:
				getBowls()[i].addNext(getBowls()[10]);
				getBowls()[i].addNext(getBowls()[15]);
				break;
			case 14:
				getBowls()[i].addNext(getBowls()[0]);
				getBowls()[i].addNext(getBowls()[8]);
				break;
			case 15:
				getBowls()[i].addNext(getBowls()[13]);
				getBowls()[i].addNext(getBowls()[16]);
				getBowls()[i].addNext(getBowls()[17]);
				getBowls()[i].addNext(getBowls()[19]);
				break;
			case 17:
				getBowls()[i].addNext(getBowls()[8]);
				getBowls()[i].addNext(getBowls()[15]);
				getBowls()[i].addNext(getBowls()[18]);
				break;
			case 18:
				getBowls()[i].addNext(getBowls()[17]);
				getBowls()[i].addNext(getBowls()[19]);
				break;
			case 19:
				getBowls()[i].addNext(getBowls()[15]);
				getBowls()[i].addNext(getBowls()[18]);
				break;

			default:
				throw new Exception("A error ocurred in bowl relationships creation!");
			}
		}
	}

	public synchronized void hunterDogRun(HunterDog dog) {
		try {
			List<Bowl> next = dog.getBowl().getNext();
			int randomBowl = 0;
			do {
				randomBowl = (int) (Math.random() * next.size());
				if (!getBowls()[randomBowl].isExistAwakeDog()) {
					dog.setBowl(getBowls()[randomBowl]);

					if (dog.getBowl().getCoins() == 0) {
						dog.wait();
					}
					pickCoins(dog);

				}
			} while (!getBowls()[randomBowl].isExistAwakeDog());
		} catch (Exception e) {
			LOGGER.error(e, e);
		}
	}

	public void medicDogRun(MedicDog dog) throws Exception {
		for (Bowl bowl : getBowls()) {
			if (bowl.getCoins() < 0)
				throw new Exception();

			if (bowl.getCoins() == 0) {
				bowl.addCoin();

				if (bowl.isExistAwakeDog()) {
					bowl.getSleepingDogs().forEach(Dog::run);
				}
			}
		}
	}

	private synchronized void pickCoins(HunterDog dog) throws Exception {
		if (dog.getBowl().getCoins() < 0)
			throw new Exception("Unepected Error: coins in bowl is less than 0");

		if (dog.getBowl().getCoins() + dog.getCoinCount() > 20) {
			dog.addCoins(dog.getBowl().getSpecificQuantity(20 - dog.getBowl().getCoins()));
			Controller.getInstance().retryQueue(dog);
		} else {
			dog.addCoins(dog.getBowl().getCoins());
		}
	}

	public Bowl[] getBowls() {
		return bowls;
	}

	public void setBowls(Bowl[] bowls) {
		this.bowls = bowls;
	}
}