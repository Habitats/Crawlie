package crawlie.gui;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

import crawlie.Message;


public class CrawlieModel implements CrawlieListener {

  private final List<CrawlieListener> listeners = new CopyOnWriteArrayList<CrawlieListener>();

  public void addListener(CrawlieListener listener) {
    listeners.add(listener);
  }

  public void removeListener(CrawlieListener listener) {
    listeners.remove(listener);
  }

  public void addErrorMessage(Message message) {
    for (CrawlieListener listener : listeners) {
      listener.addErrorMessage(message);
    }
  }

  @Override
  public void addStatusMessage(Message message) {
    for (CrawlieListener listener : listeners) {
      listener.addStatusMessage(message);
    }
  }

  @Override
  public void addLogMessage(Message message) {
    for (CrawlieListener listener : listeners) {
      listener.addLogMessage(message);
    }
  }
}
