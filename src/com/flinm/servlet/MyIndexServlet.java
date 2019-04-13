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

/**
 * 首页显示
 */
@WebServlet("/index.html")
public class MyIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private HBaseDAO hbase;
	
    public MyIndexServlet() {
    	hbase = new HBaseDAO();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<TBDescribe> tbInfo = hbase.selectTable("dp:dept");
			request.setAttribute("tableInfo", tbInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("/WEB-INF/jsp/show.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
