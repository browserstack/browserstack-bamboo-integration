package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.build.CustomPreBuildAction;
import com.atlassian.bamboo.v2.build.BaseConfigurableBuildPlugin;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.variable.CustomVariableContext;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
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

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BuildConfigurator extends BaseConfigurableBuildPlugin implements CustomPreBuildAction {

    @Override
    public BuildContext call() {
        System.out.println("YAAAS CALLLED");
        return buildContext;
    }

    @Override
    public String getEditHtml(@NotNull BuildConfiguration buildConfiguration, @Nullable Plan plan) {
      System.out.println("HAHAHHAA" + buildConfiguration.toString() + "   " + plan.toString());
      String HTML_returned = super.getEditHtml(buildConfiguration,plan);
      System.out.println("HTML SSENDERED ====== " + HTML_returned);
       return HTML_returned;
    }
}
