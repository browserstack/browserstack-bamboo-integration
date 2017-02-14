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
import com.atlassian.bandana.BandanaManager;
import com.atlassian.bandana.DefaultBandanaManager;
import com.atlassian.bandana.impl.MemoryBandanaPersister;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;




/*
  Considers different cases of the Job and Admin Configuration to start the Local Binary. Also tests the BStackConfigManager in turn.
  Thanks to @rossrowe for some of the code snippets present here.
*/

/**
  * @author Pulkit Sharma
  */

@RunWith(PowerMockRunner.class)
@PrepareForTest(ContainerManager.class)
public class BuildConfiguratorTest {
    private BuildConfigurator buildConfigurator;
    private BuildDefinition buildDefinition;
    private AdministrationConfiguration administrationConfiguration;


    @Before
    public void setUp() throws Exception {
      this.buildConfigurator = new BuildConfigurator();
      administrationConfiguration = new AdministrationConfigurationImpl(null);

      AdministrationConfigurationAccessor administrationConfigurationAccessor = mock(AdministrationConfigurationAccessor.class);
      when(administrationConfigurationAccessor.getAdministrationConfiguration()).thenReturn(administrationConfiguration);


      BandanaManager bandanaManager = new DefaultBandanaManager(new MemoryBandanaPersister());

      buildConfigurator.setAdministrationConfigurationAccessor(administrationConfigurationAccessor);

      buildConfigurator.setBandanaManager(bandanaManager);


      
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
    public void shouldTryToStartBStackLocalIfJobConfigSet() {

      Boolean localStartTry = false;

      // Job configuration is complete, and Local Testing is set to True.
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
    public void shouldNotTryToStartBStackLocalIfJobConfigSet() {

      // Job configuration is complete, and Local Testing is NOT selected.
      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");
      
      buildConfigurator.call(); 
    }


    @Test
    public void shouldNotTryToStartBStackLocalIfJobConfigHasNoAccessKey() {

      // Job configuration is incomplete, and Local Testing is set to True.
      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      buildConfigurator.call(); 
    }


    @Test
    public void shouldNotTryToStartBStackLocalIfJobConfigHasNoUserName() {

      // Job configuration is incomplete, and Local Testing is set to True.
      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");
      
      buildConfigurator.call();
    }

    @Test
    public void shouldNotTryToStartBStackLocalIfJobConfigOverrideIsFalse() {

      // Job configuration is set to NOT override Admin Config, and Local Testing is set to True.
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      buildConfigurator.call();
    }

    @Test
    public void shouldNotTryToStartBStackLocalIfJobConfigOverrideIsFalseA() {

      // Job configuration is set to NOT override Admin Config, and Local Testing is set to True.
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");
      
      buildConfigurator.call();
    }

    @Test
    public void shouldTryToStartBStackLocalIfAdminConfigIsSet() {

      Boolean localStartTry = false;

      // Admin Config is Complete and BrowserStackLocal is enabled.
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      try {
        buildConfigurator.call();
      } catch (RuntimeException e) {
        localStartTry = true;
        assertTrue(e.toString().contains("Exception while starting the BrowserStackLocal Binary"));
      }

      assertEquals(true, localStartTry);
    }

    @Test
    public void shouldNotTryToStartBStackLocalIfAdminConfigIsSet() {

      // Admin Config is Complete and BrowserStackLocal is set to some other value.
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");
      
      buildConfigurator.call();

    }

    @Test
    public void shouldNotTryToStartBStackLocalIfAdminConfigIsSet_() {

      // Admin Config is Complete and BrowserStackLocal is not enabled.
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");

      buildConfigurator.call();
    }

    @Test
    public void shouldNotTryToStartBStackLocalIfAdminConfigIsIncomplete() {

      // Admin Config is Incomplete and BrowserStackLocal is enabled.
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");
      
      buildConfigurator.call();
    }

    @Test
    public void shouldNotTryToStartBStackLocalIfAdminConfigIsIncomplete_() {

      // Admin Config is Incomplete and BrowserStackLocal is enabled.
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");


      buildConfigurator.call();
    }


    @Test
    public void shouldStartBinaryToGivePreferenceToJobConfigIfOverrideIstrue() {

      Boolean localStartTry = false;

      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");


      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");

      try {
        buildConfigurator.call();
      } catch (RuntimeException e) {
        localStartTry = true;
        assertTrue(e.toString().contains("Exception while starting the BrowserStackLocal Binary"));
      }

      assertEquals(true, localStartTry);
    }

    @Test
    public void shouldNotStartBinaryToGivePreferenceToJobConfigIfOverrideIstrue() {

      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_ACCESS_KEY, "JABBA_KEY");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");


      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");
      
      buildConfigurator.call();
    }

    @Test
    public void shouldNotStartBinaryToGivePreferenceToJobConfigIfOverrideIstrue_g() {

      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "true");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      buildConfigurator.call();
    }


    @Test
    public void shouldStartBinaryToGivePreferenceToAdminConfig() {

      Boolean localStartTry = false;

      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "false");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");


      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY, "ADMIN_JABBA_KEY");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      try {
        buildConfigurator.call();
      } catch (RuntimeException e) {
        localStartTry = true;
        assertTrue(e.toString().contains("Exception while starting the BrowserStackLocal Binary"));
      }

      assertEquals(true, localStartTry);
    }



    @Test
    public void shouldNotStartBinaryToGivePreferenceToAdminConfigInvalid() {

      Boolean localStartTry = false;

      buildDefinition.getCustomConfiguration().put("custom.browserstack.override", "false");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_USERNAME, "JABBA");
      buildDefinition.getCustomConfiguration().put("custom.browserstack." + BStackEnvVars.BSTACK_LOCAL_ENABLED, "false");


      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_USERNAME, "ADMIN_JABBA");
      administrationConfiguration.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED, "true");

      buildConfigurator.call();
    }

}