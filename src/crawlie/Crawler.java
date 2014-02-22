package crawlie;

public class Crawler implements Runnable {
	public enum State {
		WORKING, IDLE;
	}

	private Crawlie crawlie;
	public State state;

	public Crawler(Crawlie crawlie) {
		this.crawlie = crawlie;
	}

	@Override
	public void run() {
		state = State.WORKING;
		while (crawlie.getAnalyzedPages().size() < crawlie.MAX_PAGES) {
			// try to fetch a new page, if empty, thread will wait
			Page page = crawlie.getDiscoveredPages().pop();

			// sometimes the page retruned is null. this is probably a bug
			if (page == null)
				continue;
			AnalyzedPage analyzedPage = new AnalyzedPage(page.getUrl());

			// Logger.log(analyzedPage.toString());
			if (analyzedPage.SOURCE == null)
				continue;
			for (Page child : analyzedPage.getLinks()) {
				crawlie.getDiscoveredPages().add(child);
			}
			crawlie.getAnalyzedPages().add(analyzedPage);
		}
		// Logger.log("Worker done!");
		state = State.IDLE;
		crawlie.analyze();
	}
}
