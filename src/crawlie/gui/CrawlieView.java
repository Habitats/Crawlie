package crawlie.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

import crawlie.Message;

public class CrawlieView extends JPanel implements CrawlieListener {
  private static final long serialVersionUID = -8336882687395127678L;

  public static final String START = "START";
  public static final String STOP = "STOP";
  public static final String RESET = "RESET";
  public static final String INITIALIZE_CACHE = "INITIALIZE CACHE";

  private CrawlieModel model;
  private JButton pause;
  private JButton start;
  private JButton reset;
  private JButton initCache;

  private ContinuousFeedArea errorArea;
  private ContinuousFeedArea statusArea;
  private ContinuousFeedArea logArea;

  public CrawlieView() {
    start = new JButton(START);
    start.setName(START);
    pause = new JButton(STOP);
    pause.setName(STOP);
    reset = new JButton(RESET);
    reset.setName(RESET);
    initCache = new JButton(INITIALIZE_CACHE);
    initCache.setName(INITIALIZE_CACHE);

    statusArea = createScrollableArea();
    errorArea = createScrollableArea();
    logArea = createScrollableArea();


    setLayout(new GridBagLayout());
    Dimension dim = new Dimension(800, 500);
    setPreferredSize(dim);
    setMinimumSize(dim);
    setMaximumSize(dim);

    add(new JScrollPane(statusArea), new GBC(0, 3).setSpan(4, 2).setWeight(0.9, 0.1));
    add(new JScrollPane(errorArea), new GBC(2, 1).setSpan(2, 2).setWeight(.9, 0.8));
    add(new JScrollPane(logArea), new GBC(0, 1).setSpan(2, 2).setWeight(0.8, 0.8));

    add(start, new GBC(0, 0).setWeight(0.3, 0));
    add(pause, new GBC(1, 0).setWeight(0.3, 0));
    add(reset, new GBC(2, 0).setWeight(0.3, 0));
    add(initCache, new GBC(3, 0).setWeight(0.3, 0));

  }

  private ContinuousFeedArea createScrollableArea() {
    ContinuousFeedArea textArea = new ContinuousFeedArea();
    // enable auto scrolling
    DefaultCaret caret = (DefaultCaret) textArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    return textArea;
  }

  public void addController(CrawlieController controller) {
    start.addActionListener(controller);
    pause.addActionListener(controller);
    reset.addActionListener(controller);
    initCache.addActionListener(controller);
  }

  @Override
  public void addStatusMessage(Message message) {
    statusArea.append(message + "\n");
  }

  @Override
  public void addLogMessage(Message message) {
    logArea.append(message + "\n");
  }

  @Override
  public void addErrorMessage(Message message) {
    errorArea.append(message + "\n");
  }
}
