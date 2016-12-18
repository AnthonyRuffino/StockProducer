package exercise;

public class Producer extends Thread {
	
	private LoadHandler loadHandler;
	
	public Producer(LoadHandler loadHandler) {
		this.loadHandler = loadHandler;
	}
	
	@Override
	public void run() {
		generateUpdates();
	}
	
	/**
	 * This method has had the number of iterations increased to test a heavier load on the LoadHandler
	 */
	public void generateUpdates(){
		for (int i = 1; i < 1000000; i++) {
			loadHandler.receive(new PriceUpdate("Apple", 97.85));
			loadHandler.receive(new PriceUpdate("Google", 160.71));
			loadHandler.receive(new PriceUpdate("Facebook", 91.66));
			loadHandler.receive(new PriceUpdate("Google", 160.73));
			loadHandler.receive(new PriceUpdate("Facebook", 91.71));
			loadHandler.receive(new PriceUpdate("Google", 160.76));
			loadHandler.receive(new PriceUpdate("Apple", 97.85));
			loadHandler.receive(new PriceUpdate("Google", 160.71));
			loadHandler.receive(new PriceUpdate("Facebook", 91.63));
		}
	}

}
