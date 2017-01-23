package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.build.CustomPreBuildAction;
import com.atlassian.bamboo.v2.build.BaseConfigurableBuildPlugin;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.variable.CustomVariableContext;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.browserstack.bamboo.ci.BStackEnvVars;
import com.atlassian.spring.container.ContainerManager;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import com.atlassian.bamboo.plan.Plan;
import javax.annotation.Nullable;
import com.atlassian.sal.api.component.ComponentLocator;
import com.browserstack.bamboo.ci.BStackConfigManager;
import com.browserstack.bamboo.ci.singletons.BrowserStackLocalSingleton;
import com.browserstack.bamboo.ci.local.BambooBrowserStackLocal;
import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.build.logger.BuildLogger;

import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class BuildConfigurator extends BaseConfigurableBuildPlugin implements CustomPreBuildAction {

    private AdministrationConfigurationAccessor administrationConfigurationAccessor;
    private BStackConfigManager configManager;



    @Override
    public BuildContext call() {
      this.configManager = new BStackConfigManager(administrationConfigurationAccessor.getAdministrationConfiguration(), buildContext.getBuildDefinition().getCustomConfiguration());
      
      // System.out.println(Arrays.asList(buildContext.getBuildDefinition().getCustomConfiguration()));

      if(StringUtils.isNotBlank(configManager.get(BStackEnvVars.BSTACK_USERNAME)) && StringUtils.isNotBlank(configManager.get(BStackEnvVars.BSTACK_ACCESS_KEY))) {

        if(configManager.get(BStackEnvVars.BSTACK_LOCAL_ENABLED) == "true") {
          startLocal();
        }
      }

      return buildContext;
    }

    private void startLocal() {
      BuildLoggerManager buildLoggerManager = (BuildLoggerManager) ContainerManager.getComponent("buildLoggerManager");
      final BuildLogger buildLogger = buildLoggerManager.getLogger(buildContext.getResultKey());
      
      //Setting BambooBrowserStackLocal instance to null because updated configuration was not updated in the Singleton instance(because it was already in memory ??)
      BrowserStackLocalSingleton.reset();
      String accessKey = configManager.get(BStackEnvVars.BSTACK_ACCESS_KEY);
      String localPath = configManager.get(BStackEnvVars.BSTACK_LOCAL_PATH);
      String localArgs = configManager.get(BStackEnvVars.BSTACK_LOCAL_ARGS);
      BambooBrowserStackLocal browserStackLocal = BrowserStackLocalSingleton.getBrowserStackLocal(accessKey, localPath, localArgs);

      if(StringUtils.isNotBlank(localPath)) {
        localPath = localPath + " ";
      }
      
      try {
        buildLogger.addBuildLogEntry("Starting BrowserStackLocal Binary " + localPath + "with args " + localArgs);
        browserStackLocal.start();
        //Add Sleep Here ? Got 'browserstack.local is set to true but BrowserStackLocal binary is not connected error.'
        buildLogger.addBuildLogEntry("BrowserStackLocal Binary started successfully.");
      } catch (Exception e) {
        buildLogger.addBuildLogEntry("Exception while starting the BrowserStackLocal Binary : " + e.toString());
      }

    }

    @Override
    protected void populateContextForEdit(final Map<String, Object> context, final BuildConfiguration buildConfiguration, final Plan build) {
      String contextPrefix = "custom.browserstack.";

      context.put("browserstack_username_key", contextPrefix + BStackEnvVars.BSTACK_USERNAME);
      context.put("browserstack_access_key_key", contextPrefix + BStackEnvVars.BSTACK_ACCESS_KEY);
      context.put("browserstack_local_enabled_key", contextPrefix + BStackEnvVars.BSTACK_LOCAL_ENABLED);
      context.put("browserstack_local_path_key", contextPrefix + BStackEnvVars.BSTACK_LOCAL_PATH);
      context.put("browserstack_local_args_key", contextPrefix + BStackEnvVars.BSTACK_LOCAL_ARGS); 
    }

    public AdministrationConfigurationAccessor getAdministrationConfigurationAccessor() {
        return administrationConfigurationAccessor;
    }

    public void setAdministrationConfigurationAccessor(AdministrationConfigurationAccessor administrationConfigurationAccessor) {
        this.administrationConfigurationAccessor = administrationConfigurationAccessor;
    }
}
