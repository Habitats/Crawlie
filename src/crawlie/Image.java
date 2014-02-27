package crawlie;

public class Image extends Page {

  public Image(String url, Page parent, Crawlie crawlie) {
    super(url, parent, crawlie);
  }

  @Override
  public void analyze() {
    crawlie.getDiscoveredPages().addVisited(this);
    FileManager.storeFile(super.url);
  }

  @Override
  public String toString() {
    return "IMAGE - " + super.toString();
  }
}
