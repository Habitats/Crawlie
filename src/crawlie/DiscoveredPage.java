package crawlie;

public class DiscoveredPage extends Page implements Comparable<DiscoveredPage> {

  public final String PARENT;

  public DiscoveredPage(String url, String parent) {
    super(url);
    this.PARENT = parent;
    super.priority = Priority.getInstance().urlHeuristic(this);
  }

  @Override
  public int compareTo(DiscoveredPage other) {
    return other.priority - this.priority;
  }
}
