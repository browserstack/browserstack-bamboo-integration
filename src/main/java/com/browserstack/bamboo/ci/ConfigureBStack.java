package com.browserstack.bamboo.ci;

import com.atlassian.bamboo.ww2.BambooActionSupport;
import com.atlassian.bamboo.ww2.aware.permissions.GlobalAdminSecurityAware;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.bamboo.configuration.AdministrationConfigurationManager;
import com.atlassian.bamboo.configuration.AdministrationConfigurationPersister;
import com.atlassian.sal.api.component.ComponentLocator;
import com.browserstack.bamboo.ci.BStackEnvVars;
import com.atlassian.plugin.PluginAccessor;

/*
 Global BrowserStack configuration. Available in Bamboo Administration section.
*/

/**
 * @author Pulkit Sharma
 */
public class ConfigureBStack extends BambooActionSupport implements GlobalAdminSecurityAware
{

    private String username;
    private String accessKey;
    private String browserstackLocal;
    private String browserstackLocalPath;
    private String browserstackLocalArgs;


    public ConfigureBStack(){
      super();

      setAdministrationConfigurationAccessor(ComponentLocator.getComponent(AdministrationConfigurationAccessor.class));
      setAdministrationConfigurationManager(ComponentLocator.getComponent(AdministrationConfigurationManager.class));
      setAdministrationConfigurationPersister(ComponentLocator.getComponent(AdministrationConfigurationPersister.class));
    }

    public String doEdit() {
      final AdministrationConfiguration adminConfig = this.getAdministrationConfiguration();

      setUsername(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_USERNAME));
      setAccessKey(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY));
      setBrowserstackLocal(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED));
      setBrowserstackLocalPath(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_LOCAL_ARGS));

      return INPUT;
    }

    public String doSave(){
      final AdministrationConfiguration adminConfig = this.getAdministrationConfiguration();

      adminConfig.setSystemProperty(BStackEnvVars.BSTACK_USERNAME,getUsername());
      adminConfig.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY,getAccessKey());
      adminConfig.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ENABLED,getBrowserstackLocal());
      adminConfig.setSystemProperty(BStackEnvVars.BSTACK_LOCAL_ARGS,getBrowserstackLocalArgs());


      return SUCCESS;
    }

    public String getAccessKey()
    {
        return accessKey;
    }

    public void setAccessKey(String accesskey)
    {
        this.accessKey = accesskey;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setBrowserstackLocal(String value)
    {
        this.browserstackLocal = value;
    }

    public String getBrowserstackLocal()
    {
        return browserstackLocal;
    }

    public void setBrowserstackLocalPath(String value)
    {
        this.browserstackLocalPath = value;
    }

    public String getBrowserstackLocalPath()
    {
        return browserstackLocalPath;
    }

    public void setBrowserstackLocalArgs(String value)
    {
        this.browserstackLocalArgs = value;
    }

    public String getBrowserstackLocalArgs()
    {
        return browserstackLocalArgs;
    }

}