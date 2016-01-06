package local.crawler;

import local.crawler.runnable.GetPublicStatusThread;
import local.crawler.runnable.GetUserStatusThread;
import token.TokenManage;

/**
 * Created by liye on 16/1/3.
 * get public and user status.
 * store in local files.
 */
public class Main_public {
    public static void main(String[] args) {
        //TokenManage.refreshToken();
        GetPublicStatusThread getPublicStatusThread = new GetPublicStatusThread();
        new Thread(getPublicStatusThread).start();

        GetUserStatusThread getUserStatusThread = new GetUserStatusThread();
        new Thread(getUserStatusThread).start();
    }
}
