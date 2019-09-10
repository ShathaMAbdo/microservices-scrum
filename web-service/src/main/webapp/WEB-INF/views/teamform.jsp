<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	    <title>Create Team</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
	</head>
	<body>
	<div class="container">
                <nav class="navbar navbar-default">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <ul class="nav navbar-nav">
                                <li><a class="navbar-brand" href="/" th:href="@{/}">Home</a></li>
                                <li><a href="/api/user/users"style="color:red;" th:href="@{/api/user/users}">USERS</a></li>
                                <li><a href="/api/team/teams" style="color:red;"th:href="@{/api/team/teams}">Teams</a></li>
                                <li><a href="/api/sprint/sprints"style="color:red;" th:href="@{api/sprint/sprints}">SPRINTS</a></li>
                                <li><a href="/api/sprint/add"style="color:red;" th:href="@{/api/sprint/add}">Create Sprint</a></li>
                            </ul>

                        </div>
                    </div>
                </nav>
         </div>
	    <div class="container">
	        <h3 id="form_header" class="text-warning" align="center"> Team</h3>
	        <div>&nbsp;</div>

			<!-- Team input form to add a new team or update the existing team-->
	        <c:url var="saveUrl" value="/api/team/save" />
	        <form:form id="team_form" modelAttribute="teamAttr"  method="POST" action="${saveUrl}">
	        	<form:hidden path="id"  />
	            <label for="team_name">Enter Name: </label>
	            <form:input id="team_name" cssClass="form-control" name="id" var="xx" path="name" />
	            <label for="team_name">Team Activity: </label>
                <form:select id="team_name" cssClass="form-control" path="Active">
                        <form:option value="true" label="true"/>
                        <form:option value="false" label="false"/>
                        </form:select>
                <div>&nbsp;</div>
                <label for="team_name">Team members: </label>
                <div>&nbsp;</div>
	            <table id="users_table" class="table">
            	        	<thead>
            	            	<tr align="center">
            	            		<th>Name</th>
            	                    <th>Member in team<th>
            	            		<th colspan="2"></th>
            	            	</tr>
            	        	</thead>
            	        	<tbody>
            	            	<c:forEach items="${teamAttr.users}" varStatus="us" var="user"   >
            	                	<tr align="left">
            	                		<td><c:out value="${user.name}" /></td>
                                        <td><form:checkbox path="users[${us.index}].active"  value="${user.active}"/></td>
                                        <td><form:input type="hidden" path="users[${us.index}].name" value="${user.name}"/></td>
            	                    	<td><form:input type="hidden" path="users[${us.index}].id" value="${user.id}" /></td>
            	                    	<td><form:input type="hidden" path="users[${us.index}].password" value="${user.password}" /></td>
            	                    	<td><form:input type="hidden" path="users[${us.index}].username" value="${user.username}" /></td>
            	                    	<td><form:input type="hidden" path="users[${us.index}].roles" value="${user.roles}" /></td>
            	                    	<td><form:input type="hidden" path="users[${us.index}].email" value="${user.email}" /></td>
            	                    	<td><form:input type="hidden" path="users[${us.index}].phone" value="${user.phone}" /></td>
            	                    	<td><form:input type="hidden" path="users[${us.index}].city" value="${user.city}" /></td>
            	                	</tr>
            	            	</c:forEach>
            	        	</tbody>
            	 </table>
            	<button  id="saveBtn" type="submit" class="btn btn-primary">Save</button>
            	 <c:if test="${teamAttr.id != null}">
             	  <c:url var="addUrl" value="/api/team/members?id=${teamAttr.id}" /><a id="add" href="${addUrl}" class="btn btn-info">Add member</a>
                  </c:if>

            	<c:url var="CancelUrl" value="/api/team/teams" /><a id="cancel" href="${CancelUrl}" class="btn btn-danger">Cancel</a>

            	</form:form>
	    </div>

	</body>
</html>