package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jdbc.exception.DuplicateIdException;
import jdbc.exception.RecordNotFoundException;

import vo.Student;

public class StudentDAO {

	// ---------------------------------- for singleton  ----------------------------------//
	private static StudentDAO dao = new StudentDAO();
	private DataSource ds;
	private StudentDAO() {
		try {
			InitialContext ic = new InitialContext();
			ds = (DataSource)ic.lookup("java:comp/env/jdbc/oracleDB");
			System.out.println("DataSource..LookUp Success !");
			
		} catch (NamingException e) {
			System.out.println("DataSource..LookUp Fail :( ");
			e.printStackTrace();
		}
	}
	public static StudentDAO getInstance() {
		return dao;
	}
	
	// ---------------------------------- for connection ----------------------------------//
	public Connection connection() {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			System.out.println("Hello, DB Connected w/ DataSource ");
		} catch (SQLException e) {
			System.out.println("DB Disconnected !");
		}
	
		return conn;
	}
	// ---------------------------------- for close ----------------------------------//
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		ps.close();
		conn.close();
	}
	public void closeAll(PreparedStatement ps, Connection conn, ResultSet rs) throws SQLException {
		rs.close();
		closeAll(ps, conn);
	}

	// ---------------------------------- for SELECT  ----------------------------------//
	public ArrayList<Student> getAllStudent() throws SQLException{
		ArrayList<Student> result = new ArrayList<Student>();
		Connection conn = connection();
		PreparedStatement ps = 
				conn.prepareStatement("SELECT * FROM student");
			
		ResultSet rs = ps.executeQuery();
			
		while(rs.next()) {
			result.add(new Student(rs.getInt("classno"), 
									rs.getString("sid"), 
									rs.getString("password")));
		}
				
		return result;
	}
	
	// ---------------------------------- for INSERT ---------------------------------- //
	public void addMember(Student student) throws SQLException, DuplicateIdException {
		Connection conn = null;
		PreparedStatement ps = null;
	
		try {
			conn = connection();
				
			if( !doesExist(student.getName(), conn) ){
				conn = connection();
				System.out.println("connection");
					
				ps = conn.prepareStatement("INSERT INTO student VALUES(?,?,?)");
				//System.out.println("ps");
				ps.setString(1, student.getName());
				ps.setInt(2, student.getUserClass());
				ps.setString(3, student.getPassword());
					
				int row = ps.executeUpdate();
				System.out.println("Sucess ? :" + row);
			} else  {
				throw new DuplicateIdException("Already Existing ID !");
			}
		} finally {
				
			System.out.println("Adding student done");
			try{
				closeAll(ps, conn);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}		
	}
		
	// ---------------------------------- for Search ---------------------------------- //
	public boolean doesExist(String id, Connection conn) throws SQLException {
		PreparedStatement ps = 
				conn.prepareStatement("SELECT sid FROM student WHERE sid = ?");
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();

		return rs.next();
	}
	// ---------------------------------- for Update ---------------------------------- //
	public boolean updateUser(Student student) throws RecordNotFoundException {
		boolean result = false;
		Connection conn = null;
		
		conn = connection();
		try {
			if(doesExist(student.getName(), conn)) {
				PreparedStatement ps = 
						conn.prepareStatement("UPDATE student"
								+ " SET sid = ?, classno = ?, password = ?"
								+ " WHERE sid = ?");
	
				ps.setString(1, student.getName());
				ps.setInt(2, student.getUserClass());
				ps.setString(3, student.getPassword());
				ps.setString(4, student.getName());
				
				ps.executeUpdate();
				System.out.println("[DAO - UPDATE] User Information Updated !!");
				result = true;
				closeAll(ps, conn);
				System.out.println("[DAO - UPDATE] PS & RS Successfully closed !");
				return result;
			} else throw new RecordNotFoundException("[DAO - UPDATE] WARNING : No Such User Found !");
		} catch (SQLException e) {
			System.out.println("[DAO - UPDATE] WARNING : SQLException !");
			e.printStackTrace();

		} 
		return result;
	}
}
