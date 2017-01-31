package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.plan.cache.ImmutableChain;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.configuration.SystemInfo;
import com.atlassian.spring.container.LazyComponentReference;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.browserstack.bamboo.ci.lib.BStackXMLReportParser;
import com.browserstack.bamboo.ci.lib.BStackSession;
import com.browserstack.bamboo.ci.lib.BStackJUnitSessionMapper;

public class BStackReport extends ViewBuildResults {

    private List<BStackSession> bStackSessions;

    @Override
    public String doDefault() throws Exception {
      LazyComponentReference<SystemInfo> systemInfoReference = new LazyComponentReference<SystemInfo>("systemInfo"); 
      SystemInfo systemInfo = systemInfoReference.get();

      String buildWorkingDirectory = systemInfo.getBuildWorkingDirectory();
      bStackSessions = new ArrayList<BStackSession>();

      ImmutablePlan plan = getImmutablePlan();
      if (plan instanceof ImmutableChain) {
        for (ImmutableJob job : ((ImmutableChain) plan).getAllJobs()) {
          AddBStackSessions(buildWorkingDirectory + "/" + job.getKey());
        }
      } else {
        if(plan instanceof ImmutableJob) {
          ImmutableJob job = (ImmutableJob) plan;
          AddBStackSessions(buildWorkingDirectory + "/" + job.getKey());
        }
      }

      return super.doDefault();
    }

    public List<BStackSession> getSessions() {

      return bStackSessions;
    }

    private void AddBStackSessions(String directoryToScan) {

      BStackXMLReportParser bStackParser = new BStackXMLReportParser(directoryToScan);
      bStackParser.process();
      BStackJUnitSessionMapper sessionMapper = new BStackJUnitSessionMapper(directoryToScan, bStackParser.getTestSessionMap());

      bStackSessions.addAll(sessionMapper.parseAndMapJUnitXMLReports());
    }
}
