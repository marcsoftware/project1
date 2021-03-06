package com.revature;

import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.LinkedList; 

public class reviewInfo extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
				
				
				response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
			
         
      // Get an array of Cookies associated with this domain
	  
	  
		Cookie user=getCookie(request, "user");
		Cookie password=getCookie(request, "password");		
		DataManager session = new DataManager();

		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
		.append("<html>\r\n")
		.append("		<head>\r\n");
		
				session.connect();
				String rank=session.getRank(user.getValue(), password.getValue());
				if(!rank.equals("admin")){
					writer.append("			<title>list of associates</title>\r\n")
					.append("		</head>\r\n")
					.append("		<body>\r\n")
					.append("<p>You are not an admin.</p>\r\n")
					.append("<a href='/app/homepage'>home</a><br/>")
					.append("		</body>\r\n")
					.append("</html>\r\n");
			  		
					return;
				}
				LinkedList<String> result=session.listAllEmployees();

				// create HTML form
		
	
			  writer.append("			<title>review</title>\r\n")
			  .append("<style>#customers { font-family: 'Trebuchet MS, Arial, Helvetica, sans-serif'; border-collapse: collapse; width: 100%; } #customers td, #customers th { border: 1px solid #ddd; padding: 8px; width:10%} #customers tr:nth-child(even){background-color: #f2f2f2;} #customers tr:hover {background-color: #ddd;} #customers th { padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #4CAF50; color: white; }</style>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>"+user.getValue( )+"</p>\r\n");
			  writer.append("<h6>list of associates</h6> ");	
		writer.append("	<form action='review' method='post'>  ");	  
		// Traditional for loop approach
		writer.append("<table id='customers' style='width:100%'>");
		
		writer.append("<tr>");
		writer.append("<th>username</th>");
		writer.append("<th>realname</th>");
		
		writer.append("<th>email</th>");
		writer.append("<th>status</th>");
	
		writer.append("</tr>");
		for (int i = 0; i < result.size(); i=i+4) {
			
		
			writer.append("<tr>");
			writer.append("<td id='cell'><a href='reviewHistory?t="+result.get(i+3)+"'>"+result.get(i+3)+"</a></td>");
			writer.append("<td id='cell'>"+result.get(i+2)+"</td>");
			writer.append("<td id='cell'>"+result.get(i+1)+"</td>");
			writer.append("<td id='cell'>"+result.get(i+0)+"</td>");
		
			writer.append("</tr>");
		}

		writer.append("</table>");

		

		writer.append("	<input type='submit' value='save'/>  </form>  ");	
		writer.append("<a href='/app/review'>review</a><br/>");
			 

			
			writer.append("<a href='/app/reviewResolved'>Resolved</a><br/>");
			
			
			writer.append("list workers<br/>");
		writer.append("<a href='/app/logout'>logout</a><br/>");
		// set response headers
		
		
		
			  
		writer.append("			<p>this is the view</p>\r\n")
	
			  .append("		</body>\r\n")
			  .append("</html>\r\n");
			
	}


	

	public void destroy(  ) {
		
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
