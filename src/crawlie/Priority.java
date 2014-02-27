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
    if (page.SOURCE.text().split(" ").length < 200)
      return 10;
    return 0;
  }

  public int urlHeuristic(Page page) {
    int priority = 0;

    if (page.URL.contains(".no/"))
      priority += 3;
    if (page.URL.contains("facebook.com"))
      return 0;
    if (page.URL.contains("penis"))
      priority += 5;
    if (page.URL.contains("tits"))
      priority += 10;
    if (page.URL.contains("ass"))
      priority += 1;
    if (page.URL.contains("sport"))
      priority -= 10;
    if (page.URL.contains("#"))
      priority -= 10;
    if (page.URL.contains("="))
      priority -= 10;
    if (page.URL.contains("?"))
      priority -= 10;
    return priority;
  }
}
