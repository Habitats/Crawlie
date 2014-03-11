package crawlie.gui;

import crawlie.Message;

public interface CrawlieListener {


  public abstract void addStatusMessage(Message statusMessage);

  public abstract void addLogMessage(Message statusMessage);

  public abstract void addErrorMessage(Message statusMessage);

}
