package com.peppermint.vision.feed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.peppermint.vision.persistence.repository.FeedsRepository;


@Slf4j
@Service
public class FeedRefreshTaskGiver /*implements Managed*/ {

	private final FeedQueues queues;
	private final FeedRefreshWorker worker;


    public void setWaitInterval(int waitInSeconds) {
        this.waitInterval = waitInSeconds * 1000L;
    }

    private long waitInterval = 15000;


	public FeedRefreshTaskGiver(FeedQueues queues,  FeedRefreshWorker worker) {
		this.queues = queues;
		this.worker = worker;

	}

	@Async("defaultExecutor")
	public void process(){
		while (true) {
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

}
