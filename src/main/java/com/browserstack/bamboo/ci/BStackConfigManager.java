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

  public boolean hasCredentials() {
    return (StringUtils.isNotBlank(get(BStackEnvVars.BSTACK_USERNAME)) && StringUtils.isNotBlank(get(BStackEnvVars.BSTACK_ACCESS_KEY)));
  }

  public boolean localEnabled() {
    return (get(BStackEnvVars.BSTACK_LOCAL_ENABLED) != null && get(BStackEnvVars.BSTACK_LOCAL_ENABLED).equals("true"));
  }

  public String getUsername() {
    return get(BStackEnvVars.BSTACK_USERNAME);
  }

  public String getAccessKey(){
    return get(BStackEnvVars.BSTACK_ACCESS_KEY); 
  }

  public String get(String key) {
    String adminValue = adminConfig.getSystemProperty(key);
    String buildValue = buildConfig.get("custom.browserstack." + key);
    
    // System.out.println("Admin value = " + ((adminValue == null)?"nil":adminValue) + " BuildValue = " + ((buildValue == null)?"nil":buildValue) + "Override Admin " + overrideAdmin);

    if (overrideAdmin) {
      return (buildValue == null) ? null : buildValue.trim();
    } else {
      return (adminValue == null) ? null : adminValue.trim();
    }
  }
}