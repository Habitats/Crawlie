package crawlie.gui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

/**
 * A continuous feed area, suitable for a continuous log. It will remove lines from the top when the
 * maximum is reached.
 * 
 * @author Patrick
 * 
 */
public class ContinuousFeedArea extends JScrollPane {
  private static final long serialVersionUID = -3892971130305387962L;

  private int maxLength = 1000;

  private JTextPane pane;

  public ContinuousFeedArea() {
    super(new JTextPane());
    pane = (JTextPane) getViewport().getView();
    autoScroll(pane);
    pane.getDocument().addDocumentListener(new LimitLinesDocumentListener(maxLength));
    setMinimumSize(new Dimension(300, 150));
    setMaximumSize(new Dimension(300, 800));
    setPreferredSize(new Dimension(300, 350));
  }

  /**
   * this yields some bugs since it isn't entirely thread safe. gogo swing and concurrency... but
   * yeah. doesn't fail that often anyway, so lets just ignore exceptions for now TODO: fix this
   */
  public synchronized void append(String str) {
    try {
      Document doc = pane.getDocument();
      doc.insertString(doc.getLength(), str, null);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  /** enable auto scrolling */
  private void autoScroll(JTextPane pane) {
    DefaultCaret caret = (DefaultCaret) pane.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
  }

}
