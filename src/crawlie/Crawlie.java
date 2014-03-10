package crawlie;

import java.io.Serializable;
import java.util.ArrayList;
import crawlie.database.DatabaseController;
import crawlie.pages.AnalyzedPages;
import crawlie.pages.DiscoveredPage;
import crawlie.pages.DiscoveredQueue;

public class Crawlie implements Serializable {
  private AnalyzedPages analyzedPages;
  private DiscoveredQueue discoveredPages;

  private long start;
  private final ArrayList<Crawler> crawlers;
  private final DatabaseController db;

  public Crawlie() {
    crawlers = new ArrayList<Crawler>();
    db = new DatabaseController();

    // create a shutdown hook in order to store the state on shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        cacheCurrentData();
      }
    }));
  }

  /** Cache the current dataset to disk by serializing it */
  private void cacheCurrentData() {
    Logger
        .getInstance()
        .log(
            "Serialzing and storing the current data... Please wait while file workers finish downloading!");
    discoveredPages.onSerialize();
    Object[] objList = {discoveredPages, analyzedPages};
    Serializer.serializeCurrentData(objList);
  }

  /** Initialize the cached, serialized data, if possible */
  public void initializeCachedData() {
    Object[] objList = (Object[]) Serializer.deserializeCurrentData();
    discoveredPages = (DiscoveredQueue) objList[0];
    discoveredPages.onDeserialize();
    analyzedPages = (AnalyzedPages) objList[1];

    // set paused to true in order to avoid disregarding the cached data
    Config.getInstance().setPaused(true);
  }

  /** Initialize the web crawler! It will start off where it left off if in paused state */
  public void init() {
    if (Config.getInstance().isPaused()) {
      initializeCachedData();
      Config.getInstance().setPaused(false);
    } else {
      analyzedPages = new AnalyzedPages();
      discoveredPages = new DiscoveredQueue();
      DiscoveredPage seedPage =
          new DiscoveredPage(Config.getInstance().getSeed(), null, analyzedPages, discoveredPages);
      start = System.currentTimeMillis();
      discoveredPages.add(seedPage);
    }

    FileManager.getInstance().initFileManager();
    startWorkers();
  }

  /**
   * Start the crawler workers. these workers will pop the discovered page (URL) with the highest
   * priority and add its children to the discovered queue
   */
  private void startWorkers() {
    for (int i = 0; i < Config.getInstance().getMaxWorkers(); i++) {
      Crawler crawler = new Crawler(this);
      Thread crawlerThread = new Thread(crawler);
      crawlerThread.setName("CrawlerThread " + i);
      crawlerThread.start();
    }
  }

  public AnalyzedPages getAnalyzedPages() {
    return analyzedPages;
  }

  public DiscoveredQueue getDiscoveredPages() {
    return discoveredPages;
  }

  /** this gets called when all workers in a batch are done */
  public synchronized void analyzeBatch() {
    if (!workersDone())
      return;
    db.addPages(analyzedPages);
    if (Config.getInstance().isPaused()) {
      cacheCurrentData();
    } else if (analyzedPages.size() < Config.getInstance().getMaxPages()) {
      analyzedPages.dumpPages();
      startWorkers();
    }
    Logger.getInstance().log(
        "Analyzed " + analyzedPages.size() + " pages, in " + (System.currentTimeMillis() - start)
            / 1000 + " seconds.");
  }

  /** method for checking whether all the workers of a batch is done */
  private boolean workersDone() {
    return crawlers.isEmpty();
  }

  public synchronized void addWorker(Crawler crawler) {
    crawlers.add(crawler);
  }

  public synchronized void removeWorker(Crawler crawler) {
    crawlers.remove(crawler);
  }

  /** The main class. */
  public static void main(String[] args) {
    Crawlie crawlie = new Crawlie();
    crawlie.init();
  }
}
