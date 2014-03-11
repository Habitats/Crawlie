package crawlie;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

  private static Serializer instance;

  private Serializer() {};

  public synchronized static Serializer getInstance() {
    if (instance == null)
      instance = new Serializer();
    return instance;
  }

  public void serializeCurrentData(Object obj) {
    try {
      ObjectOutputStream out =
          new ObjectOutputStream(new FileOutputStream(Config.getInstance().getSerializedFile()));
      out.writeObject(obj);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Object deserializeCurrentData() {
    Object obj = null;
    try {
      ObjectInputStream in =
          new ObjectInputStream(new FileInputStream(Config.getInstance().getSerializedFile()));
      obj = in.readObject();
      in.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return obj;
  }
}
