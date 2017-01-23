package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.build.CustomBuildProcessor;
import com.atlassian.bamboo.build.LogEntry;
import com.atlassian.bamboo.build.logger.BuildLogUtils;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.builder.BuildState;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.results.tests.TestResults;
import com.atlassian.bamboo.resultsummary.tests.TestState;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.CurrentBuildResult;
import com.atlassian.bamboo.variable.CustomVariableContext;
import com.browserstack.bamboo.ci.singletons.BrowserStackLocalSingleton;
import com.browserstack.local.Local;
import com.atlassian.spring.container.ContainerManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostBuildAction implements CustomBuildProcessor {

    BuildContext buildContext;

    @NotNull
    public BuildContext call() {
      BuildLoggerManager buildLoggerManager = (BuildLoggerManager) ContainerManager.getComponent("buildLoggerManager");
      final BuildLogger buildLogger = buildLoggerManager.getLogger(buildContext.getResultKey());
      
      Local browserStackLocal = BrowserStackLocalSingleton.getBrowserStackLocal("","","");
      try {
        browserStackLocal.stop();
        buildLogger.addBuildLogEntry("BrowserStackLocal Binary stopped successfully. ");
      } catch (Exception e) {
        buildLogger.addBuildLogEntry("Exception while stopping the BrowserStackLocal Binary : " + e.toString());
      }

      return buildContext;
    }

    public void init(@NotNull BuildContext context) {
        this.buildContext = context;
    }
}
