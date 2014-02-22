package crawlie;

import java.util.ArrayList;

public class Crawlie {
	private AnalyzedPages analyzedPages;
	private final DiscoveredQueue discoveredPages;

	public final int MAX_PAGES = 20000;
	public final int MAX_WORKERS = 50;
	public final int CACHE_INTERVAL = 1000;
	private long start;
	private final ArrayList<Crawler> crawlers;
	private final DatabaseController db;

	public Crawlie() {
		analyzedPages = new AnalyzedPages();
		discoveredPages = new DiscoveredQueue();
		crawlers = new ArrayList<Crawler>();
		db = new DatabaseController();
	}

	private void init() {
		start = System.currentTimeMillis();

		String seed = "http://www.vg.no";
		DiscoveredPage seedPage = new DiscoveredPage(seed, "seed");

		startWorkers();
		discoveredPages.add(seedPage);
	}

	private void startWorkers() {
		for (int i = 0; i < MAX_WORKERS; i++) {
			Crawler crawler = new Crawler(this);
			Thread crawlerThread = new Thread(crawler);
			crawlerThread.start();
		}
	}

	public static void main(String[] args) {
		Crawlie crawlie = new Crawlie();
		crawlie.init();
	}

	public AnalyzedPages getAnalyzedPages() {
		return analyzedPages;
	}

	public DiscoveredQueue getDiscoveredPages() {
		return discoveredPages;
	}

	// this gets called when all workers in a batch are done
	public synchronized void analyzeBatch() {
		if (!workersDone())
			return;
		db.addPages(analyzedPages);
		if (analyzedPages.size() < MAX_PAGES) {
			analyzedPages.dumpPages();
			startWorkers();
		}
		Logger.log("Analyzed " + analyzedPages.size() + " pages, in " + (System.currentTimeMillis() - start) / 1000 + " seconds.");

	}

	private boolean workersDone() {
		return crawlers.isEmpty();
	}

	public synchronized void addWorker(Crawler crawler) {
		crawlers.add(crawler);
	}

	public synchronized void removeWorker(Crawler crawler) {
		crawlers.remove(crawler);
	}
}
