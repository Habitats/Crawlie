package crawlie.pages;

import crawlie.Config;
import crawlie.FileManager;

/**
 * Page factory class. Pages should only be created here!
 * 
 * @author Patrick
 * 
 */
public class PageFactory {
  public static Page createPage(String url, Page parent, AnalyzedPages analyzedPages,
      DiscoveredQueue discoveredQueue) {
    if (!Config.getInstance().cacheAncestors())
      parent = null;
    if (FileManager.getInstance().isFile(url))
      return new CrawlieFile(url, parent, analyzedPages, discoveredQueue);
    return new DiscoveredPage(url, parent, analyzedPages, discoveredQueue);
  }

  public static Page convertPage(Page page) {
    return new AnalyzedPage(page.url, page.parent, page.analyzedPages, page.discoveredQueue,
        page.priority);
  }
}
