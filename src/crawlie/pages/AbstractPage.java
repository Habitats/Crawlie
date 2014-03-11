package crawlie.pages;

import java.io.Serializable;

/**
 * When a discovered page (image, html etc) is cached, it is stored as an instance of a subclass of
 * this class
 * 
 * @author Patrick
 */
public abstract class AbstractPage implements Serializable {
  private static final long serialVersionUID = 4547183824431052054L;

  // final, immuatable fields
  public final String url;
  public final String domain;
  public final String prefix;
  public final String suffix;
  public final AbstractPage parent;

  protected int priority;
  protected final AnalyzedList analyzedPages;
  protected final DiscoveredQueue discoveredQueue;

  public AbstractPage(String url, AbstractPage parent, AnalyzedList analyzedPages, DiscoveredQueue discoveredQueue) {
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
    return String.format("Priority: %d - URL: %s", priority, url);
  }


  /**
   * what to do when popping a page instance off the priority queue.
   * 
   * if its an image -> download, html page -> expand its children, discovered new page -> add it to
   * the priority queue, etc
   * 
   * see subclasses for explicit implementations
   */
  public abstract void analyze();
}
