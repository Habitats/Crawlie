package crawlie;

public abstract class Page {
  public final String url;
  public final String domain;
  public final String prefix;
  public final String suffix;
  public final Page parent;

  protected int priority;
  protected final Crawlie crawlie;

  public Page(String url, Page parent, Crawlie crawlie) {
    this.parent = parent;
    this.url = url;
    this.crawlie = crawlie;

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
