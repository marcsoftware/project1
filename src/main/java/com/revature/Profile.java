//this is the form for submitting reimbursement
package com.revature;

import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  

import java.util.LinkedList; 






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
			  
			  .append("			<title>Profile</title>\r\n");

			  if(!rank.equals("employee")){
		
				writer.append("		</head>\r\n")
				.append("		<body>\r\n")
				.append("<p>You do not have permission to view this page.</p>\r\n")
				.append("<a href='/app/homepage'>home</a><br/>")
				.append("		</body>\r\n")
				.append("</html>\r\n");
				  
				return;
			}
			  writer.append("		</head>\r\n");

			  LinkedList<String> result=session.getProfile(user.getValue());

			// Traditional for loop approach
	
			String fullname="";
			String email="";
		for (int i = 0; i < result.size(); i=i+5) {
	
				 fullname= result.get(i);
				 email = result.get(i+1);
				 rank= result.get(i+2);

		}
	
		

			  writer.append("		<body>")
			  .append("			<p>"+user.getValue( )+"</p>")
			  .append("			<h5>Profile:</h5>")
			  
			  .append("			<form action=\"Profile\" method=\"POST\">")
			  .append("				full name: ")
			  
			  .append("				<input type=\"text\" name=\"fullname\" value='"+fullname+"'/><br/>")
			  .append("				e-mail: ")
			  .append("				<input type=\"text\" name=\"email\" value='"+email+" '/><br/>")
			  .append("				role: "+rank+"<br/>")
			  
			  .append("				<input type=\"submit\" value='update profile'/>")
			  .append("			</form>");
			  writer.append("<br/><a href='/app/request'>request</a><br/>")
			  .append("<a href='/app/view'>view</a><br/>")
			  .append("Profile<br/>")
			  .append("<a href='/app/logout'>logout</a><br/>")
			  .append("		</body>")
			  .append("</html>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				Cookie[] cookies = request.getCookies();
	  
				Cookie user=getCookie(request, "user");

				String realname = request.getParameter("fullname");
				String email = request.getParameter("email");
				
				
				DataManager session = new DataManager();
		session.connect();
				session.updateProfile(user.getValue(),realname,email );
				//Boolean result = session.register(user,password);
			
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML response
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  .append("			<title>Your profile was updated.</title>\r\n")
			  
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>Your profile was updated.</p>\r\n");
			  writer.append("<br/><a href='/app/request'>request</a><br/>")
			  .append("<a href='/app/Profile'>Profile</a><br/>")
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
