package crawlie.crawler;

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

  /** Cache the current dataset to disk by serializing it */
  private void cacheCurrentData() {
    discoveredPages.onSerialize();
    Object[] objList = {discoveredPages, analyzedPages};
    Serializer.getInstance().serializeCurrentData(objList);
    Logger.getInstance().status("Serialzing finished!");
  }

  /** Initialize the cached, serialized data, if possible */
  public void initializeCachedData() {
    Object[] objList = (Object[]) Serializer.getInstance().deserializeCurrentData();
    discoveredPages = (DiscoveredQueue) objList[0];
    discoveredPages.onDeserialize();
    analyzedPages = (AnalyzedList) objList[1];

    // set paused to true in order to avoid disregarding the cached data
    Config.getInstance().setPaused(true);
  }

  /** Initialize the web crawler! It will start off where it left off if in paused state */
  public void init() {
    if (Config.getInstance().isPaused()) {
      initializeCachedData();
      Config.getInstance().setPaused(false);
    } else {
      analyzedPages = new AnalyzedList();
      discoveredPages = new DiscoveredQueue();
      DiscoveredPage seedPage =
          new DiscoveredPage(Config.getInstance().getSeed(), null, analyzedPages, discoveredPages);
      start = System.currentTimeMillis();
      discoveredPages.add(seedPage);
    }

    FileDownloadController.getInstance().initFileManager();
    startWorkers();
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
    db.addPages(analyzedPages);
    if (Config.getInstance().isPaused()) {
      cacheCurrentData();
    } else if (analyzedPages.size() < Config.getInstance().getMaxPages()) {
      analyzedPages.dumpPages();
      startWorkers();
    }
    Logger.getInstance().status(
        "Analyzed " + analyzedPages.size() + " pages, in " + (System.currentTimeMillis() - start)
            / 1000 + " seconds.");
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
