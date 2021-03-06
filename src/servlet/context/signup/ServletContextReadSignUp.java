package servlet.context.signup;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

import dao.StudentDAO;
import jdbc.exception.DuplicateIdException;
import vo.Student;


public class ServletContextReadSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext cont;
	private Student user;
	private ArrayList<Student> users;
	private StudentDAO dao;
	private boolean isSuccesSignUp = false;

	
	public void init() throws ServletException {
		cont = getServletContext();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		user = (Student)cont.getAttribute("user");
		users = (ArrayList<Student>)cont.getAttribute("studentDB");
		
		for(int i = 0; i < users.size(); i++) {
			if(user.getName().trim().equals(null)) {
				// when incorrect format entered on userID
				user.setMatch(Student.NOT_VALID_ID_FORMAT);
				break;
			} else if(user.getPassword().trim().equals(null)) {
				// when incorrect format entered on password
				user.setMatch(Student.NOT_VALID_PASSWORD_FORMAT);
				break;
			} else if(user.getName().equals(users.get(i).getName())) {
				// when user ID exists in DB
				user.setMatch(Student.DUPLICATED_USER_ID);
				break;
			} else {
				// Valid Sign-up information
				user.setMatch(Student.VALUD_SIGN_UP);
			} 
		}
		
		PrintWriter out = response.getWriter();
		out.println("<html><body bgcolor=yellow>");
		
		switch(user.getMatching()) {
			case Student.NOT_VALID_ID_FORMAT : {
				out.print("<h1><font color='red'>INCORRECT ID FORMAT !</font></h1>");
				break;
			}
			case Student.NOT_VALID_PASSWORD_FORMAT : {
				out.print("<h1><font color='red'>INCORRECT PASSWORD FORMAT !</font></h1>");
				break;
			}
			case Student.DUPLICATED_USER_ID : {
				out.print("<h1><font color='red'>EXISTING USER !</font></h1>");
				break;
			}
			case Student.VALUD_SIGN_UP :{
				// Store the user data into the database
				try {
					dao = StudentDAO.getInstance();
					// Save to DB
					dao.addMember(user);
					
					// Save to Context
					users.add(user);
					cont.setAttribute("studentDB", users);
					
					out.println("<h1><font color='red'>Sign-UP Success for user : " + user.getName() + "</font></h1>");
					isSuccesSignUp = true;
					break;
				} catch (SQLException e) {
					System.out.println("WARNING : Connection Closed for a reason !");
				} catch (DuplicateIdException e) {
					System.out.println(e.getMessage());
				}
			}
		}// switch

		System.out.println("== Sign-Up Trial ===" + " "
				+ "\nSucess ? :" + isSuccesSignUp
				+ "\n-ID : " + user.getName() + "\n-Password : " + user.getPassword() + "\n-Class : " + user.getUserClass());
		
		out.println("<br><br><a href='index.html'>Go Back</a>");
		out.println("</body></html>");
			
	}

}
