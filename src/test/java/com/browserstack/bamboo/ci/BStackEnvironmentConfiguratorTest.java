package com.browserstack.bamboo.ci;

import org.junit.Test;
import org.junit.Before;
import com.atlassian.bamboo.variable.VariableContext;
import com.atlassian.bamboo.variable.VariableContextImpl;
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
import com.atlassian.bamboo.build.BuildDefinition;
import com.browserstack.bamboo.ci.BStackConfigManager;
import com.browserstack.bamboo.ci.singletons.BrowserStackLocalSingleton;
import com.browserstack.bamboo.ci.local.BambooBrowserStackLocal;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;
import org.apache.commons.lang.StringUtils;
import java.util.*;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.bamboo.configuration.AdministrationConfigurationImpl;
import com.atlassian.bamboo.configuration.AdministrationConfigurationManager;
import com.atlassian.bamboo.build.BuildDefinition;
import com.browserstack.bamboo.ci.BStackConfigManager;
import com.atlassian.bamboo.ResultKey;
import com.browserstack.bamboo.ci.BStackEnvVars;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.browserstack.bamboo.ci.singletons.BrowserStackLocalSingleton;
import com.browserstack.bamboo.ci.local.BambooBrowserStackLocal;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import com.atlassian.spring.container.ContainerManager;
import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.logger.LogInterceptorStack;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/*
  Tests to see if the Bamboo Variables and the Environment Variables are properly populated.
  Injects the Environment Variables with the values from the BStackConfigManager. 
*/

/**
 * @author Pulkit Sharma
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(ComponentLocator.class)
public class BStackEnvironmentConfiguratorTest {
  private BStackEnvironmentConfigurator environmentConfigurator;
  private BuildDefinition buildDefinition;
  private AdministrationConfiguration administrationConfiguration;
  private VariableContext variableContext;
  private TaskDefinition definition;
  private ArrayList<TaskDefinition> taskDefinitions;



  @Before
  public void setUp() throws Exception {

    this.environmentConfigurator = new BStackEnvironmentConfigurator();
    administrationConfiguration = new AdministrationConfigurationImpl(null);

    EnvironmentVariableAccessor environmentVariableAccessor = new EnvironmentVariableAccessorImpl(null, null);

    this.variableContext = new VariableContextImpl(Collections.<String, VariableDefinitionContext>emptyMap());

    AdministrationConfigurationManager administrationConfigurationManager = mock(AdministrationConfigurationManager.class);
    when(administrationConfigurationManager.getAdministrationConfiguration()).thenReturn(administrationConfiguration);

    environmentConfigurator.setAdministrationConfigurationManager(administrationConfigurationManager);
    this.definition = mock(TaskDefinition.class);
    Map<String, String> updatedConfiguration = new HashMap<String, String>();

    when(definition.getConfiguration()).thenReturn(updatedConfiguration);

     ArrayList<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();

    taskDefinitions.add(definition);


    Map<String, String> customConfiguration = new HashMap<>();

    buildDefinition = mock(BuildDefinition.class);
    when(buildDefinition.getCustomConfiguration()).thenReturn(customConfiguration);
    when(buildDefinition.getTaskDefinitions()).thenReturn(taskDefinitions);

    final ResultKey planResultKey = mock(ResultKey.class);

    BuildContext buildContext = mock(BuildContext.class);
    when(buildContext.getBuildDefinition()).thenReturn(buildDefinition);
    when(buildContext.getResultKey()).thenReturn(planResultKey);

    BuildLogger buildLogger = mock(BuildLogger.class);

    BuildLoggerManager buildLoggerManager = mock(BuildLoggerManager.class);
    when(buildLoggerManager.getLogger(planResultKey)).thenReturn(buildLogger);

    mockStatic(ComponentLocator.class);
    when(ComponentLocator.getComponent(AdministrationConfigurationManager.class)).thenReturn(administrationConfigurationManager);
    when(ComponentLocator.getComponent(EnvironmentVariableAccessor.class)).thenReturn(environmentVariableAccessor);

    when(buildContext.getVariableContext()).thenReturn(variableContext);

    environmentConfigurator.init(buildContext);
  }

  @Test
  public void shouldSetTheBambooVariablesToEnvironmentVariablesMap() {

    buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");

    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");

    environmentConfigurator.call();
    
    assertTrue(definition.getConfiguration().get("environmentVariables").contains(BStackEnvVars.BSTACK_USERNAME + "=${bamboo." + BStackEnvVars.BSTACK_USERNAME + "}"));
    assertTrue(definition.getConfiguration().get("environmentVariables").contains(BStackEnvVars.BSTACK_ACCESS_KEY + "=${bamboo." + BStackEnvVars.BSTACK_ACCESS_KEY + "}"));
    assertTrue(definition.getConfiguration().get("environmentVariables").contains(BStackEnvVars.BSTACK_LOCAL_ENABLED + "=${bamboo." + BStackEnvVars.BSTACK_LOCAL_ENABLED + "}"));
    assertTrue(definition.getConfiguration().get("environmentVariables").contains(BStackEnvVars.BSTACK_LOCAL_IDENTIFIER + "=${bamboo." + BStackEnvVars.BSTACK_LOCAL_IDENTIFIER + "}"));

  }

  @Test
  public void shouldSetTheBStackBambooVariablesForJobConfig() {

    buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");

    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ARGS, "1234");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_IDENTIFIER, "bot_blink_soulring_bottle_dagon_etherealblade_bloodstone_backpack_gg");


    environmentConfigurator.call();
    
    assertEquals("JABBA", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_USERNAME).getValue());
    assertEquals("JABBA_KEY", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_ACCESS_KEY).getValue());
    assertEquals("true", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ENABLED).getValue());

    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ARGS));
    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_IDENTIFIER));
  }


  @Test
  public void shouldSetTheBStackBambooVariablesForAdminConfig() {

    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");

    environmentConfigurator.call();
    
    assertEquals("ADMIN_JABBA", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_USERNAME).getValue());
    assertEquals("ADMIN_JABBA_KEY", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_ACCESS_KEY).getValue());
    assertEquals("false", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ENABLED).getValue());

    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ARGS));
    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_IDENTIFIER));
  }


  @Test
  public void shouldSetTheBStackBambooVariablesFromJobConfigIfAdminConfigSet() {

    buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");

    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");

    environmentConfigurator.call();
    
    assertEquals("JABBA", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_USERNAME).getValue());
    assertEquals("JABBA_KEY", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_ACCESS_KEY).getValue());
    assertEquals("true", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ENABLED).getValue());

    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ARGS));
    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_IDENTIFIER));
  }

  @Test
  public void shouldSetTheBStackBambooVariablesFromAdminConfigIfJobConfigNotSet() {

    buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "false");

    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
    buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
    administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");

    environmentConfigurator.call();
    
    assertEquals("ADMIN_JABBA", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_USERNAME).getValue());
    assertEquals("ADMIN_JABBA_KEY", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_ACCESS_KEY).getValue());
    assertEquals("false", variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ENABLED).getValue());

    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_ARGS));
    assertNull(variableContext.getEffectiveVariables().get(BStackEnvVars.BSTACK_LOCAL_IDENTIFIER));
  }

}