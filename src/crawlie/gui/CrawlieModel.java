package crawlie.gui;

import java.util.Observable;


public class CrawlieModel extends Observable {

  private String entry;

  public void setEntry(String entry) {
    this.entry = entry;
    setChanged();
    notifyObservers(entry);
  }

  public String getEntry() {
    return entry;
  }

}
