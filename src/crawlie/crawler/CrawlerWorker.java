package crawlie.crawler;

import crawlie.Config;
import crawlie.Logger;
import crawlie.pages.AbstractPage;
import crawlie.pages.PageFactory;

/**
 * Worker crawler class implemented according to the consumer/producer principle
 * 
 * @author Patrick
 * 
 */
public class CrawlerWorker implements Runnable {

  private CrawlerController crawlie;

  public CrawlerWorker(CrawlerController crawlie) {
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
      AbstractPage page = PageFactory.convertPage(crawlie.getDiscoveredPages().pop());
      if (crawlie.getAnalyzedPages().size() >= Config.getInstance().getMaxPages()) {
        return;
      }

      Logger.getInstance().log(page.toString());
      page.analyze();
    }
    // Logger.log("Worker done!");
    crawlie.removeWorker(this);
    crawlie.analyzeBatch();
  }
}
