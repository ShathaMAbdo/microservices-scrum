<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Sprint charts</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>
<div class="container">
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <ul class="nav navbar-nav">
                            <li><a class="navbar-brand" href="/" th:href="@{/}">Home</a></li>
                            <li><a href="/api/user/users"style="color:red;" th:href="@{/api/user/users}">USERS</a></li>
                            <li><a href="/api/team/teams" style="color:red;"th:href="@{api/team/teams}">TEAMS</a></li>
                            <li><a href="/api/team/add" style="color:red;"th:href="@{/api/team/add}">Create Team</a></li>
                            <li><a href="/api/sprint/sprints"style="color:red;" th:href="@{api/sprint/sprints}">SPRINTS</a></li>
                            <li><a href="/api/sprint/add"style="color:red;" th:href="@{/api/sprint/add}">Create Sprint</a></li>
                        </ul>

                    </div>
                </div>
            </nav>
     </div>
<div class="container">
        <div class="jumbotron">
            <div class="row text-center">
                <div class="">
                 <h1><font color="red">Sprint charts</h1>
                 <a style="color:black;" href="/api/sprint/canvasjschart?sprintid=${sprintid}" color="red" target="_blank"><h4> Planned/Actual Todo Chart</h3></a>
                 <a style="color:black;" href="/api/sprint/canvasjschart1?sprintid=${sprintid}" color="red" target="_blank"><h4> Planned/Actual Remaining Chart</h3></a>
                 <a style="color:black;" href="/api/sprint/canvasjschart2?sprintid=${sprintid}" color="red" target="_blank"><h4> Developers Performance </h3></a>

            </div>
            <div class="row text-center">
                <img src="${pageContext.request.contextPath}/images/logo-charts.png" width="800" height="350"/>
            </div>

        </div>

  </div>


</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
