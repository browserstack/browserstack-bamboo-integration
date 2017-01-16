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
    [/@ww.form]
</body>
</html>
