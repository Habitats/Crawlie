package crawlie.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import crawlie.Config;

/**
 * Data structure to hold pages that are already analyzed and cache them in memory until they are
 * (pariodically) written the database
 * 
 * @author Patrick
 * 
 */
public class AnalyzedPages implements Iterable<AnalyzedPage>, Serializable {
  private ArrayList<AnalyzedPage> pages;
  private int size;

  public AnalyzedPages() {
    pages = new ArrayList<AnalyzedPage>();
  }

  public synchronized void add(AnalyzedPage e) {
    size += 1;
    if (Config.getInstance().storeContent()) {
      pages.add(e);
    }
  }

  public synchronized int size() {
    return size;
  }

  public synchronized int cacheSize() {
    return pages.size();
  }

  @Override
  public synchronized Iterator<AnalyzedPage> iterator() {
    return pages.iterator();
  }

  // the dumping of pages could be implemented in severals ways. As of now it just returns a new
  // list
  public synchronized void dumpPages() {
    pages = new ArrayList<AnalyzedPage>();
  }
}
