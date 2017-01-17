package com.browserstack;

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

import com.browserstack.BStackEnvVars;
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

      List<TaskDefinition> taskDefinitions = buildContext.getBuildDefinition().getTaskDefinitions();

      for (TaskDefinition taskDefinition : taskDefinitions) {
          Map<String, String> configuration = taskDefinition.getConfiguration();
          String originalEnv = StringUtils.defaultString((String) configuration.get("environmentVariables"));
          Map<String, String> origMap = environmentVariableAccessor.splitEnvironmentAssignments(originalEnv, false);        
          System.out.println(Arrays.asList(origMap));

          origMap.put(BStackEnvVars.BSTACK_USERNAME, "${bamboo." + BStackEnvVars.BSTACK_USERNAME + "}");
          origMap.put(BStackEnvVars.BSTACK_ACCESS_KEY, "${bamboo." + BStackEnvVars.BSTACK_ACCESS_KEY + "}");
          environmentVariableAccessor = new EnvironmentVariableAccessorImpl(null, null);

          String modifiedVars = environmentVariableAccessor.joinEnvironmentVariables(origMap);
          configuration.put("environmentVariables", modifiedVars);
      }
      

      injectVariable(buildContext, BStackEnvVars.BSTACK_USERNAME, adminConfig.getSystemProperty(BStackEnvVars.BSTACK_USERNAME));
      injectVariable(buildContext, BStackEnvVars.BSTACK_ACCESS_KEY, adminConfig.getSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY));

      return buildContext;
  }

  private void injectVariable(BuildContext buildContext, String key, String value) {
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