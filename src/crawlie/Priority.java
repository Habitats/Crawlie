package crawlie;

import crawlie.pages.AbstractPage;
import crawlie.pages.AnalyzedPage;

/**
 * Heuristic class to handle the priority of content and URL's.
 *
 * Should be rather easy to extend and manipulate without causing havoc
 *
 * Could potentially be extended to include heuristics for the pages content in the future
 *
 * @author Patrick
 */
public class Priority {

  private static Priority instance;

  private Priority() {
  }

  public synchronized static Priority getInstance() {
    if (instance == null) {
      instance = new Priority();
      getInstance().loadDefaults();
    }
    return instance;
  }

  // ########### SINGLETON #######################################

  public int contentHeuristic(AnalyzedPage page) {
    if (page.source.text().split(" ").length < 200) {
      return 10;
    }
    return 0;
  }

  private void loadDefaults() {
    predefinedAvoidance = Config.getInstance().getPredefinedAvoidance();
    predefinedPriority = Config.getInstance().getPredefinedPriority();
  }

  // some fields defined through the config for easy access
  private String predefinedPriority;
  private String predefinedAvoidance;

  /**
   * Generates a heuristic value based on a page's URL
   *
   * the lower the returned value, the less likely the crawler is do visit and expand that page
   */
  public int urlHeuristic(AbstractPage page) {
    int priority = 0;

    // if (page.url.contains(".no/"))
    // priority += 3;
    if (page.url.contains("sport")) {
      priority -= 10;
    }

    // one predefined avoidance rule from the config
    if (page.url.contains(predefinedAvoidance)) {
      priority -= 10;
    }

    // one predefined priority rule from the config
    if (page.url.contains(predefinedPriority)) {
      priority += 10;
    }

    // avoid urls that include #, =, ? and other funny characters, as these are probably just
    // modifiers for already accessed urls
    if (page.url.contains("#")) {
      priority -= 10;
    }
    if (page.url.contains("=")) {
      priority -= 10;
    }
    if (page.url.contains("?")) {
      priority -= 10;
    }

    // if page matches one of the filetypes marked for download
    if (page.suffix.matches(Config.getInstance().getDownloadFiletype())) {
      Logger.getInstance().log(
          "[" + page.suffix + "] matches [" + Config.getInstance().getDownloadFiletype() + "]? - " + page.suffix
              .matches(Config.getInstance().getDownloadFiletype()));
      priority += 100;
    }
    return priority;
  }
}
