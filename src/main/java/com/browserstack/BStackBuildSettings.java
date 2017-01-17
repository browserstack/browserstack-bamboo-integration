package com.browserstack;

import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.build.CustomPreBuildAction; 
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.v2.build.BaseConfigurableBuildPlugin;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.variable.CustomVariableContext;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.atlassian.spring.container.ContainerManager;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.ArrayUtils;
import com.atlassian.bamboo.process.EnvironmentVariableAccessorImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import com.atlassian.sal.api.component.ComponentLocator;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class BStackBuildSettings extends BaseConfigurableBuildPlugin implements CustomPreBuildAction
{

  private EnvironmentVariableAccessor environmentVariableAccessor;

  @Override
  public BuildContext call() {
    
    return buildContext;
  }

  @Override
  protected void populateContextForEdit(final Map<String, Object> context, final BuildConfiguration buildConfiguration, final Plan build) {
      System.out.println(Arrays.asList(buildConfiguration));
      System.out.println(Arrays.asList(context));

  }
   
  @Override
  public void addDefaultValues(@NotNull BuildConfiguration buildConfiguration) {
      super.addDefaultValues(buildConfiguration);
      System.out.println(Arrays.asList(buildContext.getBuildDefinition().getCustomConfiguration()));
      System.out.println(Arrays.asList(buildConfiguration));
  }

  public EnvironmentVariableAccessor getEnvironmentVariableAccessor() {
      return environmentVariableAccessor;
  }

  public void setEnvironmentVariableAccessor(EnvironmentVariableAccessor environmentVariableAccessor) {
      this.environmentVariableAccessor = environmentVariableAccessor;
  }

}