package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.plan.cache.ImmutableChain;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.configuration.SystemInfo;
import com.atlassian.spring.container.LazyComponentReference;
import java.util.List;
import com.browserstack.bamboo.ci.lib.XmlBStackReportParser;



public class BStackReport extends ViewBuildResults {

    @Override
    public String doDefault() throws Exception {
      LazyComponentReference<SystemInfo> systemInfoReference = new LazyComponentReference<SystemInfo>("systemInfo"); 
      SystemInfo systemInfo = systemInfoReference.get();

      String buildWorkingDirectory = systemInfo.getBuildWorkingDirectory();

      ImmutablePlan plan = getImmutablePlan();
      if (plan instanceof ImmutableChain) {
        List<ImmutableChain> chains = cachedPlanManager.getPlansByProject(getImmutablePlan().getProject(), ImmutableChain.class);
        for (ImmutableJob job : ((ImmutableChain) plan).getAllJobs()) {
          System.out.println("JOB KEY " + job.getKey());

          XmlBStackReportParser bstackParser = new XmlBStackReportParser(buildWorkingDirectory + "/" + job.getKey());
          bstackParser.process();
        }
      }

      return super.doDefault();
    }
}
