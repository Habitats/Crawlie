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

  private int maxLength = 200;

  public ContinuousFeedArea() {
    getDocument().addDocumentListener(new LimitLinesDocumentListener(maxLength));
  }

  public synchronized void append(String str) {
    try {
      Document doc = getDocument();
      doc.insertString(doc.getLength(), str, null);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

}
