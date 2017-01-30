package com.browserstack.bamboo.ci;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/*
  Tests to ensure that the names of enivronment variables remain unchanged in the future.
*/

public class BStackEnvVarsTest {

    @Test
    public void environmentVariableValues() throws Exception {
      assertEquals(BStackEnvVars.BSTACK_USERNAME, "BROWSERSTACK_USERNAME");
      assertEquals(BStackEnvVars.BSTACK_ACCESS_KEY, "BROWSERSTACK_ACCESS_KEY");
      assertEquals(BStackEnvVars.BSTACK_LOCAL_ENABLED, "BROWSERSTACK_LOCAL");
      assertEquals(BStackEnvVars.BSTACK_LOCAL_PATH, "BROWSERSTACK_LOCAL_PATH");
      assertEquals(BStackEnvVars.BSTACK_LOCAL_ARGS, "BROWSERSTACK_LOCAL_ARGS");
      assertEquals(BStackEnvVars.BSTACK_LOCAL_IDENTIFIER, "BROWSERSTACK_LOCAL_IDENTIFIER");
    }
}