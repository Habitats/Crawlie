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
      Page page = PageFactory.convertPage(crawlie.getDiscoveredPages().pop());
      if (crawlie.getAnalyzedPages().size() >= Config.getInstance().getMaxPages()) {
        return;
      }

      Logger.log("Analysing: " + page.toString());
      page.analyze();
    }
    // Logger.log("Worker done!");
    crawlie.removeWorker(this);
    crawlie.analyzeBatch();
  }
}
