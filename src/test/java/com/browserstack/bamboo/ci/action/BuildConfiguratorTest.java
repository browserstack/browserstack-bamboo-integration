package com.browserstack.bamboo.ci.action;

import org.junit.Test;
import org.junit.Before;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.bamboo.configuration.AdministrationConfigurationImpl;
import com.atlassian.bamboo.build.BuildDefinition;
import com.browserstack.bamboo.ci.BStackConfigManager;
import com.atlassian.bamboo.ResultKey;
import com.browserstack.bamboo.ci.BStackEnvVars;
import java.util.Map;
import java.util.HashMap;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;



@RunWith(PowerMockRunner.class)
@PrepareForTest(ContainerManager.class)
public class BuildConfiguratorTest {
    private BuildConfigurator buildConfigurator;
    private BuildDefinition buildDefinition;


    @Before
    public void setUp() throws Exception {
      this.buildConfigurator = new BuildConfigurator();
      final AdministrationConfiguration administrationConfiguration = new AdministrationConfigurationImpl(null);

      AdministrationConfigurationAccessor administrationConfigurationAccessor = mock(AdministrationConfigurationAccessor.class);
      when(administrationConfigurationAccessor.getAdministrationConfiguration()).thenReturn(administrationConfiguration);

      buildConfigurator.setAdministrationConfigurationAccessor(administrationConfigurationAccessor);
      
      Map<String, String> customConfiguration = new HashMap<>();

      buildDefinition = mock(BuildDefinition.class);

      when(buildDefinition.getCustomConfiguration()).thenReturn(customConfiguration);
      final ResultKey planResultKey = mock(ResultKey.class);

      BuildContext buildContext = mock(BuildContext.class);
      when(buildContext.getBuildDefinition()).thenReturn(buildDefinition);
      when(buildContext.getResultKey()).thenReturn(planResultKey);

      BuildLogger buildLogger = mock(BuildLogger.class);

      BuildLoggerManager buildLoggerManager = mock(BuildLoggerManager.class);
      when(buildLoggerManager.getLogger(planResultKey)).thenReturn(buildLogger);

      mockStatic(ContainerManager.class);
      when(ContainerManager.getComponent("buildLoggerManager")).thenReturn(buildLoggerManager);

      buildConfigurator.init(buildContext);

    }

    @Test
    public void shouldTryStartingLocalIfJobConfigSet() {

      Boolean localStartTry = false;

      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      try {
        buildConfigurator.call();
      } catch (RuntimeException e) {
        localStartTry = true;
        assertTrue(e.toString().contains("Exception while starting the BrowserStackLocal Binary"));
      }

      assertEquals(true, localStartTry);
    }

    @Test
    public void shouldNotTryStartingLocalIfJobConfigSet() {

      Boolean localStartTry = false;

      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");
      
      try {
        buildConfigurator.call();
      } catch (RuntimeException e) {
        localStartTry = true;
        assertTrue(e.toString().contains("Exception while starting the BrowserStackLocal Binary"));
      }

      assertEquals(false, localStartTry);
    }
}