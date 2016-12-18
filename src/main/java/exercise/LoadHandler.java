package exercise;

import java.util.Arrays;

public class LoadHandler {

	// THis value was changed to be protected so it would be accessible to child
	// classes which might extend this class
	protected static final int MAX_PRICE_UPDATES = 100;

	// This value was changed to be protected so it would be accessible to child
	// classes which might extend this class
	protected final Consumer consumer;

	public LoadHandler(Consumer consumer) {
		this.consumer = consumer;
	}

	public void receive(PriceUpdate priceUpdate) {
		consumer.send(Arrays.asList(priceUpdate));
	}

}
