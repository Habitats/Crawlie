package crawlie.pages;

import java.util.ArrayList;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawlie.Config;
import crawlie.Logger;
import crawlie.pages.Page;

/**
 * Pages that are analyzed are stored as an instance of this class and cached until they are written
 * to the database
 * 
 * @author Patrick
 * 
 */
public class AnalyzedPage extends Page {
  private static final long serialVersionUID = -4212043927105278607L;

  public transient Document source;
  public final String title;

  private ArrayList<Page> children;
  private String sourceAsText;

  public AnalyzedPage(String url, Page parent, AnalyzedPages analyzedPage,
      DiscoveredQueue discoveredPages, int priority) {
    super(url, parent, analyzedPage, discoveredPages);
    super.priority = priority;
    String title = "No title";
    Document source = null;

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

  public ArrayList<Page> genChildren() {
    if (children == null) {
      children = new ArrayList<Page>();
      Elements links = source.select("a[href]");
      for (Element child : links) {
        // remove unecessary stuff from URL
        String url = child.attr("abs:href");
        // if (!FileManager.getInstance().isFile(url))
        addChildren(child, url);
      }
      if (Config.getInstance().includeImages()) {
        Elements images = source.select("img[src]");
        for (Element img : images) {
          String url = img.attr("abs:src");
          // url = url.split("\\?|=|#")[0];
          addChildren(img, url);
        }
      }
    }
    return children;
  }

  private void addChildren(Element e, String url) {
    // if (e.val().startsWith("/"))
    // url = prefix + "//" + domain + url;
    if (!discoveredQueue.visited(url)) {
      Page newPage = PageFactory.createPage(url, this, analyzedPages, discoveredQueue);
      discoveredQueue.addVisited(newPage);
      children.add(newPage);
    }
  }

  @Override
  public void analyze() {
    if (source == null)
      return;
    // if (crawlie.getAnalyzedPages().size() >= Config.getInstance().getMaxPages()) {
    // return;
    // }
    analyzedPages.add(this);
    for (Page child : genChildren()) {
      child.analyze();
    }
    // remove the source when analyzing is done to free up memory
    source = null;
  }
}
