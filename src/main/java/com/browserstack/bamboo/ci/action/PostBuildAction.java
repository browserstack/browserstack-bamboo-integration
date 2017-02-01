package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.CustomBuildProcessor;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.browserstack.bamboo.ci.singletons.BrowserStackLocalSingleton;
import com.browserstack.bamboo.ci.local.BambooBrowserStackLocal;
import com.atlassian.spring.container.ContainerManager;
import org.jetbrains.annotations.NotNull;
import com.browserstack.bamboo.ci.BStackConfigManager;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;


/*
  Post Build Action. Stop the BrowserStack Local Binary if it is running.
*/

/**
 * @author Pulkit Sharma
 */

public class PostBuildAction implements CustomBuildProcessor {

    BuildContext buildContext;

    private AdministrationConfigurationAccessor administrationConfigurationAccessor;
    private BStackConfigManager configManager;

    @NotNull
    public BuildContext call() {
      this.configManager = new BStackConfigManager(administrationConfigurationAccessor.getAdministrationConfiguration(), buildContext.getBuildDefinition().getCustomConfiguration());

      if(configManager.hasCredentials()) {
        if(configManager.localEnabled()) {
          stopLocal();
        }
      }
      
      return buildContext;
    }


    private void stopLocal() {
      BuildLoggerManager buildLoggerManager = (BuildLoggerManager) ContainerManager.getComponent("buildLoggerManager");
      final BuildLogger buildLogger = buildLoggerManager.getLogger(buildContext.getResultKey());

      BambooBrowserStackLocal browserStackLocal = BrowserStackLocalSingleton.getBrowserStackLocal("","","");

      try {
        browserStackLocal.stop();
        buildLogger.addBuildLogEntry("BrowserStackLocal Binary stopped successfully. LocalIdentifier: " + browserStackLocal.getLocalIdentifier());
      } catch (Exception e) {
        buildLogger.addBuildLogEntry("Exception while stopping the BrowserStackLocal Binary : " + e.toString());
      }
    }


    public void init(@NotNull BuildContext context) {
        this.buildContext = context;
    }

    public AdministrationConfigurationAccessor getAdministrationConfigurationAccessor() {
        return administrationConfigurationAccessor;
    }

    public void setAdministrationConfigurationAccessor(AdministrationConfigurationAccessor administrationConfigurationAccessor) {
        this.administrationConfigurationAccessor = administrationConfigurationAccessor;
    }
}
