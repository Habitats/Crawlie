package crawlie.pages;

import crawlie.crawler.FileDownloadController;

/**
 * Whenever a non-html file is cached for download, it is stored as an instance of this class
 * 
 * @author Patrick
 * 
 */
public class CrawlieFile extends AbstractPage {

  public CrawlieFile(String url, AbstractPage parent, AnalyzedList analyzedPages, DiscoveredQueue discoveredQueue) {
    super(url, parent, analyzedPages, discoveredQueue);
  }

  @Override
  public void analyze() {
    FileDownloadController.getInstance().addFileToDownloadQueue(super.url);
  }

  @Override
  public String toString() {
    return "Cached for download > " + super.toString();
  }
}
