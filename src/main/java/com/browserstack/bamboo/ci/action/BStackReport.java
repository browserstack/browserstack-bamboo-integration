package com.browserstack.bamboo.ci.action;

import com.atlassian.bamboo.plan.cache.ImmutableChain;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.build.Job;
import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.configuration.SystemInfo;
import com.atlassian.spring.container.LazyComponentReference;
import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.AdministrationConfigurationManager;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BStackReport extends ViewBuildResults {

    @Override
    public String doDefault() throws Exception {
      LazyComponentReference<SystemInfo> systemInfoReference = new LazyComponentReference<SystemInfo>("systemInfo"); 

      SystemInfo systemInfo = systemInfoReference.get();

      System.out.println("BUILD WORKING DIR " + systemInfo.getBuildWorkingDirectory().toString());
      System.out.println("Current WORKING DIR " + systemInfo.getCurrentDirectory().toString());
      System.out.println("getArtifactsDirectory WORKING DIR " + systemInfo.getArtifactsDirectory().toString());
      System.out.println("getBuildPath WORKING DIR " + systemInfo.getBuildPath().toString());

      ResultsSummary summary = resultsSummary();

      if (summary == null) {
          System.out.println("result summary was null");
          return ERROR;
        }


      ImmutablePlan plan = getImmutablePlan();
      if (plan instanceof ImmutableChain) {
        System.out.println("HA bro!");
      }
      return super.doDefault();
    }
}
