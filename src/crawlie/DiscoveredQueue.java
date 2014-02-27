package crawlie;

import java.util.HashMap;
import java.util.Iterator;
import com.google.common.collect.MinMaxPriorityQueue;

/**
 * Data structure that inherits properties from hashmap and priority queue. It ensures unique
 * entries.
 * 
 * @author anon
 * 
 */
public class DiscoveredQueue implements Iterable<DiscoveredPage> {
  private HashMap<String, Boolean> visited;
  private MinMaxPriorityQueue<DiscoveredPage> pages;
  private final int MAX_SIZE = 10000;

  public DiscoveredQueue() {
    visited = new HashMap<String, Boolean>();
    pages = MinMaxPriorityQueue.maximumSize(MAX_SIZE).create();
  }

  private synchronized boolean visited(String url) {
    return visited.get(url) != null;
  }

  public synchronized DiscoveredPage pop() {
    if (pages.size() == 0)
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    DiscoveredPage page = pages.pollFirst();
    return page;
  }

  public synchronized void add(DiscoveredPage e) {
    if (!visited(e.URL)) {
      if (Config.getInstance().singleDomain() && !Config.getInstance().getSeed().contains(e.DOMAIN)) {
        return;
      }

      pages.add(e);
      visited.put(e.URL, true);
      // if (pages.size() >= MAX_SIZE)
      // pages.removeLast();
      notify();
    }
  }

  public synchronized int size() {
    return pages.size();
  }

  @Override
  public Iterator<DiscoveredPage> iterator() {
    return pages.iterator();
  }
}
