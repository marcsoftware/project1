package com.revature;

import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  



import java.util.Scanner;



public class login extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
				
				
				
			
         
      // Get an array of Cookies associated with this domain
	  Cookie[] cookies = request.getCookies();
	  
		Cookie user=getCookie(request, "user");
	
		// set response headers
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML form
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  
			  .append("			<title>login</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>:::"+user.getValue( )+"</p>\r\n")
			  .append("		login:\n\r")
			  .append("			<form action=\"login\" method=\"POST\">\r\n")
			  .append("				name: \r\n")
			  
			  .append("				<input type=\"text\" name=\"user\" />\r\n")
			  .append("				password: \r\n")
			  .append("				<input type=\"password\" name=\"password\" />\r\n")
			  .append("				<input type=\"submit\" value=\"Submit\" />\r\n")
			  .append("			</form>\r\n")
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
				
				Cookie test = new Cookie("user", user);
				test.setMaxAge(60*60*24);
				response.addCookie( test );
				 test = new Cookie("password", password);
				test.setMaxAge(60*60*24);
				response.addCookie( test );
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML response
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  .append("			<title>logged in</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n");

			  
		if (user != null && !user.trim().isEmpty()) {
			writer.append("	Welcome " + user + ".\r\n");
			writer.append("	You are logged in.\r\n");
			createSession( request,  response);
		} else {
			writer.append("	some went wrong.\r\n");
		}
		writer.append("		</body>\r\n")
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
