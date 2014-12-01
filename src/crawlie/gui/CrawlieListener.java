package crawlie.gui;

import crawlie.Message;

/**
 * Interface for message events
 *
 * @author Patrick
 */
public interface CrawlieListener {

  public abstract void addStatusMessage(Message statusMessage);

  public abstract void addLogMessage(Message statusMessage);

  public abstract void addErrorMessage(Message statusMessage);

}
