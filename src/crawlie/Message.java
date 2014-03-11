package crawlie;

public class Message {
  public enum Type {
    STATUS, LOG, ERROR;
  }

  public final String message;
  public final long time;
  public final Type type;

  public Message(String message, Type type) {
    this.time = System.currentTimeMillis();
    this.message = message;
    this.type = type;
  }

  @Override
  public String toString() {
    return type.name() + ": " + message;
  }
}
