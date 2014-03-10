package crawlie;

import crawlie.pages.Page;
import crawlie.pages.PageFactory;

/**
 * Worker crawler class implemented according to the consumer/producer principle
 * 
 * @author Patrick
 * 
 */
public class Crawler implements Runnable {

  private Crawlie crawlie;

  public Crawler(Crawlie crawlie) {
    this.crawlie = crawlie;
  }

  @Override
  public void run() {
    this.crawlie.addWorker(this);
    while (crawlie.getAnalyzedPages().cacheSize() < Config.getInstance().getCacheInterval()) {
      if (Config.getInstance().isPaused())
        break;

      // try to fetch a new page from the discovered priority queue, if empty, thread will wait
      // until something is pushed in the queue
      Page page = PageFactory.convertPage(crawlie.getDiscoveredPages().pop());
      if (crawlie.getAnalyzedPages().size() >= Config.getInstance().getMaxPages()) {
        return;
      }

      Logger.getInstance().log("Analysing: " + page.toString());
      page.analyze();
    }
    // Logger.log("Worker done!");
    crawlie.removeWorker(this);
    crawlie.analyzeBatch();
  }
}
