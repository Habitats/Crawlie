package crawlie.gui;

import java.util.Observable;


public class CrawlieModel extends Observable {

  private String entry;
  private String name;

  public void setEntry(String entry) {
    this.entry = entry;
    setChanged();
    notifyObservers(entry);
  }

  public String getEntry() {
    return entry;
  }

  public void setName(String name) {
    this.name = name;
    setChanged();
    notifyObservers(name);
  }
}
