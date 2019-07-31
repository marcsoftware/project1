package com.revature;

import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.LinkedList; 


import java.util.Scanner;



public class view extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
				
				
				response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
			
         
      // Get an array of Cookies associated with this domain
	  Cookie[] cookies = request.getCookies();
	  
		Cookie user=getCookie(request, "user");

		DataManager session = new DataManager();

				
				session.connect();
				
				LinkedList<String> result=session.listRequests(user.getValue());

				// create HTML form
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  
			  .append("			<title>view</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>:::"+user.getValue( )+"</p>\r\n");
		// Traditional for loop approach
		for (int i = 0; i < result.size(); i=i+5) {
			System.out.println(result.get(i));
			writer.append("			<span id='cell'>"+result.get(i)+"</span>\r\n");
			writer.append("			<span id='cell'>"+result.get(i+1)+"</span>\r\n");
			writer.append("			<span id='cell'>"+result.get(i+2)+"</span>\r\n");
			writer.append("			<span id='cell'>"+result.get(i+3)+"</span>");
			writer.append("			<span id='cell'>"+result.get(i+4)+"</span><br/>");
		}
	
		writer.append("<br/><a href='/app/request'>request</a><br/>")
			  .append("<a href='/app/view'>view</a><br/>")
			  .append("<a href='/app/logout'>logout</a><br/>");
		// set response headers
		
		
		
			  
		writer.append("			<p>this is the view</p>\r\n")
	
			  .append("		</body>\r\n")
			  .append("</html>\r\n");
	}


	

	public void destroy(  ) {
		
	}


	public void createSession(HttpServletRequest request, HttpServletResponse response){
		try{  
			response.setContentType("text/html");  
			PrintWriter out = response.getWriter();  
			
			String n=request.getParameter("user");  
			out.print("Welcome "+n);  
			
			HttpSession session=request.getSession();  
			session.setAttribute("uname",n);  
	
			out.print("<a href='/app/request'>apply</a>");  
					
			out.close();  
  
        }catch(Exception e){
			System.out.println(e);
		}  
   		   
	}
	  
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}
}
