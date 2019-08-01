package com.revature;
import java.util.Enumeration;
import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.LinkedList; 


import java.util.Scanner;



public class review extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
				
				
				response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
			
         
      // Get an array of Cookies associated with this domain
	  Cookie[] cookies = request.getCookies();
	  
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
					writer.append("			<title>review</title>\r\n")
					.append("		</head>\r\n")
					.append("		<body>\r\n")
					.append("<p>You are not an admin.</p>\r\n")
					.append("<a href='/app/homepage'>home</a><br/>")
					.append("		</body>\r\n")
					.append("</html>\r\n");
			  		
					return;
				}
				LinkedList<String> result=session.listStatus("pending");

				// create HTML form
		
	
			  writer.append("			<title>review</title>\r\n")
			  .append("<style>#customers { font-family: 'Trebuchet MS, Arial, Helvetica, sans-serif'; border-collapse: collapse; width: 100%; } #customers td, #customers th { border: 1px solid #ddd; padding: 8px; width:10%} #customers tr:nth-child(even){background-color: #f2f2f2;} #customers tr:hover {background-color: #ddd;} #customers th { padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #4CAF50; color: white; }</style>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  .append("			<p>"+user.getValue( )+"</p>\r\n");

		writer.append("	<form action='review' method='post'>  ");	  
		// Traditional for loop approach
		writer.append("<table id='customers' style='width:100%'>");
		
		writer.append("<tr>");
		writer.append("<th>-</th>");
		writer.append("<th>name</th>");
		
		writer.append("<th>picture</th>");
		writer.append("<th>comment</th>");
		writer.append("<th>amount</th>");

		writer.append("</tr>");
		for (int i = 0; i < result.size(); i=i+7) {
			System.out.println(result.get(i));
		
			writer.append("<tr>");
   
			writer.append("<td>");
			writer.append("<input type='radio' name='"+result.get(i)+"' value='yes'> yes");
			writer.append("<input type='radio' name='"+result.get(i)+"' value='no'> no");
			writer.append("<input type='radio' name='"+result.get(i)+"' value='pending'> pending");
			writer.append("</td>");
			writer.append("<td id='cell'>"+result.get(i+1)+"</td>");
			
			writer.append("<td id='cell'>"+result.get(i+3)+"</td>");
			writer.append("<td id='cell'>"+result.get(i+4)+"</td>");
			writer.append("<td id='cell'>"+result.get(i+5)+"</td>");
			writer.append("</tr>");
		}

		writer.append("</table>");
		writer.append("	<input type='submit' value='save'/>  </form>  ");	 
		writer.append("review <br/>");
		writer.append("<a href='/app/reviewResolved'>Resolved</a><br/>");
			
		writer.append("<a href='/app/logout'>logout</a><br/>");
		
		// set response headers
		
		
		
			  
		writer.append("			<p>this is the view</p>\r\n")
	
			  .append("		</body>\r\n")
			  .append("</html>\r\n");
	}


	

	public void destroy(  ) {
		
	}

public void doPost(HttpServletRequest request, HttpServletResponse response)  
		throws ServletException, IOException {  
			handleRequest(request, response);
			
}

public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
 
	PrintWriter out = res.getWriter();
	res.setContentType("text/html");


	Cookie[] cookies = req.getCookies();
	  
		Cookie user=getCookie(req, "user");

	Enumeration<String> parameterNames = req.getParameterNames();

	while (parameterNames.hasMoreElements()) {

		String paramName = parameterNames.nextElement();
		//out.write(paramName);
		

		String[] paramValues = req.getParameterValues(paramName);
		for (int i = 0; i < paramValues.length; i++) {
			String paramValue = paramValues[i];
			//out.write(" is " + paramValue);
			//out.write(" : ");

			DataManager session = new DataManager();

				
			session.connect();
			session.updateStatus(paramName, paramValue, user.getValue());
		}
		out.write("work was saved. <a href='/app/review'>review</a><br/>");
	}

	out.close();

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