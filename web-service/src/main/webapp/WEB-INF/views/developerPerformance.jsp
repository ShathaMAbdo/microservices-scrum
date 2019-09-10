<!-- chart.jsp-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Performance</title>
<script type="text/javascript">
window.onload = function() {

var dps = [[]];
var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2", // "light1", "dark1", "dark2"
	title: {
		text: "Developers Performance "
	},
			dataPointWidth: 50,

	subtitles: [{
		text: "Based on story points"
	}],
	axisY: {
		title: "Story Points",
		includeZero: true
	},
	axisX: {
		title: "Developers",
	},
	data: [{
		type: "column",
		yValueFormatString: "#,##0",
		dataPoints: dps[0]
	}]
});

var yValue;
var label;

<c:forEach items="${dataPointsList}" var="dataPoints" varStatus="loop">
	<c:forEach items="${dataPoints}" var="dataPoint">
		yValue = parseInt("${dataPoint.y}");
		label = "${dataPoint.label}";
		dps[parseInt("${loop.index}")].push({
			label : label,
			y : yValue,
		});
	</c:forEach>
</c:forEach>

chart.render();

}
</script>
</head>
<body>
<h1 id="form_header" class="text-warning" align="center" style="font-weight:bold">Sprint: ${sprintname}  </h1>
<h1 id="form_header" class="text-warning" align="center" style="font-weight:bold"> Team: ${teamname}  </h1>
	<div id="chartContainer" style="height: 370px; width: 100%;"></div>
	<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
</body>
<footer>
<p>
<script> document.write(new Date().toLocaleDateString()); </script>
</p>
</footer>

</html>