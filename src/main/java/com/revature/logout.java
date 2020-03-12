package com.revature;


import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  







public class logout extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
				
				
				
			
				Cookie test = new Cookie("user", "");
				test.setMaxAge(60*60*24);
				response.addCookie( test );
				test = new Cookie("password", "");
				test.setMaxAge(60*60*24);
				response.addCookie( test );

				// set response headers
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// create HTML form
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  
			  .append("			<title>logout</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n")
			  
			  .append("			<p>you are logged out.</p><br/>")
		
			  .append("<a href='/app/login'>login</a><br/>")
			  .append("<a href='/app/register'>register</a><br/>")
	
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
