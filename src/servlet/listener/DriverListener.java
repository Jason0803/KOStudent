package servlet.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dao.StudentDAO;
import vo.Student;

public class DriverListener implements ServletContextListener {
	private ServletContext cont;
	private Student student;
	private ArrayList<Student> studentDB;
	private Connection conn;
	PreparedStatement ps;
	ResultSet rs;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		cont = arg0.getServletContext();
		studentDB = new ArrayList<Student>();
		
		try {
			Class.forName(cont.getInitParameter("ODriver"));
			System.out.println("Driver Successfully Loaded !");
			
			String dbUrl = cont.getInitParameter("ODriverURL");
			String dbUser = cont.getInitParameter("ODriverUserName");
			String dbPassword = cont.getInitParameter("ODriverPassword");
			
			System.out.println("Connecting..");
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			System.out.println("Driver Listener Connection Success !");
			
			ps = conn.prepareStatement("SELECT * FROM student");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				student = new Student(rs.getInt("classno"), rs.getString("sid"), rs.getString("password") );
				studentDB.add(student);
			}
			
			rs.close();
			ps.close();
			
			System.out.println("All Closed Successfully !");
			
			cont.setAttribute("studentDB", studentDB);
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Not Found !");
		} catch (SQLException e) {
			System.out.println("Connection Closed !! ");
		}
	}
	
}
