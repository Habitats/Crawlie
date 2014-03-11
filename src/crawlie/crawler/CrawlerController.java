package crawlie.crawler;

import java.io.IOException;
import java.util.ArrayList;

import crawlie.Config;
import crawlie.Logger;
import crawlie.database.DatabaseController;
import crawlie.pages.AnalyzedList;
import crawlie.pages.DiscoveredPage;
import crawlie.pages.DiscoveredQueue;

public class CrawlerController {
  private AnalyzedList analyzedPages;
  private DiscoveredQueue discoveredPages;

  private long start;
  private final ArrayList<CrawlerWorker> crawlers;
  private final DatabaseController db;

  public CrawlerController() {
    crawlers = new ArrayList<CrawlerWorker>();
    db = new DatabaseController();
    analyzedPages = new AnalyzedList();
    discoveredPages = new DiscoveredQueue();

    /**
     * create a shutdown hook in order to store the state on shutdown DOES NOT WORK ON TERMINATE,
     * only on "normal" shutdown, ie. through x-ing the window etc ... not very useful without a
     * GUI!
     */
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        cacheCurrentData();
      }
    }));

  }

  /**
   * Cache the current dataset to disk by serializing it
   */
  private void cacheCurrentData() {
    if (discoveredPages == null)
      return;
    discoveredPages.onSerialize();
    Object[] objList = {discoveredPages, analyzedPages};
    Serializer.getInstance().serializeCurrentData(objList);
    Logger.getInstance().status("Serialzing finished!");
  }

  /** Initialize the cached, serialized data, if possible */
  public void initializeCachedData() {
    Logger.getInstance().status("Attempting to deserialize cached data...");
    Object[] objList;
    try {
      objList = (Object[]) Serializer.getInstance().deserializeCurrentData();
      discoveredPages = (DiscoveredQueue) objList[0];
      discoveredPages.onDeserialize();
      analyzedPages = (AnalyzedList) objList[1];

      // set paused to true in order to avoid disregarding the cached data
      Logger.getInstance().status("Deserialzing finished!");
    } catch (IOException e) {
      Logger.getInstance().status("Deserialzing failed! Possible corruption");
    }
  }

  /** Initialize the web crawler! It will start off where it left off if in paused state */
  public void init() {
    start = System.currentTimeMillis();

    FileDownloadController.getInstance().initFileManager();
    startWorkers();

    // add the seed page to the discovered list if it isn't already discovered. if it is discovered
    // it means that it is using a cached dataset, thus the seed should not be visited again
    if (!discoveredPages.visited(Config.getInstance().getSeed())) {
      DiscoveredPage seedPage =
          new DiscoveredPage(Config.getInstance().getSeed(), null, analyzedPages, discoveredPages);
      discoveredPages.add(seedPage);
    }
  }

  /**
   * Start the crawler workers. these workers will pop the discovered page (URL) with the highest
   * priority and add its children to the discovered queue
   */
  private void startWorkers() {
    for (int i = 0; i < Config.getInstance().getMaxWorkers(); i++) {
      CrawlerWorker crawler = new CrawlerWorker(this);
      Thread crawlerThread = new Thread(crawler);
      crawlerThread.setName("CrawlerThread " + i);
      crawlerThread.start();
    }
  }

  public AnalyzedList getAnalyzedPages() {
    return analyzedPages;
  }

  public DiscoveredQueue getDiscoveredPages() {
    return discoveredPages;
  }

  /** this gets called when all workers in a batch are done */
  public synchronized void analyzeBatch() {
    if (!workersDone())
      return;
    Logger.getInstance().status("Writing batch to database...");
    db.addPages(analyzedPages);
    Logger.getInstance().status("Writing batch to database succeeded!");
    if (Config.getInstance().isPaused()) {
      cacheCurrentData();
    } else if (analyzedPages.size() < Config.getInstance().getMaxPages()) {
      analyzedPages.dumpPages();
      startWorkers();
    }
    Logger.getInstance().status("Unique pages discovered: " + discoveredPages.getVisitedSize());
    Logger.getInstance().status("Unique pages analyzed: " + analyzedPages.size());
    Logger.getInstance().status(
        "Execution time: " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");

    // unlock the GUI. yes i know, this is dirty, but it works
    Config.getInstance().setGuiLock(false);
  }

  /** method for checking whether all the workers of a batch is done */
  private boolean workersDone() {
    return crawlers.isEmpty();
  }

  public synchronized void addWorker(CrawlerWorker crawler) {
    crawlers.add(crawler);
  }

  public synchronized void removeWorker(CrawlerWorker crawler) {
    crawlers.remove(crawler);
  }
}
