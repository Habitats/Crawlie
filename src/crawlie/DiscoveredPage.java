package crawlie;

public class DiscoveredPage extends Page implements Comparable<DiscoveredPage> {


  public DiscoveredPage(String url, Page parent, Crawlie crawlie) {
    super(url, parent, crawlie);
    super.priority = Priority.getInstance().urlHeuristic(this);
  }

  @Override
  public int compareTo(DiscoveredPage other) {
    return other.priority - this.priority;
  }

  @Override
  public void analyze() {
      crawlie.getDiscoveredPages().add(this);
  }
}
