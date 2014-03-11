package crawlie.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import crawlie.Config;
import crawlie.Logger;
import crawlie.Message;
import crawlie.crawler.CrawlerController;


/**
 * GUI imeplemented with classic MVC, this being the controller
 * 
 * @author Patrick
 * 
 */
public class CrawlieGuiController implements ActionListener, Observer {

  private CrawlieModel model;
  private CrawlieListener view;
  private CrawlieFrame frame;

  public CrawlieGuiController() {
    // set look and feel to the OS default over the standard java one (which frankly is quite ugly)
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

    model = new CrawlieModel();
    view = new CrawlieView();

    frame = new CrawlieFrame((CrawlieView) view);

    model.addListener(view);
    Logger.getInstance().addObserver(this);

    ((CrawlieView) view).addController(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    System.out.println(e.getActionCommand());
    String sourceName = ((JComponent) e.getSource()).getName();
    CrawlerController crawlie = new CrawlerController();
    if (sourceName.equals(CrawlieView.START)) {
      Logger.getInstance().status("Starting Crawlie!");
      crawlie.init();
      Config.getInstance().setPaused(false);
    } else if (sourceName.equals(CrawlieView.RESET)) {
      Logger.getInstance().status("Resetting the crawler to its initial configuration...");
      crawlie = new CrawlerController();
      Config.getInstance().setPaused(false);
    } else if (sourceName.equals(CrawlieView.STOP)) {
      Logger
          .getInstance()
          .status(
              "Serialzing and storing the current data... Please wait while file workers finish downloading!");
      Config.getInstance().setPaused(true);
    } else if (sourceName.equals(CrawlieView.INITIALIZE_CACHE)) {
      Logger.getInstance().status("Attempting to restore serialized data...");
      crawlie.initializeCachedData();
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    switch (((Message) arg).type) {
      case ERROR:
        model.addErrorMessage((Message) arg);
        break;
      case LOG:
        model.addLogMessage((Message) arg);
        break;
      case STATUS:
        model.addStatusMessage((Message) arg);
        break;
    }
  }
}
