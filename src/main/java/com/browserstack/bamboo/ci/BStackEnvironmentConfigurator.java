package com.browserstack.bamboo.ci;

import com.atlassian.bamboo.variable.VariableContext;
import com.atlassian.bamboo.variable.VariableDefinitionContext;
import com.atlassian.bamboo.variable.VariableType;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationManager;
import com.atlassian.bamboo.buildqueue.manager.CustomPreBuildQueuedAction;
import com.atlassian.bamboo.v2.build.BaseConfigurableBuildPlugin;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.process.EnvironmentVariableAccessorImpl;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.sal.api.component.ComponentLocator;
import com.browserstack.bamboo.ci.BStackEnvVars;
import com.browserstack.bamboo.ci.BStackConfigManager;
import com.browserstack.bamboo.ci.singletons.BrowserStackLocalSingleton;
import com.browserstack.bamboo.ci.local.BambooBrowserStackLocal;
import org.apache.commons.lang.StringUtils;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class BStackEnvironmentConfigurator extends BaseConfigurableBuildPlugin implements CustomPreBuildQueuedAction {

  protected AdministrationConfigurationManager administrationConfigurationManager;
  private EnvironmentVariableAccessor environmentVariableAccessor;

  public BStackEnvironmentConfigurator() {
      super();
  }

  @Override
  public BuildContext call() {
      setAdministrationConfigurationManager(ComponentLocator.getComponent(AdministrationConfigurationManager.class));

      AdministrationConfiguration adminConfig = administrationConfigurationManager.getAdministrationConfiguration();
      setEnvironmentVariableAccessor(ComponentLocator.getComponent(EnvironmentVariableAccessor.class));

      BStackConfigManager configManager = new BStackConfigManager(adminConfig, buildContext.getBuildDefinition().getCustomConfiguration());
      

      if(configManager.hasCredentials()) {
        addEnvVarsToPlan();

        injectVariable(buildContext, BStackEnvVars.BSTACK_USERNAME, configManager.get(BStackEnvVars.BSTACK_USERNAME));
        injectVariable(buildContext, BStackEnvVars.BSTACK_ACCESS_KEY, configManager.get(BStackEnvVars.BSTACK_ACCESS_KEY));
        injectVariable(buildContext, BStackEnvVars.BSTACK_LOCAL_ENABLED, configManager.get(BStackEnvVars.BSTACK_LOCAL_ENABLED));
      }


      // System.out.println("CONFIG MANAGER VALUES");

      // System.out.println("BSTACK_USERNAME = " + configManager.get(BStackEnvVars.BSTACK_USERNAME));
      // System.out.println("BSTACK_ACCESS_KEY = " + configManager.get(BStackEnvVars.BSTACK_ACCESS_KEY));

      // System.out.println("BSTACK_LOCAL_ENABLED = " + configManager.get(BStackEnvVars.BSTACK_LOCAL_ENABLED));
      // System.out.println("BSTACK_LOCAL_PATH = " + configManager.get(BStackEnvVars.BSTACK_LOCAL_PATH));
      // System.out.println("BSTACK_LOCAL_ARGS = " + configManager.get(BStackEnvVars.BSTACK_LOCAL_ARGS));

      
      return buildContext;
  }

  private void addEnvVarsToPlan() {
    List<TaskDefinition> taskDefinitions = buildContext.getBuildDefinition().getTaskDefinitions();

    for (TaskDefinition taskDefinition : taskDefinitions) {
        Map<String, String> configuration = taskDefinition.getConfiguration();
        String originalEnv = StringUtils.defaultString((String) configuration.get("environmentVariables"));
        Map<String, String> origMap = environmentVariableAccessor.splitEnvironmentAssignments(originalEnv, false);        

        System.out.println("Environment Variables Already Set = " + Arrays.asList(origMap));

        origMap.put(BStackEnvVars.BSTACK_USERNAME, "${bamboo." + BStackEnvVars.BSTACK_USERNAME + "}");
        origMap.put(BStackEnvVars.BSTACK_ACCESS_KEY, "${bamboo." + BStackEnvVars.BSTACK_ACCESS_KEY + "}");
        origMap.put(BStackEnvVars.BSTACK_LOCAL_ENABLED, "${bamboo." + BStackEnvVars.BSTACK_LOCAL_ENABLED + "}");
        origMap.put(BStackEnvVars.BSTACK_LOCAL_IDENTIFIER, "${bamboo." + BStackEnvVars.BSTACK_LOCAL_IDENTIFIER + "}");

        environmentVariableAccessor = new EnvironmentVariableAccessorImpl(null, null);
        String modifiedVars = environmentVariableAccessor.joinEnvironmentVariables(origMap);
        configuration.put("environmentVariables", modifiedVars);
    }
  }

  private void injectVariable(BuildContext buildContext, String key, String value) {
      System.out.println("Injecting " + key + ": " + value);
      VariableContext variableContext =  buildContext.getVariableContext();
      variableContext.addLocalVariable(key, value);
      VariableDefinitionContext variableDefinitionContext = variableContext.getEffectiveVariables().get(key);
      if (variableDefinitionContext != null)
      {
        variableDefinitionContext.setVariableType(VariableType.ENVIRONMENT);
      }
  }

  public void setAdministrationConfigurationManager(AdministrationConfigurationManager administrationConfigurationManager) {
      this.administrationConfigurationManager = administrationConfigurationManager;
  }


  public EnvironmentVariableAccessor getEnvironmentVariableAccessor() {
      return environmentVariableAccessor;
  }

  public void setEnvironmentVariableAccessor(EnvironmentVariableAccessor environmentVariableAccessor) {
      this.environmentVariableAccessor = environmentVariableAccessor;
  }


}