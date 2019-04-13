package com.flinm.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flinm.dao.HBaseDAO;
import com.flinm.pojo.TBDescribe;
import com.google.gson.Gson;

/**
 * 一系列数据库后台操作
 */
@WebServlet("/showhbase")
public class ShowHBaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private HBaseDAO hbase;
	
    public ShowHBaseServlet() {
    	hbase = new HBaseDAO();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String tableName = "dp:" + request.getParameter("tableName");
		String rowkey = request.getParameter("rowkey");
		String colFamily = request.getParameter("columnFamily");
		String col = request.getParameter("qualifier");
		String val = request.getParameter("cell");
		
		if (action.equals("add")) {   //添加
			hbase.insertRow(tableName, rowkey, colFamily, col, val);
			response.sendRedirect("/index.html");
		}else if (action.equals("update")) {    //更新
			hbase.insertRow(tableName, rowkey, colFamily, col, val);
			response.sendRedirect("/index.html");
		}else if (action.equals("delete")) {   //删除
			hbase.deleteRow(tableName, rowkey, colFamily, col);
			response.sendRedirect("/index.html");
		}else if (action.equals("bycondition")) {
			List<TBDescribe> data = null;
			try {
				data = hbase.selectByCondition("dp:dept");
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setCharacterEncoding("UTF-8");  
			response.setContentType("application/json; charset=utf-8"); 
			//转化为JSON数据
			Gson gson = new Gson();
			response.getWriter().print(gson.toJson(data));
		}else if (action.equals("subdept")) {
			List<TBDescribe> data = null;
			try {
				data = hbase.selectSubdeptByRowkey("dp:dept", "1_001");
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setCharacterEncoding("UTF-8");  
			response.setContentType("application/json; charset=utf-8"); 
			//转化为JSON数据
			Gson gson = new Gson();
			response.getWriter().print(gson.toJson(data));
		}
		
	}

}
