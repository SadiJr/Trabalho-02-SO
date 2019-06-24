package br.ufsc.ine.sin.ine5611;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Controller {
	private static final Logger LOGGER = LogManager.getLogger(Controller.class);
	
	private static final Controller instance = new Controller();
	private static final String FIRST_MESSAGE = "\nTendo um total de ";
	private static final String COINS = " moedas!";
	private Grove grove;
	private Hunter[] hunters;
	private MedicDog redDog;

	public static Controller getInstance() {
		return instance;
	}
	
	public void init() {
		try {
			LOGGER.info("Creating Grove");
			this.grove = new Grove();
			
			LOGGER.info("Creating Hunters");
			hunters = new Hunter[3];
			hunters[0] = new Hunter(Color.YELLOW);
			hunters[2] = new Hunter(Color.GREEN);
			hunters[1] = new Hunter(Color.BLUE);
			
			for (Hunter hunter : hunters) {
				HunterDog[] dogs = (HunterDog[]) hunter.getDogs();
				for (HunterDog dog : dogs) {
					dog.setBowl(grove.getBowls()[0]);
				}
			}
			
			this.redDog = new MedicDog(Color.RED);
			
			queue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void queue() {
		for (Hunter hunter : hunters) {
			hunter.getDogs()[0].run();
		}
		
	}

	public void hunterDogRun(HunterDog dog) {
		grove.hunterDogRun(dog);
		
		if(dog.getCoinCount() + dog.getHunter().getCoinCount() >= 50) {
			//TODO stop all threads
			for (Hunter hunter : hunters) {
				Dog[] dogs = hunter.getDogs();
				for (Dog d : dogs) {
					try {
						d.wait();
					} catch (InterruptedException e) {
						LOGGER.error(e);
					}
				}
			}
			LOGGER.info("A competição acabou e o resultado é:");
			LOGGER.info("1º Lugar:\nCaçador " + dog.getColor().name() + FIRST_MESSAGE +
					(dog.getHunter().getCoinCount() + dog.getCoinCount()) + COINS);
			LOGGER.info("2º Lugar:\nCaçador " + dog.getColor().name() + FIRST_MESSAGE +
					(dog.getHunter().getCoinCount() + dog.getCoinCount()) + COINS);
			LOGGER.info("3º Lugar:\nCaçador " + dog.getColor().name() + FIRST_MESSAGE +
					(dog.getHunter().getCoinCount() + dog.getCoinCount()) + COINS);
		}
	}

	public void medicDogRun(MedicDog medicDog) {
		
	}

	public void retryQueue(HunterDog dog) {
		Dog[] dogs = dog.getHunter().getDogs();
		if(dogs[0].equals(dog)) {
			dogs[1].run();
		} else {
			dogs[0].run();
		}
		try {
			dog.wait();
		} catch (InterruptedException e) {
			LOGGER.error(e);
		}
	}
}