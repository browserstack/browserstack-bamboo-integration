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
        text-align: center;
        outline: none;
        transition: .4s;
        vertical-align: middle;
    }

    th {
        background-color: #ccc;
    }

    /* Add a background color to the button if it is clicked on (add the .active class with JS), and when you move the mouse over it (hover) */
    td.accordion.active, td.accordion:hover {
        background-color: #ddd;
    }


</style>

<table style="width: 100%">
    <tr>
      <th></th>
      <th align="left">Test Case</th>
      <th align="center">Status</th>
      <th align="center">Selenium Logs</th>
    </tr>
    [#list sessions as bStackSession]
        <tr>
            <td class = "accordion">
                <h3>+</h3>
            </td>

            <td align="left">
                ${bStackSession.getTestCaseName()}
            </td>
            <td align="center">
                ${bStackSession.getSession().getStatus()}
            </td>
            <td align="center">
                <a href="${bStackSession.getSession().getLogUrl()}">View</a>
            </td>
        </tr>
        <tr>
            <td colspan="100%" style="padding:0px; border-bottom: 0;">
                <div style="display:none;">
                    <iframe width="100%" height="500px" src="" data-src="${bStackSession.getSession().getPublicUrl()}" allowfullscreen="true" webkitallowfullscreen="true" mozallowfullscreen="true"></iframe>
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
            this.innerHTML = "<h3>+</h3>"
            hiddenRow.style.display = "none";
        } else {
            //Lazy Load iframe
            var iframe = hiddenRow.children[0]; 
            src = iframe.getAttribute('data-src');
            iframe.setAttribute('src',src);
            hiddenRow.style.display = "block";
            this.innerHTML = "<h3>-</h3>"
        }
    }
}
</script>
