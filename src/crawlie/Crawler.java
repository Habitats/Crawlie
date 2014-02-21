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
			Page page = crawlie.getDiscoveredPages().pop();
			AnalyzedPage analyzedPage = null;
			try {
				analyzedPage = new AnalyzedPage(page.getUrl());
			} catch (Exception e) {
				System.out.println(page + page.getUrl());
			}
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
