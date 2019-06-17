package br.ufsc.ine.sin.ine5611;

public class MedicDog extends Dog {

	public MedicDog(Color color) {
		super(color);
	}

	@Override
	public void run() {
		Controller.getInstance().medicDogRun(this);
	}
}