package br.ufsc.ine.sin.ine5611.enums;

public enum Color {
	RED("Red"), BLUE("Blue"), GREEN("Green"), YELLOW("Yellow");

	private String colorType;

	Color(String color) {
		this.colorType = color;
	}

	public String getColor() {
		return colorType;
	}
}