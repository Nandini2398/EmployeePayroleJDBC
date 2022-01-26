package com.bridgelabz.EmployeePayroleJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayroleDBService {
	private PreparedStatement employeePayroleDataStatement;
	private static EmployeePayroleDBService employeePayroleDBService;

	private EmployeePayroleDBService() {
		
	}
	
	public static EmployeePayroleDBService getInstance() {
		if(employeePayroleDBService == null)
			employeePayroleDBService = new EmployeePayroleDBService();
		return employeePayroleDBService;
	}
	
	private Connection getConnection() throws SQLException {
		
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_payrole?useSSL=false";
		String userName = "root";
		String password = "NANdini@23";
		Connection connection;
		
		System.out.println("Connecting to the database : "+jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is Succcessfully Established!! "+connection);
		
		return connection;
	}
	private List<EmployeePayroleData> getEmployeePayroleData(ResultSet resultSet) {
		
		List<EmployeePayroleData> employeePayroleList = new ArrayList<>();
		
		try {
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double basicSalary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayroleList.add(new EmployeePayroleData(id, name, basicSalary, startDate));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayroleList;
		
	}