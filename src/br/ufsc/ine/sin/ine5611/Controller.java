package br.ufsc.ine.sin.ine5611;

public class Controller {
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
			this.grove = new Grove();
			hunters = new Hunter[30];
			hunters[0] = new Hunter(Color.YELLOW);
			hunters[2] = new Hunter(Color.GREEN);
			hunters[1] = new Hunter(Color.BLUE);
			
			this.redDog = new MedicDog(Color.RED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void hunterDogRun(HunterDog dog) {
		grove.hunterDogRun(dog);
		
		if(dog.getCoinCount() + dog.getHunter().getCoinCount() >= 50) {
			//TODO stop all threads
			System.out.println("A competição acabou e o resultado é:");
			System.out.println("1º Lugar:\nCaçador " + dog.getColor().name() + FIRST_MESSAGE +
					(dog.getHunter().getCoinCount() + dog.getCoinCount()) + COINS);
			System.out.println("2º Lugar:\nCaçador " + dog.getColor().name() + FIRST_MESSAGE +
					(dog.getHunter().getCoinCount() + dog.getCoinCount()) + COINS);
			System.out.println("3º Lugar:\nCaçador " + dog.getColor().name() + FIRST_MESSAGE +
					(dog.getHunter().getCoinCount() + dog.getCoinCount()) + COINS);
		}
	}

	public void medicDogRun(MedicDog medicDog) {
		
	}
}