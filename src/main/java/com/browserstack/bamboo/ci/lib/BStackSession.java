package com.browserstack.bamboo.ci.lib;
import com.browserstack.automate.model.Session;
import com.browserstack.bamboo.ci.lib.BStackSessionStub;
import com.browserstack.bamboo.ci.lib.JUnitReport;

/*
  bundles a JUnit TestCase and its corresponding BrowserStack Session together.
*/

/**
 * @author Pulkit Sharma
 */
public class BStackSession {
  private JUnitReport testCase;
  private String bStackSessionId;
  private Session bstackSessionInstance;
  private String exceptionEncountered;


  public BStackSession(JUnitReport testCase, String bStackSessionId, Session bstackSessionInstance, String exceptionEncountered) {
    this.testCase = testCase;
    this.bStackSessionId = bStackSessionId;
    this.bstackSessionInstance = bstackSessionInstance;
    this.exceptionEncountered = exceptionEncountered;
  }

  public String getTestCaseName() {
    return testCase.fullName();
  }


  public String getTestCaseStatus() {
    return testCase.status;
  }

  public String getTestCaseDuration() {
    return testCase.duration;
  }


  public String getBStackSessionId() {
    return bStackSessionId;
  }

  public Session getSession() {
    if (bstackSessionInstance == null) {
      return (new BStackSessionStub());
    }
    return bstackSessionInstance;
  }

  public String getExceptionEncountered() {
    return exceptionEncountered;
  }
}