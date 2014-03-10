package crawlie.pages;

import crawlie.FileManager;

/**
 * Whenever a file is cached for download, it is stored as an instance of this class
 * 
 * @author Patrick
 * 
 */
public class CrawlieFile extends Page {

  public CrawlieFile(String url, Page parent, AnalyzedPages analyzedPages,
      DiscoveredQueue discoveredQueue) {
    super(url, parent, analyzedPages, discoveredQueue);
  }

  @Override
  public void analyze() {
    FileManager.getInstance().storeFile(super.url);
    discoveredQueue.addVisited(this);
  }

  @Override
  public String toString() {
    return "CRAWLIE FILE - " + super.toString();
  }
}
