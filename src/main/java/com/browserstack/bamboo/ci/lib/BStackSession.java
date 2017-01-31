package com.browserstack.bamboo.ci.lib;
import com.browserstack.automate.model.Session;

public class BStackSession {
  private String testCase;
  private String bStackSessionId;
  private Session bstackSessionInstance;


  public BStackSession(String testCase, String bStackSessionId, Session bstackSessionInstance) {
    this.testCase = testCase;
    this.bStackSessionId = bStackSessionId;
    this.bstackSessionInstance = bstackSessionInstance;
  }

  public String getTestCaseName() {
    return testCase;
  }

  public String getBStackSessionId() {
    return bStackSessionId;
  }

  public Session getSession() {
    return bstackSessionInstance;
  }
}