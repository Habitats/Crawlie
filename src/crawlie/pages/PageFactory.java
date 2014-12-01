package crawlie.pages;

import crawlie.Config;
import crawlie.crawler.FileDownloadController;

/**
 * Page factory class. Pages should only be created here!
 *
 * @author Patrick
 */
public class PageFactory {

  public static AbstractPage createPage(String url, AbstractPage parent, AnalyzedList analyzedPages,
                                        DiscoveredQueue discoveredQueue) {
    if (!Config.getInstance().cacheAncestors()) {
      parent = null;
    }
    if (FileDownloadController.getInstance().matchesFiletypeToDownload(url)) {
      return new CrawlieFile(url, parent, analyzedPages, discoveredQueue);
    }
    return new DiscoveredPage(url, parent, analyzedPages, discoveredQueue);
  }

  /**
   * converts discovredPages into analyzed instances. this is required whenever a discovered page is popped off the
   * priority queue to be analyzed and expanded
   */
  public static AbstractPage convertPage(AbstractPage page) {
    return new AnalyzedPage(page.url, page.parent, page.analyzedPages, page.discoveredQueue, page.priority);
  }
}
