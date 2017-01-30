<table style="width: 100%">
    <tr>
      <th align="left">Test Case</th>
      <th align="left">BrowserStack Session ID</th>
    </tr>
    [#list sessions as bStackSession]
        <tr>
            <td>
                ${bStackSession.getTestCaseName()}
            </td>
            <td>
                <a href="">${bStackSession.getBStackSessionId()}</a>
            </td>
        </tr>
    [/#list]
</table>
