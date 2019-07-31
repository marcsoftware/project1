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
			  .append("<style>table,th,td{border: 1px solid black;}td{width:10%;}</style>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>:::"+user.getValue( )+"</p>\r\n");
		// Traditional for loop approach
		writer.append("<table style='width:100%'>");
		writer.append("<tr>");
		writer.append("<th>reference#</th>");
		writer.append("<th>status</th>");
		
		writer.append("<th>picture</th>");
		writer.append("<th>description</th>");
		writer.append("<th>amount</th>");

		writer.append("</tr>");
		/**al.push(amount);
                al.push(comment);
                al.push( picture );
                al.push(status);
                al.push(id); */
		for (int i = 0; i < result.size(); i=i+5) {
			System.out.println(result.get(i));
		

			writer.append("<tr>");
			writer.append("			<td id='cell'>"+result.get(i)+"</td>");
			writer.append("			<td id='cell'>"+result.get(i+1)+"</td>");
			writer.append("			<td id='cell'>"+result.get(i+2)+"</td>");
			writer.append("			<td id='cell'>"+result.get(i+3)+"</td>");
			writer.append("			<td id='cell'>"+result.get(i+4)+"</td>");
			writer.append("</tr>");
		}
	
		writer.append("</table>");
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
