package crawlie;

public class Crawler implements Runnable {

	private Crawlie crawlie;

	public Crawler(Crawlie crawlie) {
		this.crawlie = crawlie;
	}

	@Override
	public void run() {
		this.crawlie.addWorker(this);
		while (crawlie.getAnalyzedPages().cacheSize() < Config.getInstance().getCacheInterval()) {
			// try to fetch a new page, if empty, thread will wait
			DiscoveredPage page = crawlie.getDiscoveredPages().pop();

			// sometimes the page retruned is null. this is probably a bug
			if (page == null)
				continue;
			AnalyzedPage analyzedPage = new AnalyzedPage(page.URL, page.PARENT);

			// Logger.log(analyzedPage.toString());
			if (analyzedPage.SOURCE == null)
				continue;
			for (DiscoveredPage child : analyzedPage.getChildren()) {
				crawlie.getDiscoveredPages().add(child);
			}
			crawlie.getAnalyzedPages().add(analyzedPage);
		}
		// Logger.log("Worker done!");
		crawlie.removeWorker(this);
		crawlie.analyzeBatch();
	}
}
