package com.revature;

import com.revature.dao.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.Scanner;



public class register extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
			
		// set response headers
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML form
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  .append("			<title>register</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("		register\r\n")
			  .append("			<form action=\"register\" method=\"POST\">\r\n")
			  .append("				name: \r\n")
			  
			  .append("				<input type=\"text\" name=\"user\" />\r\n")
			  .append("				password: \r\n")
			  .append("				<input type=\"password\" name=\"password\" />\r\n")
			  .append("				<input type=\"submit\" value=\"Submit\" />\r\n")
			  .append("			</form>\r\n")
			  .append("<a href='/app/'>login</a><br/>")
			  .append("		</body>\r\n")
			  .append("</html>\r\n");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				String user = request.getParameter("user");
				String password = request.getParameter("password");
				DataManager session = new DataManager();
				session.connect();
				Boolean result = session.register(user,password);
			
			
				
		

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML response
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  .append("			<title>register</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n");

			  
		if (user != null && !user.trim().isEmpty() && result) {
			writer.append("	Welcome " + user + ".\r\n");
			writer.append("	You have regsiter a new account\r\n");
			writer.append("<a href='/app/login'>login</a><br/>");
		}else if(!result){
			writer.append("	That user already exsists.\r\n");
		} else {
			writer.append("	something went wrong.\r\n");
		}
		writer.append("		</body>\r\n")
			  .append("</html>\r\n");
	}	
	

	public void destroy(  ) {
		
	  }


	  
	  

}
