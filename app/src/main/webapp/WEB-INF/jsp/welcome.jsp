<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
    <head>
        <link rel="stylesheet" href="webjars/bootstrap/4.3.1/css/bootstrap.min.css" />
        <link rel="stylesheet" href="webjars/bootstrap/4.3.1/js/bootstrap.min.js" />
        <script src="webjars/jquery/3.4.1/jquery.min.js"></script>
    </head>
    <script type="text/javascript">
        function loadBlog(node) {
            let requestTime = new Date().getTime();

            node.innerHTML = "  <div style='text-align:center;'\n"
                             + "    <br><br><br><br><div class='spinner-border' role='status'>\n"
                             + "</div></div>"

            $.ajax({
                       url: '/app/load/' + node.id,
                       type: 'GET',
                       contentType: 'application/json',
                       dataType:'JSON',
                       success: function(blog){
                           var responseReceivedTime = new Date().getTime();
                           var processedTime = responseReceivedTime - requestTime;
                           node.innerHTML = "   <h2>" + blog.title + "</h2>\n"
                                            + " <div style='text-align: left'>\n"
                                            + "     <ttextarea>" + blog.post + "</tttextarea>\n"
                                            + " </div>\n"
                                            + " <text style='font-size: small; color: gray'>Author: " + blog.author + " | " + blog.creationDate + "</text>\n"
                                            + " <br><br>"
                                            + " <text style='font-weight: bold; color: green'>Loading time: " + processedTime + " ms</text>\n"
                                            + " <hr/>\n"
                       }
            })
                .fail(function() {
                    console.log("error");
                });
        }

        function evictCache() {
            $.ajax({
                       url: '/app/clear',
                       type: 'GET',
                       contentType: 'application/json'
                   })
                .done(function() {
                    location.reload()
                })
                .fail(function() {
                    console.log("error");
                });
        }
    </script>
    <body>
        <div class="container">
        <div class="jumbotron" style="padding: 20px 50px 20px 100px;">
            <h1>Distributed cache sample</h1>
            Project represents distributed cache effectiveness for few App nodes.<br>
            Resources (blogs) are retrieving from data source, that responds with delay (+2000ms), but cache saves this time for repeated call <u>from any App's node</u>.
            <table>
                <tr>
                    <td>
                        <br><h2>To grasp cache effect:</h2><br>
                        <ul>
                            <li>Load resources on first node (blue button)</li>
                            <li>Go to any other node</li>
                            <li>Load same resource on second node</li>
                            <li>Notice, that 'loading time' is significantly improved!</li>
                        </ul>
                    </td>
                    <td style="width: 20%"></td>
                    <td style="vertical-align: top; text-align:right">
                        <br><h2>Application nodes:</h2><br>
                        <c:forEach items="${nodes}" var="node">
                            <a href="${node}">${node}</a><br>
                        </c:forEach>
                    </td>
                </tr>
            </table>
            <br>
            <p style="color: blue">Note: you could repeat test by evicting cached data (button in right corner)</p>
        </div>

        <h2 style="color: chocolate">Available Blogs:</h2>
        <hr/>
        <div class="row">
            <c:forEach items="${blogIds}" var="blogid">
                <div class="col-lg-4 blog" id="${blogid}">
                    <h2>Blog id: ${blogid}</h2>
                    <div style="text-align: center">
                        <button class="btn btn-default">
                            <img src="load.png" height="100px" onclick="loadBlog(this.parentElement.parentElement.parentElement)"/>
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div style="text-align: right">
            <a class="btn btn-danger" onclick="evictCache()" role="button">Evict cache</a></p>
        </div>
    </div>
    </body>

</html>