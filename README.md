#BrowserStack Bamboo Integration Plugin

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png)

<img src ="https://www.clearvision-cm.com/wp-content/uploads/2015/03/Bamboo-URL.png" width = "250px">

###Development
Make sure the Atlassian SDK is installed.
https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project/install-the-atlassian-sdk-on-a-linux-or-mac-system

`clone` this repo and `cd` into it.

Use `atlas-run`, which will install the plugin in a separate Bamboo instance and start it on the default port 6990.

In a separate tab use `atlas-mvn package` to compile after any changes (this will QuickReload the plugin inside the running bamboo instance)

Here are the SDK commands you can use:
* atlas-run   -- installs this plugin into the product and starts it on localhost
* atlas-debug -- same as atlas-run, but allows a debugger to attach at port 5005
* atlas-cli   -- after atlas-run or atlas-debug, opens a Maven command line window:
                 - 'pi' reinstalls the plugin into the running product instance
* atlas-help  -- prints description for all commands in the SDK

Full documentation regarding Bamboo plugin is always available at:

https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK
