package servlet.context.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.Student;

/**
 * Servlet implementation class ServletContextWriting for Log-In
 */
public class ServletContextWriting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Student user;
	private ServletContext cont;
	private RequestDispatcher rd;
	
	public void init() throws ServletException {
		cont = getServletContext();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		int userClass = Integer.parseInt(request.getParameter("userClass"));
		String userName = request.getParameter("usr");
		String pw = request.getParameter("pwd");
		
		user = new Student(userClass, userName, pw);

		cont.setAttribute("user", user);
		
		rd = request.getRequestDispatcher("SC_checkLogin");
		rd.forward(request, response);
	}

}
