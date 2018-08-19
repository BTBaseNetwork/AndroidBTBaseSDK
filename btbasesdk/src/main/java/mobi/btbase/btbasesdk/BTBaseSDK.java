package mobi.btbase.btbasesdk;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

public class BTBaseSDK {
    private static HttpProxyCacheServer httpProxyCacheServer;

    public static void start(Context context) {
        httpProxyCacheServer = new HttpProxyCacheServer(context);
    }

    public static HttpProxyCacheServer getHttpProxyCacheServer() throws Exception {
        if (httpProxyCacheServer == null)
        {
            throw new Exception("Need start BTBaseSDK");
        }
        return httpProxyCacheServer;
    }
}
