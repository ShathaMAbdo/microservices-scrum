<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	    <title>Task form</title>
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

                                </ul>
                            </div>
                        </div>
                    </nav>
             </div>
	    <div class="container">
	        <h3 id="form_header" class="text-warning" align="center">Task of sprint ${sprintname}</h3>
	        <div>&nbsp;</div>

			<!-- Task input form to add a new task or update the existing task-->
	        <c:url var="saveUrl" value="/api/task/save?sprintid=${sprintid}" />
	        <form:form id="task_form" modelAttribute="taskAttr" method="POST" action="${saveUrl}">
	        	<form:hidden path="id" />
	        	<label for="task_name">Enter Priority: </label>
                <form:input id="task_name" type="number" min="0" cssClass="form-control" path="priority" />
	            <label for="task_name">Enter Name: </label>
	            <form:input id="task_name" cssClass="form-control" path="name" />
	            <label for="task_name">Enter StoryPoints: </label>
                <form:input id="task_name" type="number" min="0" cssClass="form-control" path="storyPoints" />
	            <div>&nbsp;</div>
	             <label for="task_name">Sub Tasks : </label>

                	  <table id="subtasks_table" class="table">
                       <thead>
                        <tr align="center">
                        <th>Sub Task Name</th>
                         <th colspan="2"></th>
                           </tr>
                            </thead>
                            	<tbody>
                            	    <c:forEach items="${taskAttr.subTasks}" varStatus="st" var="subTask"   >
                            	        <tr align="left">
                            	            <td><c:out  value="${subTask.name}"/></td>
                                            <td><type="hidden" value="${subTask.id}" /></td>

                            	            <td>
                                                <c:url var="editUrl" value="/api/subtask/edit?id=${subTask.id}&taskid=${taskAttr.id}&sprintid=${sprintid}" /><a id="update" href="${editUrl}" class="btn btn-warning">Update</a>
                                            </td>
                                            <td>
                                                <c:url var="deleteUrl" value="/api/subtask/delete?id=${subTask.id}&taskid=${taskAttr.id}&sprintid=${sprintid}" /><a id="delete" href="${deleteUrl}" class="btn btn-danger">Delete</a>
                                            </td>
                                         </tr>
                            	    </c:forEach>
                            	</tbody>
                      </table>
	            <button id="saveBtn" type="submit" class="btn btn-primary">Save</button>
	             <c:if test="${taskAttr.id != null}">
	                  <c:url var="addUrl" value="/api/subtask/add?taskid=${taskAttr.id}&sprintid=${sprintid}" /><a id="add" href="${addUrl}" class="btn btn-info">Add subTask</a>
                 </c:if>
                <c:url var="CancelUrl" value="/api/task/tasks?sprintid=${sprintid}" /><a id="cancel" href="${CancelUrl}" class="btn btn-danger">Cancel</a>
	        </form:form>
	</body>
</html>