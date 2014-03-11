package crawlie.gui;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * A continuous feed area, suitable for a continuous log. It will remove lines from the top when the
 * maximum is reached.
 * 
 * @author Patrick
 * 
 */
public class ContinuousFeedArea extends JTextPane {
  private static final long serialVersionUID = -3892971130305387962L;

  private int maxLength = 1000;

  public ContinuousFeedArea() {
    getDocument().addDocumentListener(new LimitLinesDocumentListener(maxLength));
  }

  /**
   * this yields some bugs since it isn't entirely thread safe. gogo swing and concurrency... but
   * yeah. doesn't fail that often anyway, so lets just ignore exceptions for now TODO: fix this
   */
  public synchronized void append(String str) {
    try {
      Document doc = getDocument();
      doc.insertString(doc.getLength(), str, null);
    } catch (BadLocationException e) {
      // e.printStackTrace();
    }
  }

}
