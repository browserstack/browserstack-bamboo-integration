package com.browserstack;

import com.atlassian.bamboo.ww2.BambooActionSupport;
import com.atlassian.bamboo.ww2.aware.permissions.GlobalAdminSecurityAware;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.bamboo.configuration.AdministrationConfigurationManager;
import com.atlassian.bamboo.configuration.AdministrationConfigurationPersister;
import com.atlassian.sal.api.component.ComponentLocator;
import com.browserstack.BStackEnvVars;
import com.atlassian.plugin.PluginAccessor;


public class configureBStack extends BambooActionSupport implements GlobalAdminSecurityAware
{

    private String username;
    private String accessKey;

    public configureBStack(){
      super();

      setAdministrationConfigurationAccessor(ComponentLocator.getComponent(AdministrationConfigurationAccessor.class));
      setAdministrationConfigurationManager(ComponentLocator.getComponent(AdministrationConfigurationManager.class));
      setAdministrationConfigurationPersister(ComponentLocator.getComponent(AdministrationConfigurationPersister.class));
    }

    public String doEdit() {
      final AdministrationConfiguration adminConfig = this.getAdministrationConfiguration();

      setUsername(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_USERNAME));
      setAccessKey(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY));

      return INPUT;
    }

    public String doSave(){
      final AdministrationConfiguration adminConfig = this.getAdministrationConfiguration();

      adminConfig.setSystemProperty(BStackEnvVars.BSTACK_USERNAME,getUsername());
      adminConfig.setSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY,getAccessKey());
      
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

}