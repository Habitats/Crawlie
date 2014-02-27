package crawlie;

import java.util.ArrayList;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class AnalyzedPage extends Page {
  public final String PARENT;
  public final Document SOURCE;
  public final String TITLE;

  private ArrayList<DiscoveredPage> children;

  public AnalyzedPage(String url, String parent, int priority) {
    super(url);
    super.priority = priority;
    String title = "No title";
    Document source = null;
    PARENT = parent;
    // Logger.log("[" + super.SUFFIX + "] matches [" + Config.getInstance().getDownloadFiletype() +
    // "]? - "
    // + super.SUFFIX.matches(Config.getInstance().getDownloadFiletype()));
    try {
      if (super.SUFFIX.matches(Config.getInstance().getDownloadFiletype())) {
        storeFile(url);
      } else {
        source = Jsoup.connect(url).get();
        title = source.select("title").text();
      }
    } catch (IOException e) {
      Logger.error("Unable to load page: " + url);
    }

    SOURCE = source;
    TITLE = title;
  }

  public ArrayList<DiscoveredPage> getChildren() {
    if (children == null) {
      children = new ArrayList<DiscoveredPage>();
      Elements links = SOURCE.select("a[href]");
      for (Element child : links) {
        // remove unecessary stuff from URL
        String url = child.attr("abs:href");
        addChildren(child, url);
      }
      if (Config.getInstance().includeImages()) {
        Elements images = SOURCE.select("img[src]");
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
    if (e.val().startsWith("/"))
      url = PREFIX + "//" + DOMAIN + url;
    DiscoveredPage newPage = new DiscoveredPage(url, URL);
    children.add(newPage);
  }

  private synchronized void storeFile(String url) throws IOException {
    String name = url.substring(url.lastIndexOf("/") + 1);
    String folder = Config.getInstance().getDownloadLocation() + "/";

    InputStream in = (new URL(url)).openStream();

    if (!new File(folder).exists())
      new File(folder);
    OutputStream out = new BufferedOutputStream(new FileOutputStream(folder + name));

    for (int b; (b = in.read()) != -1;) {
      out.write(b);
    }
    out.close();
    in.close();
  }

  @Override
  public String toString() {
    return String.format("Title: %s - URL: %s", TITLE, URL);
  }
}
