package br.ufsc.ine.sin.ine5611.model;

import br.ufsc.ine.sin.ine5611.controller.Controller;
import br.ufsc.ine.sin.ine5611.enums.Color;

public class MedicDog extends Dog {

	public MedicDog(Color color) {
		super(color);
	}

	@Override
	public void run() {
		Controller.getInstance().medicDogRun(this);
	}
}