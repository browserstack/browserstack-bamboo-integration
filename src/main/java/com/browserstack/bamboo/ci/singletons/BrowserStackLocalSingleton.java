package com.browserstack.bamboo.ci.singletons;

import com.browserstack.bamboo.ci.local.BambooBrowserStackLocal;

public class BrowserStackLocalSingleton {

    private static BambooBrowserStackLocal browserStackLocal;
    
    public static BambooBrowserStackLocal getBrowserStackLocal(String accessKey, String binaryPath, String binaryArgs) {

        if (BrowserStackLocalSingleton.browserStackLocal == null) {
            setBrowserStackLocal(new BambooBrowserStackLocal(accessKey, binaryPath, binaryArgs));
        }
        return BrowserStackLocalSingleton.browserStackLocal;
    }

    public static void reset() {
       BrowserStackLocalSingleton.browserStackLocal = null;
    }

    public static void setBrowserStackLocal(BambooBrowserStackLocal browserStackLocal) {
        BrowserStackLocalSingleton.browserStackLocal = browserStackLocal;
    }
}
