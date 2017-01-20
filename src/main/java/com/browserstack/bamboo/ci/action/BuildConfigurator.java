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
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BuildConfigurator extends BaseConfigurableBuildPlugin implements CustomPreBuildAction {

    private AdministrationConfigurationAccessor administrationConfigurationAccessor;


    @Override
    public BuildContext call() {
      System.out.println(Arrays.asList(buildContext.getBuildDefinition().getCustomConfiguration()));
      return buildContext;
    }
    
    @Override
    protected void populateContextForEdit(final Map<String, Object> context, final BuildConfiguration buildConfiguration, final Plan build) {
      context.put("BrowserStackEnabled", BrowserStackEnabled());
      context.put("browserstack_username_key", BStackEnvVars.BSTACK_USERNAME);
      context.put("browserstack_access_key_key", BStackEnvVars.BSTACK_ACCESS_KEY);
      context.put("browserstack_local_enabled_key", BStackEnvVars.BSTACK_LOCAL_ENABLED);
      context.put("browserstack_local_path_key", BStackEnvVars.BSTACK_LOCAL_PATH);
      context.put("browserstack_local_args_key", BStackEnvVars.BSTACK_LOCAL_ARGS); 
    }

    public boolean BrowserStackEnabled() {
      AdministrationConfiguration adminConfig = administrationConfigurationAccessor.getAdministrationConfiguration();

      return (StringUtils.isNotBlank(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_USERNAME))
              && StringUtils.isNotBlank(adminConfig.getSystemProperty(BStackEnvVars.BSTACK_ACCESS_KEY)));
    }

    public AdministrationConfigurationAccessor getAdministrationConfigurationAccessor() {
        return administrationConfigurationAccessor;
    }

    public void setAdministrationConfigurationAccessor(AdministrationConfigurationAccessor administrationConfigurationAccessor) {
        this.administrationConfigurationAccessor = administrationConfigurationAccessor;
    }
}
