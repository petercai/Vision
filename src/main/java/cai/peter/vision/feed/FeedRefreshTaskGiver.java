package cai.peter.vision.feed;

import cai.peter.vision.persistence.repository.FeedsRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FeedRefreshTaskGiver /*implements Managed*/ {

	private final FeedQueues queues;
	private final FeedRefreshWorker worker;

	private ExecutorService executor;

    public void setWaitInterval(int waitInSeconds) {
        this.waitInterval = waitInSeconds * 1000L;
    }

    private long waitInterval = 15000;


	public FeedRefreshTaskGiver(FeedQueues queues, FeedsRepository feedDAO, FeedRefreshWorker worker) {
		this.queues = queues;
		this.worker = worker;

		executor = Executors.newFixedThreadPool(1);
	}

//	@Override
	public void stop() {
		log.info("shutting down feed refresh task giver");
		executor.shutdownNow();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error("interrupted while waiting for threads to finish.");
			}
		}
	}

//	@Override
	public void start() {
		log.info("starting feed refresh task giver");
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (!executor.isShutdown()) {
					try {
						FeedRefreshContext context = queues.take();
						if (context != null) {
							worker.updateFeed(context);
						} else {
							log.debug("nothing to do, sleeping for 15s");
							try {
								Thread.sleep(waitInterval);
							} catch (InterruptedException e) {
								log.info("interrupted while sleeping");
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		});
	}
}
