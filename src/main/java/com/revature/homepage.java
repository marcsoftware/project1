package com.revature;

import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  



import java.util.Scanner;



public class homepage extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
         
      // Get an array of Cookies associated with this domain
	  Cookie[] cookies = request.getCookies();
	  
		Cookie user=getCookie(request, "user");
		
		Cookie password=getCookie(request, "password");


		PrintWriter writer = response.getWriter();
		DataManager session = new DataManager();
		session.connect();

		String rank;
		try{
			rank=session.getRank(user.getValue(), password.getValue());
		}catch(Exception e){
			rank="none";
		}
		writer.append("<!DOCTYPE html>\r\n");
		writer.append("<html>\r\n")
			   .append("		<head>\r\n")
			   
			   .append("			<title>homepage</title>\r\n");
			   writer.append("		</head>\r\n");
				writer.append("		<body>\r\n");
		if(rank.equals("admin")){

			writer.append("<p>manager homepage.</p>\r\n");
			writer.append("<a href='/app/review'>review</a><br/>");
			writer.append("<a href='/app/reviewResolved'>Resolved</a><br/>");
			writer.append("<a href='/app/reviewInfo'>list workers</a><br/>");
			writer.append("<a href='/app/logout'>logout</a><br/>");
	
		}else if(rank.equals("employee")){
		
			
			writer.append("<p>employee homepage.</p>\r\n");
			writer.append("<a href='/app/request'>request</a><br/>");
			writer.append("<a href='/app/view'>request history</a><br/>");
			writer.append("<a href='/app/Profile'>Profile</a><br/>");
			writer.append("<a href='/app/logout'>logout</a><br/>");
		
		}else{
			
			writer.append("<p>you are not logged in</p>");
			writer.append("<a href='/app/login'>login</a><br/>");
			writer.append("<a href='/app/register'>register</a><br/>");
		
		}

	
	
		
		// create HTML form
		
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
