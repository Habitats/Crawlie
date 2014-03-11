package crawlie.crawler;

import java.util.ArrayList;

import crawlie.Config;


/**
 * Singleton for general purpose file utility methods
 * 
 * It holds a set of file workers that will take an url and store them on the disk accordingly
 * 
 * @author Patrick
 * 
 */
public class FileDownloadController {
  private static FileDownloadController instance;

  public synchronized static FileDownloadController getInstance() {
    if (instance == null)
      instance = new FileDownloadController();
    return instance;
  }

  // ########### SINGLETON #######################################

  private ArrayList<String> filesToStore;

  private FileDownloadController() {}

  public void initFileManager() {
    filesToStore = new ArrayList<String>();
    for (int i = 0; i < Config.getInstance().getMaxFileWorkers(); i++) {
      Thread fw =
          new Thread(new FileDownloadWorker(this, Config.getInstance().getDownloadLocation() + "/"));
      fw.setName("FileWorker " + i);
      fw.start();
    }
  }

  private synchronized void addFileToStore(String file) {
    filesToStore.add(file);
    notify();
  }

  public synchronized String getFileToStore() {
    while (filesToStore.isEmpty())
      try {
        wait();
      } catch (InterruptedException e) {
      }
    return filesToStore.remove(0);
  }

  public void storeFile(String url) {
    storeFile(url, Config.getInstance().getDownloadLocation() + "/");
  }

  public void storeFile(final String url, final String folder) {
    addFileToStore(url);
  }

  public boolean isFile(String url) {
    return url.substring(url.lastIndexOf(".") + 1).matches(
        Config.getInstance().getDownloadFiletype());
  }

  public boolean running() {
    return !Config.getInstance().isPaused();
  }
}
