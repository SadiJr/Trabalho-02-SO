package br.ufsc.ine.sin.ine5611;
public class Hunter {
	private int coinCount;
	private HunterDog[] dogs;
	private Color color;
	
	public Hunter(Color color) {
		this.color = color;
		coinCount = 0;
		dogs = new HunterDog[2];
		dogs[0] = new HunterDog(this, color);
		dogs[1] = new HunterDog(this, color);
	}
	
	public int getCoinCount() {
		return coinCount;
	}
	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}
	public Dog[] getDogs() {
		return dogs;
	}
	public void setDogs(HunterDog[] dogs) {
		this.dogs = dogs;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	} 
}