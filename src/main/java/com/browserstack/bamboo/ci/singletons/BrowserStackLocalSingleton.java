package com.browserstack.bamboo.ci.singletons;

import com.browserstack.local.Local;

public class BrowserStackLocalSingleton {

    private static Local browserStackLocal;
    
    public static Local getBrowserStackLocal() {
        if (BrowserStackLocalSingleton.browserStackLocal == null) {
            setBrowserStackLocal(new Local());
        }
        return BrowserStackLocalSingleton.browserStackLocal;
    }

    public static void setBrowserStackLocal(Local browserStackLocal) {
        BrowserStackLocalSingleton.browserStackLocal = browserStackLocal;
    }
}
