package crawlie;

public class Image extends Page {

  public Image(String url, Page parent, Crawlie crawlie) {
    super(url, parent, crawlie);
  }

  @Override
  public void analyze() {
    FileManager.getInstance().storeFile(super.url);
    crawlie.getDiscoveredPages().addVisited(this);
  }

  @Override
  public String toString() {
    return "IMAGE - " + super.toString();
  }
}
