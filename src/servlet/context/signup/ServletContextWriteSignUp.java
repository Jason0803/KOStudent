package servlet.context.signup;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.Student;

public class ServletContextWriteSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext cont;
	private Student user;
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
		
		System.out.println("Fetching Sign-Up data from form..");

		int userClass = Integer.parseInt(request.getParameter("userClass"));
		String userName = request.getParameter("usr").toLowerCase();
		String pw = request.getParameter("pwd");

		System.out.println(">>Fetching Successfully done !" );
		user = new Student(userClass, userName, pw);

		cont.setAttribute("user", user);
		rd = request.getRequestDispatcher("SC_checkSignUp");
		rd.forward(request, response);
	}

}

