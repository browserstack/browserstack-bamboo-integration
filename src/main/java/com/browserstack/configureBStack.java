package com.browserstack;

import com.atlassian.bamboo.ww2.BambooActionSupport;
import com.atlassian.bamboo.ww2.aware.permissions.GlobalAdminSecurityAware;

public class configureBStack extends BambooActionSupport implements GlobalAdminSecurityAware
{
   public String doEdit() {
      return INPUT;
   }

   public String doSave(){
    
      return SUCCESS;
   }
}