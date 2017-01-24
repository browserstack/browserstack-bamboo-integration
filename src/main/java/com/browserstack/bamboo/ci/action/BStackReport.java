package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.plan.cache.ImmutableChain;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.configuration.SystemInfo;
import com.atlassian.spring.container.LazyComponentReference;

public class BStackReport extends ViewBuildResults {

    @Override
    public String doDefault() throws Exception {
      LazyComponentReference<SystemInfo> systemInfoReference = new LazyComponentReference<SystemInfo>("systemInfo"); 

      SystemInfo systemInfo = systemInfoReference.get();

      System.out.println("BUILD WORKING DIR " + systemInfo.getBuildWorkingDirectory().toString());
      System.out.println("Current WORKING DIR " + systemInfo.getCurrentDirectory().toString());
      System.out.println("getArtifactsDirectory WORKING DIR " + systemInfo.getArtifactsDirectory().toString());
      System.out.println("getBuildPath WORKING DIR " + systemInfo.getBuildPath().toString());


      ImmutablePlan plan = getImmutablePlan();
      if (plan instanceof ImmutableChain) {
        System.out.println("HA bro!");
      }
      return super.doDefault();
    }
}
