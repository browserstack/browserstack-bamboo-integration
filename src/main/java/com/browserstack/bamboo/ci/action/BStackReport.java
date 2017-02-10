package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.plan.cache.ImmutableChain;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.configuration.SystemInfo;
import com.atlassian.spring.container.LazyComponentReference;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.browserstack.bamboo.ci.lib.BStackXMLReportParser;
import com.browserstack.bamboo.ci.lib.BStackSession;
import com.browserstack.bamboo.ci.lib.BStackJUnitSessionMapper;
import com.browserstack.automate.AutomateClient;
import com.browserstack.bamboo.ci.BStackConfigManager;

/*
  invoked when accessing the BrowserStack Report tab. It will try to parse reports present in the Build's working directory in order to create mappings from Test Cases to BrowserStack sessions.
*/

/**
 * @author Pulkit Sharma
 */
public class BStackReport extends ViewBuildResults {

    private List<BStackSession> bStackSessions;

    private AdministrationConfigurationAccessor administrationConfigurationAccessor;

    String buildNumber;
    
    /*
      The default entry point.
    */
    @Override
    public String doDefault() throws Exception {
      LazyComponentReference<SystemInfo> systemInfoReference = new LazyComponentReference<SystemInfo>("systemInfo"); 
      SystemInfo systemInfo = systemInfoReference.get();

      String artifactsBaseDirectory = systemInfo.getArtifactsDirectory();

      bStackSessions = new ArrayList<BStackSession>();

      ImmutablePlan plan = getImmutablePlan();
      
      if (plan instanceof ImmutableChain) {
        for (ImmutableJob job : ((ImmutableChain) plan).getAllJobs()) {
          AddBStackSessions(artifactsBaseDirectory, job);
        }
      } else {
        if(plan instanceof ImmutableJob) {
          ImmutableJob job = (ImmutableJob) plan;
          AddBStackSessions(artifactsBaseDirectory, job);
        }
      }

      return super.doDefault();
    }

    public List<BStackSession> getSessions() {

      return bStackSessions;
    }

    private void AddBStackSessions(String baseDirectory, ImmutableJob job) {

      String directoryToScan = baseDirectory + "/" + "plan-" + job.getParent().getId() + "/" + job.getKey().split("-")[2] + "/build-" + String.format("%05d", Integer.parseInt(buildNumber)) + "/BSTACK_REPORTS/target";    
      System.out.println("SCANNING THE FOLLOWING " + directoryToScan);

      BStackConfigManager configManager = new BStackConfigManager(administrationConfigurationAccessor.getAdministrationConfiguration(), job.getBuildDefinition().getCustomConfiguration());
      AutomateClient automateClient = null;

      if(configManager.hasCredentials()) {
        automateClient = new AutomateClient(configManager.getUsername(), configManager.getAccessKey());
      }

      if (automateClient != null) {
        BStackXMLReportParser bStackParser = new BStackXMLReportParser(directoryToScan);
        bStackParser.process();
        BStackJUnitSessionMapper sessionMapper = new BStackJUnitSessionMapper(directoryToScan, bStackParser.getTestSessionMap(), automateClient);

        bStackSessions.addAll(sessionMapper.parseAndMapJUnitXMLReports());
      } 
      
    }

    public void setBuildNumber(String buildNumber){
      this.buildNumber = buildNumber;
    }

    public AdministrationConfigurationAccessor getAdministrationConfigurationAccessor() {
        return administrationConfigurationAccessor;
    }

    public void setAdministrationConfigurationAccessor(AdministrationConfigurationAccessor administrationConfigurationAccessor) {
        this.administrationConfigurationAccessor = administrationConfigurationAccessor;
    }
}
