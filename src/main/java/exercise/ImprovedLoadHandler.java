package exercise;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImprovedLoadHandler extends LoadHandler {

	// This value controls how often updates are sent to the consumer
	private static final int BLOCK_DURATION_IN_MILLISECONDS = 100;

	// the time that the receive method was first called on this instance
	private Date startTime;

	private Map<Integer, Set<PriceUpdate>> queue;

	// these values keep track of the current and last blocks we are currently
	// processing
	// when ever a new block starts we start a delayed task to send that blocks
	// data with only the
	// most recent values for each company
	private Integer currentBlock = null;
	private Integer lastBlock = null;

	// The ScheduledThreadPoolExecutor comes from the java.util.concurrent
	// package and we use it to
	// control the delayed processing of blocks of price updates
	private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(MAX_THREADS);
	private static final int MAX_THREADS = 100;

	public ImprovedLoadHandler(Consumer consumer) {
		super(consumer);
		queue = new HashMap<>();
	}

	@Override
	public void receive(PriceUpdate priceUpdate) {

		if (startTime == null)
			startTime = new Date();

		currentBlock = calculateBlockNumber(new Date());

		if (!queue.containsKey(currentBlock))
			queue.put(currentBlock, new HashSet<>());

		if (!queue.get(currentBlock).add(priceUpdate)) {
			// this removes the entry for the key (defined by company name only) 
			// the price is older and needs to be replaced
			queue.get(currentBlock).remove(priceUpdate);
			
			// this adds the latest price value for the current company name but has
			// the latest price
			queue.get(currentBlock).add(priceUpdate);
		}

		if (!currentBlock.equals(lastBlock)) {
			// we have started a new block and must schedule it to be processed
			// in X milliseconds
			// where X = BLOCK_DURATION_IN_MILLISECONDS
			lastBlock = currentBlock;
			processBlockLater(currentBlock);

		}
	}

	private Integer calculateBlockNumber(Date now) {
		Long milliSecondsSinceStart = now.getTime() - startTime.getTime();
		return milliSecondsSinceStart.intValue() / BLOCK_DURATION_IN_MILLISECONDS;
	}

	public void processBlockLater(Integer blockNumber) {
		executor.schedule(new Runnable() {
			@Override
			public void run() {
				if (queue == null)
					return;
				if (!queue.containsKey(blockNumber))
					return;

				Set<PriceUpdate> priceUpdates = queue.get(blockNumber);

				if (priceUpdates == null || priceUpdates.isEmpty())
					return;

				consumer.send(new ArrayList<>(priceUpdates));

				// clean up
				queue.put(blockNumber, null);
			}
		}, BLOCK_DURATION_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
	}
}
