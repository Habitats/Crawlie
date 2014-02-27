package crawlie;

public class Page {
  public final String URL;
  public final String DOMAIN;
  public final String PREFIX;
  public final String SUFFIX;

  protected int priority;

  public Page(String url) {
    URL = url;

    String prefix;
    String domain;
    String suffix;
    try {
      prefix = URL.split("/")[0].toLowerCase();
      domain = URL.split("/")[2].toLowerCase();
      suffix = URL.substring(URL.lastIndexOf(".") + 1).toLowerCase();
    } catch (Exception e) {
      prefix = "NONE";
      domain = "NONE";
      suffix = "NONE";
    }
    PREFIX = prefix;
    DOMAIN = domain;
    SUFFIX = suffix;
  }

  public int getPriority() {
    return priority;
  }

}
