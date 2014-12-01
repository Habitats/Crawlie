package crawlie.crawler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import crawlie.Config;


/**
 * Singleton for general purpose file utility methods
 *
 * It holds a set of file workers that will take an url and store them on the disk accordingly
 *
 * It is created as a singleton because it will make no sense to have multiple download controllers running
 *
 * @author Patrick
 */
public class FileDownloadController {

  private static FileDownloadController instance;

  public synchronized static FileDownloadController getInstance() {
    if (instance == null) {
      instance = new FileDownloadController();
    }
    return instance;
  }

  private FileDownloadController() {
  }

  // ########### SINGLETON #######################################

  // workers will pop urls off this queue and download them to disk
  private Queue<String> fileDownloadQueue;


  public void initFileManager() {
    fileDownloadQueue = new ConcurrentLinkedQueue<String>();
    for (int i = 0; i < Config.getInstance().getMaxFileWorkers(); i++) {
      Thread fw = new Thread(new FileDownloadWorker(this, Config.getInstance().getDownloadLocation() + "/"));
      fw.setName("FileWorker " + i);
      fw.start();
    }
  }

  /**
   * add a file to the cache and notify one waiting worker
   */
  public synchronized void addFileToDownloadQueue(String file) {
    fileDownloadQueue.add(file);
    notify();
  }

  /**
   * returns the first item added to the queue. if empty, wait for the queue to fill up
   */
  public synchronized String getUrlFromDownloadQueue() {
    while (fileDownloadQueue.isEmpty()) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    return fileDownloadQueue.poll();
  }

  /**
   * does the proposed url match a filetype marked for download?
   */
  public boolean matchesFiletypeToDownload(String url) {
    return url.substring(url.lastIndexOf(".") + 1).matches(Config.getInstance().getDownloadFiletype());
  }

  public boolean running() {
    return !Config.getInstance().isPaused();
  }
}
