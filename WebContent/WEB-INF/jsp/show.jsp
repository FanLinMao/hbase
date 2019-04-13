<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.flinm.pojo.TBDescribe"%>
<%@page import="java.util.List"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% 
	String basePath = request.getScheme()+"://"+request.getServerName() +":"+ request.getServerPort() + request.getContextPath() + "/"; 
	//out.print(basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Show HBase DataBase</title>
	<!-- 引入bootstrap样式文件 -->
	<link rel="stylesheet" href="<%=basePath %>bootstrap/css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="<%=basePath %>bootstrap/css/bootstrap.min.css">
	<style type="text/css">
		*{
			margin: 0px;
			padding: 0px;
		}
		html,body{
			width: 100%;
			height: 100%;
		}
		#left_bar{
			
			width:15%;
			height:50%;
			border: 1px solid #CCCCCC;
			border-radius: 5px;
			float:left;
		}
		#right_box{
			
			margin-right: 2.5%;
			width:80%;
			height:70%;
			float:right;
			border: 1px solid #CCCCCC;
			border-radius: 5px;
		}
	</style>
</head>

<body>
	<%-- <% 
		List<TBDescribe> tbInfo = (List<TBDescribe>) request.getAttribute("tableInfo");
	%> --%>
	<div class="page-header" style="text-align: center;">
	    <h1>HBase数据库展示
	        <small>Servlet+JSP+Ajax</small>
	    </h1>
	</div>
	
	<div id="left_bar">
		<ul class="nav nav-pills nav-stacked">
		  <li class="active"><a href="#">展示默认dept数据表</a></li>
		  <li><a href="#">添加数据</a></li>
		  <li><a href="#">更新数据</a></li>
		  <li><a href="#">删除数据</a></li>
		  <li><a href="#">查询所有一级部门</a></li>
		  <li><a href="#">根据rk查所有子部门</a></li>
		</ul>
	</div>
	<div id="right_box">
		<div class="panel panel-default">
		    <div class="panel-body">
		    	<!-- 展示数据 -->
		        <table id="showData" class="table table-hover">
					<caption>TbaleName: dept</caption>
					<thead>
						<tr>
							<th>RowKey(行键)</th>
							<th>ColumnFamily(列簇)</th>
							<th>Qualifier(列名)</th>
							<th>Cell(值)</th>
						</tr>
						
					</thead>
					<tbody>
				    	<c:choose>
				    		<c:when test="${not empty tableInfo }">
								<c:forEach var="info" step="1" items="${tableInfo }">
									<tr>
										<td>${info.rowkey }</td>
										<td>${info.columnFamily }</td>
										<td>${info.qualifier }</td>
										<td>${info.cellValue }</td>
									</tr>
								</c:forEach>
				    		</c:when>
				    		<c:otherwise>
				    			<tr>
									<td rowspan="2" colspan="4"><h3>没有查询到数据..</h3></td>
								</tr>
								<tr>
									
								</tr>
				    		</c:otherwise>
				    	</c:choose>
			    	</tbody>
				</table>
				<!-- 添加数据 -->
				<form id="addData" style="display: none;" class="form-horizontal" role="form" action="/showhbase?action=add" method="post">
				  <div class="form-group">
				    <label for="tableName" class="col-sm-2 control-label">TableName(表名)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="tableName" name="tableName">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="rowkey" class="col-sm-2 control-label">Rowkey(行键)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="rowkey" name="rowkey">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="columnFamily" class="col-sm-2 control-label">ColumnFamily(列簇)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="columnFamily" name="columnFamily">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="qualifier" class="col-sm-2 control-label">Qualifier(限定名)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="qualifier" name="qualifier">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="cell" class="col-sm-2 control-label">Cell(值)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="cell" name="cell">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-2 col-sm-10">
				      <button type="submit" class="btn btn-info">添加</button>
				    </div>
				  </div>
				</form>
				<!-- 更新数据 -->
				<form id="updateData" style="display: none;" class="form-horizontal" role="form" action="/showhbase?action=update" method="post">
				  <div class="form-group">
				    <label for="tableName" class="col-sm-2 control-label">TableName(表名)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="tableName" name="tableName">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="rowkey" class="col-sm-2 control-label">Rowkey(行键)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="rowkey" name="rowkey">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="columnFamily" class="col-sm-2 control-label">ColumnFamily(列簇)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="columnFamily" name="columnFamily">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="qualifier" class="col-sm-2 control-label">Qualifier(限定名)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="qualifier" name="qualifier">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="cell" class="col-sm-2 control-label">Cell(值)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="cell" name="cell">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-2 col-sm-10">
				      <button type="submit" class="btn btn-info">更新</button>
				    </div>
				  </div>
				</form>
				<!-- 删除数据 -->
				<form id="deleteData" style="display: none;" class="form-horizontal" role="form" action="/showhbase?action=delete" method="post">
				  <div class="form-group">
				    <label for="tableName" class="col-sm-2 control-label">TableName(表名)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="tableName" name="tableName">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="rowkey" class="col-sm-2 control-label">Rowkey(行键)</label>
				    <div class="col-sm-5">
				      <input type="text" class="form-control" id="rowkey" name="rowkey">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-2 col-sm-10">
				      <button type="submit" class="btn btn-info">删除</button>
				    </div>
				  </div>
				</form>
				<!-- 展示数据 -->
		        <table id="showData1" style="display: none;" class="table table-hover">
					<caption>TbaleName: dept</caption>
					<thead>
						<tr>
							<th>RowKey(行键)</th>
							<th>ColumnFamily(列簇)</th>
							<th>Qualifier(列名)</th>
							<th>Cell(值)</th>
						</tr>
						
					</thead>
					<tbody id="tbody">
				    	
			    	</tbody>
				</table>
		    </div>
		</div>
	</div>
	<!-- 先引入jquery文件 -->
	<script src="<%=basePath %>bootstrap/js/jquery-1.11.0.js"></script>
	<!-- 后引入bootstrap文件 -->
	<script src="<%=basePath %>bootstrap/js/bootstrap.min.js"></script>
	<script>
		$(function(){
			$("#left_bar > ul > li").each(function(index){
				if (index == 0){
		           $(this).click(function () {
		           	   $("li").removeClass("active");
		           	   $(this).addClass("active");
		               $("#showData").show();
		               $("#addData").hide();
		               $("#updateData").hide();
		               $("#deleteData").hide();
		               $("#showData1").hide();
		           });
		       } else if (index == 1) {
		           $(this).click(function () {
		           		$("li").removeClass("active");
		           	   $(this).addClass("active");
		               $("#showData").hide();
		               $("#addData").show();
		               $("#updateData").hide();
		               $("#deleteData").hide();
		               $("#showData1").hide();
		           });
		       } else if (index == 2) {
		           $(this).click(function () {
		           		$("li").removeClass("active");
		           	   $(this).addClass("active");
		               $("#showData").hide();
		               $("#addData").hide();
		               $("#updateData").show();
		               $("#deleteData").hide();
		               $("#showData1").hide();
		           });
		       } else if (index == 3) {
		           $(this).click(function () {
		           		$("li").removeClass("active");
		           	   $(this).addClass("active");
		               $("#showData").hide();
		               $("#addData").hide();
		               $("#updateData").hide();
		               $("#deleteData").show();
		               $("#showData1").hide();
		           });
		       }else if (index == 4) {
		           $(this).click(function () {
		           		$("li").removeClass("active");
		           	   $(this).addClass("active");
		               $("#showData").hide();
		               $("#addData").hide();
		               $("#updateData").hide();
		               $("#deleteData").hide();
		               $.ajax({
		            	  	url: '/showhbase?action=bycondition',
		            		type: 'get',
		            		async: false,
		            		dataType: 'json',
		            		success: function(data){
		            			$("#showData1").show();
		            			$("#tbody tr").remove();
		            			for(var i=0; i<data.length; i++){
		            				$("#tbody").append("<tr>"+
											"<td>"+data[i].rowkey+"</td>"+
											"<td>"+data[i].columnFamily +"</td>"+
											"<td>"+data[i].qualifier +"</td>"+
											"<td>"+data[i].cellValue +"</td>"+
											"</tr>")
		            			}
		            		}
		               });
		           });
		       }else if (index == 5) {
		           $(this).click(function () {
		           		$("li").removeClass("active");
		           	   $(this).addClass("active");
		               $("#showData").hide();
		               $("#addData").hide();
		               $("#updateData").hide();
		               $("#deleteData").hide();
		               $.ajax({
		            	  	url: '/showhbase?action=subdept',
		            		type: 'post',
		            		dataType: 'json',
		            		async: false,
		            		success: function(data){
		            			console.log(data);
		            			$("#showData1").show();
		            			$("#tbody tr").remove();
		            			for(var i=0; i<data.length; i++){
		            				$("#tbody").append("<tr>"+
											"<td>"+data[i].rowkey+"</td>"+
											"<td>"+data[i].columnFamily +"</td>"+
											"<td>"+data[i].qualifier +"</td>"+
											"<td>"+data[i].cellValue +"</td>"+
											"</tr>")
		            			}
		            		}
		               });
		           });
		       }
			});
		});
		
	</script>
</body>
</html>