package crawlie;

public class Priority {

  private static Priority instance;

  private Priority() {}

  public synchronized static Priority getInstance() {
    if (instance == null)
      instance = new Priority();
    return instance;
  }

  // ########### SINGLETON #######################################

  public int contentHeuristic(AnalyzedPage page) {
    if (page.source.text().split(" ").length < 200)
      return 10;
    return 0;
  }

  public int urlHeuristic(Page page) {
    int priority = 0;

    if (page.url.contains(".no/"))
      priority += 3;
    if (page.url.contains("facebook.com"))
      return 0;
    // if (page.URL.contains("penis"))
    // priority += 5;
    // if (page.URL.contains("tits"))
    // priority += 10;
    // if (page.URL.contains("ass"))
    // priority += 1;
    if (page.url.toLowerCase().contains("porn"))
      priority += 10;
    if (page.url.contains("comments"))
      priority -= 20;
    if (page.url.contains("sport"))
      priority -= 10;
    if (page.url.contains("#"))
      priority -= 10;
    if (page.url.contains("="))
      priority -= 10;
    if (page.url.contains("?"))
      priority -= 10;
    if (page.suffix.matches(Config.getInstance().getDownloadFiletype())) {
      Logger.log("[" + page.suffix + "] matches [" + Config.getInstance().getDownloadFiletype()
          + "]? - " + page.suffix.matches(Config.getInstance().getDownloadFiletype()));
      priority += 100;
    }
    return priority;
  }
}
