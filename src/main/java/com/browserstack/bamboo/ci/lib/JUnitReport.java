package com.browserstack.bamboo.ci.lib;

import org.apache.commons.lang.StringUtils;

public class JUnitReport {

  public String classname;
  public String duration;
  public String name;

  public JUnitReport(String classname, String name, String duration) {
    this.classname = classname;
    this.name = name;
    this.duration = duration;
  }

  public String strippedName() {
    if (StringUtils.isEmpty(name)) {
        return null;
    }

    int subscriptIndex = name.indexOf('[');
    if (subscriptIndex != -1) {
        return name.substring(0, subscriptIndex);
    }
    return name;
  }

  public String fullStrippedName() {
    return classname + "." + strippedName();
  }

  public String fullName() {
    return classname + "." + name;
  }

}