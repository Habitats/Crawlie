package crawlie.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import crawlie.Config;
import crawlie.Logger;
import crawlie.Message;
import crawlie.crawler.CrawlerController;


/**
 * GUI imeplemented with classic MVC, this being the controller
 *
 * @author Patrick
 */
public class CrawlieGuiController implements ActionListener, Observer {

  private CrawlieModel model;
  private CrawlieListener view;
  private CrawlieFrame frame;
  private CrawlerController crawlie;

  public CrawlieGuiController() {
    // set look and feel to the OS default over the standard java one (which frankly is quite ugly)
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

    // set up and configure the GUI in an MVC-fashin
    model = new CrawlieModel();
    view = new CrawlieView();
    frame = new CrawlieFrame((CrawlieView) view);
    ((CrawlieView) view).addController(this);

    // the view listens on changes in the model
    model.addListener(view);

    // add the GuiController as an observer for the logging feature in order to receive log messages
    // and other stuff from the crawler
    Logger.getInstance().addObserver(this);
  }

  /**
   * actionlistener for button events. when a button is clicked, you go from here and execute the appropriate action
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    // which button was clicked?
    String sourceName = ((JComponent) e.getSource()).getName();

    // if the gui is locked, do nothing
    if (Config.getInstance().isGuiLocked()) {
      Logger.getInstance().status("Wait!");
      return;
    }

    // if the crawler is running, attempt to pause -- pause, because it should be able to resume
    // where it left off
    if (sourceName.equals(CrawlieView.STOP)) {
      if (!Config.getInstance().isPaused()) {
        Logger.getInstance()
            .status("Serialzing and storing the current data... Please wait while file workers finish downloading!");

        // pause the parser. the worker threads depend upon this variable and will try and finish
        // their current workload before pausing
        Config.getInstance().setPaused(true);

        // to avoid people clicking on stuff when they shouldn't click on stuff. crawler will
        // automatically unlock when it's done
        Config.getInstance().setGuiLock(true);
      } else {
        Logger.getInstance().status("No crawler running!");
      }
    }

    // if crawler is ready, attempt to start a new instance, or resume the previous one
    else if (Config.getInstance().isPaused()) {
      if (sourceName.equals(CrawlieView.START)) {
        Logger.getInstance().status("Starting Crawlie!");
        // if no crawler ran this session, initialize a new one
        if (crawlie == null) {
          crawlie = new CrawlerController();
        }
        Config.getInstance().setPaused(false);

        crawlie.initialize();
      } else if (sourceName.equals(CrawlieView.RESET)) {
        Logger.getInstance().status("Resetting the crawler to its initial configuration...");
        crawlie = new CrawlerController();
      } else if (sourceName.equals(CrawlieView.INITIALIZE_CACHE)) {
        crawlie = new CrawlerController();
        crawlie.initializeCachedData();
      }
    } else {
      Logger.getInstance().status("Action disabled while the crawler is running!");
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
