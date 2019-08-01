//this is the form for submitting reimbursement
package com.revature;

import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  



import java.util.Scanner;



public class Profile extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
				
				
				
			
         
      // Get an array of Cookies associated with this domain
	  Cookie[] cookies = request.getCookies();
	  
		Cookie user=getCookie(request, "user");
		Cookie password=getCookie(request, "password");
		DataManager session = new DataManager();
		session.connect();
		String rank=session.getRank(user.getValue(), password.getValue());
		// set response headers
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML form
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  
			  .append("			<title>request</title>\r\n");

			  if(!rank.equals("employee")){
		
				writer.append("		</head>\r\n")
				.append("		<body>\r\n")
				.append("<p>You do not have permission to view this page.</p>\r\n")
				.append("<a href='/app/homepage'>home</a><br/>")
				.append("		</body>\r\n")
				.append("</html>\r\n");
				  
				return;
			}
			  writer.append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>:::"+user.getValue( )+"</p>\r\n")
			  .append("		form:\n\r")
			  .append("			<form action=\"request\" method=\"POST\">\r\n")
			  .append("				amount: \r\n")
			  
			  .append("				<input type=\"text\" name=\"amount\" />\r\n")
			  .append("				comment: \r\n")
			  .append("				<input type=\"text\" name=\"comment\" />\r\n")
			  .append("				picture: \r\n")
			  .append("				<input type=\"text\" name=\"picture\" />\r\n")
			  .append("				<input type=\"submit\" value=\"Submit\" />\r\n")
			  .append("			</form>\r\n");
			  writer.append("<br/><a href='/app/request'>request</a><br/>")
			  .append("<a href='/app/view'>view</a><br/>")
			  .append("<a href='/app/logout'>logout</a><br/>")
			  .append("		</body>\r\n")
			  .append("</html>\r\n");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				Cookie[] cookies = request.getCookies();
	  
				Cookie user=getCookie(request, "user");

				String amount = request.getParameter("amount");
				String comment = request.getParameter("comment");
				String picture = request.getParameter("picture");
				
				DataManager session = new DataManager();
		session.connect();
				session.addNewRequest(user.getValue(), amount, comment, picture);
				//Boolean result = session.register(user,password);
			
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML response
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  .append("			<title>request was submitted</title>\r\n")
			  
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>Your request was submitted.</p>\r\n");
			  writer.append("<br/><a href='/app/request'>request</a><br/>")
			  .append("<a href='/app/view'>view</a><br/>")
			  .append("<a href='/app/logout'>logout</a><br/>");
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
