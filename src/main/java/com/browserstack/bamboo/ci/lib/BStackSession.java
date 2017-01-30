package com.browserstack.bamboo.ci.lib;

public class BStackSession {
  private String testCase;
  private String bStackSessionId;

  public BStackSession(String testCase, String bStackSessionId) {
    this.testCase = testCase;
    this.bStackSessionId = bStackSessionId;
  }

  public String getTestCaseName() {
    return testCase;
  }

  public String getBStackSessionId() {
    return bStackSessionId;
  }
}