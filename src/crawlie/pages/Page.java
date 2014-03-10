package crawlie.pages;

import java.io.Serializable;

/*
 * When a discovered page is cached, it is stored as an instance of a subclass of this class
 */
public abstract class Page implements Serializable {
  private static final long serialVersionUID = 4547183824431052054L;

  public final String url;
  public final String domain;
  public final String prefix;
  public final String suffix;
  public final Page parent;

  protected int priority;
  protected final AnalyzedPages analyzedPages;
  protected final DiscoveredQueue discoveredQueue;

  public Page(String url, Page parent, AnalyzedPages analyzedPages, DiscoveredQueue discoveredQueue) {
    this.parent = parent;
    this.url = url;
    this.analyzedPages = analyzedPages;
    this.discoveredQueue = discoveredQueue;

    String prefix;
    String domain;
    String suffix;
    try {
      prefix = url.split("/")[0].toLowerCase();
      domain = url.split("/")[2].toLowerCase();
      suffix = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
    } catch (Exception e) {
      prefix = "NONE";
      domain = "NONE";
      suffix = "NONE";
    }
    this.prefix = prefix;
    this.domain = domain;

    this.suffix = suffix;
  }

  public int getPriority() {
    return priority;
  }

  @Override
  public String toString() {
    return String.format("Priority: %4d - URL: %s", priority, url);
  }


  public abstract void analyze();
}
