package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.CustomBuildProcessor;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.browserstack.bamboo.ci.singletons.BrowserStackLocalSingleton;
import com.browserstack.bamboo.ci.local.BambooBrowserStackLocal;
import com.atlassian.spring.container.ContainerManager;
import org.jetbrains.annotations.NotNull;

public class PostBuildAction implements CustomBuildProcessor {

    BuildContext buildContext;

    @NotNull
    public BuildContext call() {
      BuildLoggerManager buildLoggerManager = (BuildLoggerManager) ContainerManager.getComponent("buildLoggerManager");
      final BuildLogger buildLogger = buildLoggerManager.getLogger(buildContext.getResultKey());
      
      BambooBrowserStackLocal browserStackLocal = BrowserStackLocalSingleton.getBrowserStackLocal("","","");
      try {
        browserStackLocal.stop();
        buildLogger.addBuildLogEntry("BrowserStackLocal Binary stopped successfully. LocalIdentifier: " + browserStackLocal.getLocalIdentifier());
      } catch (Exception e) {
        buildLogger.addBuildLogEntry("Exception while stopping the BrowserStackLocal Binary : " + e.toString());
      }

      return buildContext;
    }

    public void init(@NotNull BuildContext context) {
        this.buildContext = context;
    }
}
