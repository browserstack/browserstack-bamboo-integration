<html>
<head>
    <title>BrowserStack Configuration</title>
    <meta name="decorator" content="adminpage">
</head>
<body>
<img src="${req.contextPath}/download/resources/com.browserstack.browserstack-bamboo-integration:BStackAssets/browserstack_logo.svg" border="0" width="350px"/>
<h1>BrowserStack Configuration</h1>

<div class="paddedClearer"></div>
    [@ww.form action="/admin/browserstack/BStackSaveConfiguration.action"
        id="BStackConfigurationForm"
        submitLabelKey='global.buttons.update'
        cancelUri='/admin/administer.action']

        [@ui.bambooSection title="Credentials"]
            [@ww.textfield name="username" label='Username' /]
            [@ww.textfield name="accessKey" label='Access Key' /]
        [/@ui.bambooSection]

        [@ww.checkbox label='Enable BrowserStack Local' name='browserstackLocal' toggle='true' description='BrowserStack Local allows you to test your private and internal servers, alongside public URLs on BrowserStack, <a href="https://www.browserstack.com/local-testing">more information</a>.' /]
        [@ui.bambooSection title="BrowserStack Local Options" dependsOn='browserstackLocal' showOn='true']
            [@ww.textfield name="browserstackLocalPath" label='Binary Path' description='If left empty, plugin will download it.' /]
            [@ww.textfield name="browserstackLocalArgs" label='Binary Arguments' description='If left empty, binary will be launched with the default arguments.' /]
        [/@ui.bambooSection]
    [/@ww.form]
</body>
</html>
