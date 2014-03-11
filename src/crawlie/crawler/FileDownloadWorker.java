package crawlie.crawler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import crawlie.Logger;


/**
 * Worker class for downloading files and storing themn in a desired directory. Having only one
 * thread doing this is painfully slow.
 * 
 * Workers work according to the producer/consumer principle
 * 
 * @author Patrick
 * 
 */
public class FileDownloadWorker implements Runnable {
  private String folder;
  private FileDownloadController fileManager;

  public FileDownloadWorker(FileDownloadController fileManager, String folder) {
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
        Logger.getInstance().log("Downloaded > " + name);
      } catch (IOException e) {
        Logger.getInstance().error("Failed to download: " + file);
      }
    }
  }
}
