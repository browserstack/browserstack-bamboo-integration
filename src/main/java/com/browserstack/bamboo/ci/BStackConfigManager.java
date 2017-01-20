package com.browserstack.bamboo.ci;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.browserstack.bamboo.ci.BStackEnvVars;
import org.apache.commons.lang.StringUtils;
import java.util.Map;

public class BStackConfigManager {

  private AdministrationConfiguration adminConfig;
  private Map<String, String> buildConfig;
  private boolean overrideAdmin;

  public BStackConfigManager(AdministrationConfiguration adminConfig, Map<String, String> buildConfig) {
    this.adminConfig = adminConfig;
    this.buildConfig = buildConfig;
    if (StringUtils.isNotBlank(buildConfig.get("custom.browserstack.override")) && buildConfig.get("custom.browserstack.override").equals("true")) {
      this.overrideAdmin = true;
    } else {
      this.overrideAdmin = false;
    }
  }

  public String get(String key) {
    String adminValue = adminConfig.getSystemProperty(key);
    String buildValue = buildConfig.get("custom.browserstack." + key);
    
    if (overrideAdmin) {
      return buildValue;
    } else {
      return adminValue;
    }
  }
}