package com.browserstack.bamboo.ci.lib;
import com.browserstack.automate.model.Session;

public class BStackSessionStub extends Session {

  public BStackSessionStub(){
  }

  public String getPublicUrl(){
    return "";
  }

 public String getLogUrl(){
    return "";
  }

 public String getStatus(){
    return "";
  }
  
}
