package br.ufsc.ine.sin.ine5611;

public class HunterDog extends Dog {
	private Bowl bowl;
	private int coinCount;
	private Hunter hunter;
	
	public HunterDog(Color color) {
		super(color);
	}
	
	public HunterDog(Hunter hunter, Color color) {
		super(color);
		this.hunter = hunter;
		this.coinCount = 0;
	}
	
	public int getCoinCount() {
		return coinCount;
	}
	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}
	
	public void addCoins(int quantity) {
		this.coinCount += quantity;
	}
	public Hunter getHunter() {
		return hunter;
	}
	public void setHunter(Hunter hunter) {
		this.hunter = hunter;
	}
	
	public Bowl getBowl() {
		return bowl;
	}
	public void setBowl(Bowl bowl) {
		this.bowl = bowl;
	}

	@Override
	public void run() {
		Controller.getInstance().hunterDogRun(this);
	}
}