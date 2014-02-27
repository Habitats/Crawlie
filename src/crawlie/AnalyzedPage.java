package crawlie;

import java.util.ArrayList;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class AnalyzedPage extends Page {
  public final Document source;
  public final String title;

  private ArrayList<Page> children;

  public AnalyzedPage(String url, Page parent, Crawlie crawlie) {
    super(url, parent, crawlie);
    super.priority = priority;
    String title = "No title";
    Document source = null;

    try {
      source = Jsoup.connect(url).get();
      title = source.select("title").text();
    } catch (IOException | IllegalArgumentException e) {
      Logger.error("Unable to load page: " + url);
    }

    this.source = source;
    this.title = title;
  }

  public ArrayList<Page> getChildren() {
    if (children == null) {
      children = new ArrayList<Page>();
      Elements links = source.select("a[href]");
      for (Element child : links) {
        // remove unecessary stuff from URL
        String url = child.attr("abs:href");
        if (!FileManager.isFile(url))
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
    Page newPage = PageFactory.createPage(url, this, crawlie);
    children.add(newPage);
  }

  @Override
  public void analyze() {
    if (source == null)
      return;
    if (crawlie.getAnalyzedPages().size() >= Config.getInstance().getMaxPages()) {
      return;
    }
    crawlie.getAnalyzedPages().add(this);
    for (Page child : getChildren()) {
      child.analyze();
    }
  }
}
