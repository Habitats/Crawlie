package crawlie.pages;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import crawlie.Config;
import crawlie.Logger;

/**
 * Pages that are analyzed are stored as an instance of this class and cached until they are written to the database
 *
 * @author Patrick
 */
public class AnalyzedPage extends AbstractPage {

  private static final long serialVersionUID = -4212043927105278607L;

  // Document isn't serializable
  public transient Document source;

  public final String title;

  private ArrayList<AbstractPage> children;

  public AnalyzedPage(String url, AbstractPage parent, AnalyzedList analyzedPage, DiscoveredQueue discoveredPages,
                      int priority) {
    super(url, parent, analyzedPage, discoveredPages);
    super.priority = priority;
    String title = "No title";
    Document source = null;

    // the JSoup library is used to downloading
    try {
      source = Jsoup.connect(url).get();
      title = source.select("title").text();
    } catch (IOException | IllegalArgumentException e) {
      Logger.getInstance().error("Unable to load page: " + url);
    }

    this.source = source;
    // if (source != null)
    // this.sourceAsText = source.toString();
    this.title = title;
  }

  public ArrayList<AbstractPage> genChildren() {
    if (children == null) {
      children = new ArrayList<AbstractPage>();

      // select all anchor/link elements from the source/DOM
      Elements links = source.select("a[href]");
      for (Element child : links) {
        // fetch the url value from the href attribute
        String url = child.attr("abs:href");

        // sometimes people put empty urls out onm the web... amazing.
        if (url.length() > 0) {
          addChildren(url);
        }
      }

      // if include_images is set, well, include the image tags as well! sometimes ordinary urls are
      // stored here for reason i do not know
      if (Config.getInstance().includeImages()) {
        // select all image elements
        Elements images = source.select("img[src]");
        for (Element img : images) {
          // fetch the url value from the src attribute
          String url = img.attr("abs:src");
          addChildren(url);
        }
      }
    }
    return children;
  }

  /**
   * Add the element/url to its parents list. as of now only the url is added.
   *
   * From this URL, create a new page instance, and link it to its parent, creating a tree-structure
   */
  private void addChildren(String url) {
    if (!discoveredQueue.visited(url)) {
      AbstractPage newPage = PageFactory.createPage(url, this, analyzedPages, discoveredQueue);
      children.add(newPage);
    }
  }

  @Override
  public void analyze() {
    if (source == null) {
      return;
    }
    // add this page to the set of analyzed pages
    analyzedPages.add(this);

    // scan all the urls in this document and add them as children
    for (AbstractPage child : genChildren()) {
      // the children will be an instance of DiscoveredPage, that will get added to the
      // discoveredQueue
      child.analyze();
    }
    // remove the source when analyzing is done to free up memory
    source = null;
  }

  @Override
  public String toString() {
    return "Analyzed > " + super.toString();
  }
}
