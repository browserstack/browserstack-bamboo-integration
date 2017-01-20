package com.browserstack.bamboo.ci;

import com.atlassian.bamboo.build.CustomPreBuildAction;
import com.atlassian.bamboo.v2.build.BaseConfigurableBuildPlugin;
import com.atlassian.bamboo.v2.build.BuildContext;

public class Configuration extends BaseConfigurableBuildPlugin implements CustomPreBuildAction {
    @Override
    public BuildContext call() {
        System.out.println("YAAAS CALLLED");
        return buildContext;
    }
}
