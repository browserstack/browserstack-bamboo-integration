[@ui.bambooSection title='<img width="30px" src="${req.contextPath}/download/resources/com.browserstack.bamboo.browserstack-bamboo-integration:BStackAssets/browserstack_icon.svg" />&nbsp;BrowserStack Report' description="Details of sessions which ran on BrowserStack, you can click the individual Test Cases for more information."]

    <style>
        /* Style the buttons that are used to open and close the accordion panel */
        td,th {
            padding: 10px;
            border-bottom: 1px solid grey;
        }

        td.accordion {
            color: #444;
            cursor: pointer;
            padding: 0px;
            text-align: left;
            outline: none;
            transition: .4s;
            vertical-align: middle;
        }

        th {
            background-color: #ccc;
        }

        /* Add a background color to the button if it is clicked on (add the .active class with JS), and when you move the mouse over it (hover) */
        td.accordion.active, tr:hover {
            background-color: #ddd;
        }


    </style>

    <table style="width: 100%">
        <tr>
          <th align="left">Test Case</th>
          <th align="center">Status</th>
          <th align="center">Duration</th>
          <th align="center">Selenium Logs</th>
        </tr>
        [#list sessions as bStackSession]
            <tr>

                <td align="left" class = "accordion">
                    ${bStackSession.getTestCaseName()}
                </td>
                <td align="center">
                    ${bStackSession.getTestCaseStatus()}
                </td>

                <td align="center">
                    ${bStackSession.getTestCaseDuration()}
                </td>
                
                <td align="center">
                    [#if bStackSession.getSession().getLogUrl() == ""]
                        <p>Error while fetching data</p>
                    [#else]
                        <a target="_blank" href="${bStackSession.getSession().getLogUrl()}">View</a>
                    [/#if]
                </td>
            </tr>
            <tr>
                <td colspan="100%" style="padding:0px; border-bottom: 0;">
                    <div style="display:none;">
                        <div>
                            <p>${bStackSession.getExceptionEncountered()}</p>
                        </div>
                        <iframe width="100%" height="700px" src="" data-src="${bStackSession.getSession().getPublicUrl()}" allowfullscreen="true" webkitallowfullscreen="true" mozallowfullscreen="true"></iframe>
                    </div>
                </td>
            </tr>
        [/#list]
    </table>

    <script>
    var acc = document.getElementsByClassName("accordion");
    var i;

    for (i = 0; i < acc.length; i++) {
        acc[i].onclick = function(){
            /* Toggle between adding and removing the "active" class,
            to highlight the button that controls the panel */
            this.classList.toggle("active");

            /* Toggle between hiding and showing the active panel */
            var hiddenRow = this.parentElement.nextElementSibling.children[0].children[0];
            console.log("Hidden Row: ", hiddenRow);
            if (hiddenRow.style.display === "block") {
                hiddenRow.style.display = "none";
            } else {
                //Lazy Load iframe
                var iframe = hiddenRow.children[1]; 
                src = iframe.getAttribute('data-src');
                iframe.setAttribute('src',src);
                hiddenRow.style.display = "block";
            }
        }
    }
    </script>
[/@ui.bambooSection]