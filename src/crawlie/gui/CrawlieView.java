package crawlie.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class CrawlieView extends JPanel implements Observer {
  public static final String START = "START";
  public static final String PAUSE = "PAUSE";
  public static final String RESET = "RESET";
  public static final String INITIALIZE_CACHE = "INITIALIZE CACHED DATA";

  private CrawlieModel model;
  private JTextArea logField;
  private JButton pause;
  private JButton start;
  private JButton reset;
  private JButton initCache;

  public CrawlieView() {
    start = new JButton(START);
    start.setName(START);
    pause = new JButton(PAUSE);
    pause.setName(PAUSE);
    reset = new JButton(RESET);
    reset.setName(RESET);
    initCache = new JButton(INITIALIZE_CACHE);
    initCache.setName(INITIALIZE_CACHE);
    logField = new JTextArea();

    // enable auto scrolling
    DefaultCaret caret = (DefaultCaret) logField.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    setLayout(new GridBagLayout());
    Dimension dim = new Dimension(800, 500);
    setPreferredSize(dim);
    setMinimumSize(dim);
    setMaximumSize(dim);

    add(start, new GBC(0, 0).setWeight(0.1, 0));
    add(pause, new GBC(1, 0).setWeight(0.1, 0));
    add(new JScrollPane(logField), new GBC(0, 1).setSpan(4, 2).setWeight(1, 1));
    add(reset, new GBC(2, 0).setWeight(0.1, 0));
    add(initCache, new GBC(3, 0).setWeight(0.1, 0));

  }

  public void setModel(CrawlieModel model) {
    this.model = model;
  }

  @Override
  public void update(Observable o, Object arg) {
    logField.append(arg.toString() + "\n");
  }

  public void addController(CrawlieController controller) {
    start.addActionListener(controller);
    pause.addActionListener(controller);
    reset.addActionListener(controller);
    initCache.addActionListener(controller);
  }
}
