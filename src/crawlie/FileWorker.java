package crawlie;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class FileWorker implements Runnable {
  private String folder;
  private FileManager fileManager;

  public FileWorker(FileManager fileManager, String folder) {
    this.fileManager = fileManager;
    this.folder = folder;
  }

  @Override
  public void run() {

    while (fileManager.running()) {
      String file = fileManager.getFileToStore();
      String name = file.substring(file.lastIndexOf("/") + 1);
      try {
        InputStream in = (new URL(file)).openStream();

        if (!new File(folder).exists())
          new File(folder);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(folder + name));
        for (int b; (b = in.read()) != -1;) {
          out.write(b);
        }
        out.close();
        in.close();
        Logger.log("Stored file: " + name);
      } catch (IOException e) {
        Logger.error("Failed to download: " + file);
      }
    }
  }
}
