package crawlie;

import java.util.ArrayList;

import crawlie.Crawler.State;

public class Crawlie {
	private PageQueue analyzedPages;
	private PageQueue discoveredPages;

	public final int MAX_PAGES = 1000;
	public final int MAX_WORKERS = 50;
	private long start;
	private ArrayList<Crawler> crawlers;
	private DatabaseController db;

	public Crawlie() {
		analyzedPages = new PageQueue();
		discoveredPages = new PageQueue();
		db = new DatabaseController();
	}

	private void init() {
		start = System.currentTimeMillis();

		String seed = "http://www.vg.no";
		AnalyzedPage seedPage = new AnalyzedPage(seed);
		crawlers = new ArrayList<Crawler>();

		for (int i = 0; i < MAX_WORKERS; i++) {
			Crawler crawler = new Crawler(this);
			crawlers.add(crawler);
			Thread crawlerThread = new Thread(crawler);
			crawlerThread.start();
		}
		discoveredPages.add(seedPage);
	}

	public static void main(String[] args) {
		Crawlie crawlie = new Crawlie();
		crawlie.init();
	}

	public PageQueue getAnalyzedPages() {
		return analyzedPages;
	}

	public PageQueue getDiscoveredPages() {
		return discoveredPages;
	}

	public void analyze() {
		if (!workersDone())
			return;
		db.addPages(analyzedPages);

		Logger.log("Analyzed " + analyzedPages.size() + " pages, in " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
	}

	private boolean workersDone() {
		for (Crawler crawler : crawlers) {
			if (crawler.state == State.WORKING)
				return false;
		}
		return true;
	}
}
