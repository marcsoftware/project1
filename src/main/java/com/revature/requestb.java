//this is the form for submitting reimbursement
package com.revature;

import com.revature.dao.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.fileupload.FileItem;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class requestb extends HttpServlet {

	private static final long serialVersionUID = -1641096228274971485L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
				
				
				
				
			
         
      // Get an array of Cookies associated with this domain
	  
	  
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
			  .append("			<form action=\"requestb\" method=\"POST\" enctype = 'multipart/form-data'>\r\n")
			  .append("				amount: \r\n")
			  
			  .append("				<input type=\"text\" name=\"amount\" />\r\n")
			  .append("				comment: \r\n")
			  .append("				<input type=\"text\" name=\"comment\" />\r\n")
			  .append("				picture: \r\n")
			  .append("				<input type=\"file\" name=\"picture\" />\r\n")
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
				
	  
				Cookie user=getCookie(request, "user");
				String rand ="";  
				// Create a factory for disk-based file items
DiskFileItemFactory factory = new DiskFileItemFactory();

// Configure a repository (to ensure a secure temp location is used)


factory.setRepository(new File("c:\\temp"));

// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload(factory);
String filePath = getServletContext().getInitParameter("file-upload"); 
// Parse the request
int i=0;
String[] bucket = new String[]{ "","","","","","" }; 
try{
				List<FileItem> items = upload.parseRequest(request);

				Iterator<FileItem> iter= items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();

					if (item.isFormField()) {
						String test = item.getFieldName();
						bucket[i] = item.getString();
						System.out.println(test+"-----------"+bucket[i]);
						i++;
					} else {
					
					// processUploadedFile(item);
					// Process a file upload
					rand =String.valueOf(new Random());  
						File uploadedFile = new File(filePath+(rand)+".png");
						item.write(uploadedFile);
					
					}
				}
			}catch(Exception e){}
				String amount = bucket[0];
				String comment = bucket[1];
				
				
				DataManager session = new DataManager();
		session.connect();
				session.addNewRequest(user.getValue(), amount, comment, rand);
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
