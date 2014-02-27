package crawlie;

public class PageFactory {
  public static Page createPage(String url, Page parent, Crawlie crawlie) {
    if (!Config.getInstance().cacheAncestors())
      parent = null;
    if (FileManager.isFile(url))
      return new Image(url, parent, crawlie);
    return new DiscoveredPage(url, parent, crawlie);
  }

  public static Page convertPage(Page page) {
    return new AnalyzedPage(page.url, page.parent, page.crawlie);
  }
}
