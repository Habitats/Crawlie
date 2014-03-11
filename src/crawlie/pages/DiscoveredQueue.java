package crawlie.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

import crawlie.Config;

/**
 * Data structure that inherits properties from hashmap and priority queue. It ensures unique
 * entries.
 * 
 * It utilizes a MinMaxPriorityQueue from the google guava library due to the lack of a data
 * structure with similar capabilities in the standard lib
 * 
 * @author Patrick
 * 
 */
public class DiscoveredQueue implements Iterable<DiscoveredPage>, Serializable {
  private static final long serialVersionUID = -8067373472278549730L;

  // keeps track of which urls that have been visited this crawler session
  private HashMap<String, Boolean> visited;

  // keeps track of which page to analyze next according to a heuristic value specified in
  // Priority.java
  private transient MinMaxPriorityQueue<DiscoveredPage> pages;

  // max entries that are allowed in the MinMaxPriorityQueue. when full, the queue will
  // automatically disregard elements with the lowest priority in linear time
  private final int MAX_SIZE = 10000;

  // helper structure due to MinMaxPriorityQueue not being serializable
  private List<DiscoveredPage> tempPages;

  private long size;

  public DiscoveredQueue() {
    visited = new HashMap<String, Boolean>();
    pages = MinMaxPriorityQueue.maximumSize(MAX_SIZE).create();
  }

  /**
   * checks if the given url has been visited before, during this crawler session. there's no point
   * in going the same place twice
   */
  public synchronized boolean visited(String url) {
    return visited.get(url) != null;
  }

  /** pop the page with the highest priority from the queue */
  public synchronized AbstractPage pop() {
    AbstractPage page;

    while (pages.size() == 0)
      try {
        // quque empty, wait for it to fill up
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    page = pages.pollFirst();
    return page;
  }

  public synchronized void add(AbstractPage newPage) {
    if (Config.getInstance().singleDomain()
        && !Config.getInstance().getSeed().contains(newPage.domain)) {
      return;
    }

    pages.add((DiscoveredPage) newPage);
    addVisited(newPage);

    // notify waiting workers that the queue is no longer empty
    notifyAll();
  }

  public synchronized void addVisited(AbstractPage page) {
    visited.put(page.url, true);
    size += 1;
  }

  public synchronized int size() {
    return pages.size();
  }

  /**
   * Workaround method due to MinMaxPriorityQueue not being serializable. Store everything
   * temporarily in a "normal" list
   */
  public void onSerialize() {
    tempPages = new ArrayList<DiscoveredPage>();
    tempPages.addAll(pages);
  }

  /** the total amount of unique urls discovered */
  public long getVisitedSize() {
    return size;
  }


  /**
   * Workaround method due to MinMaxPriorityQueue not being serializable. Put everything back in the
   * MinMaxPriorityQueue
   */
  public void onDeserialize() {
    pages = MinMaxPriorityQueue.maximumSize(MAX_SIZE).create();
    pages.addAll(tempPages);
  }

  @Override
  public Iterator<DiscoveredPage> iterator() {
    return pages.iterator();
  }
}
