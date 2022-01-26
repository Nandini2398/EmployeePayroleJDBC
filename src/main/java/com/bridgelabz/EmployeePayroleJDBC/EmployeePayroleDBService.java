package com.bridgelabz.EmployeePayroleJDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.xdevapi.Statement;

public class EmployeePayroleDBService {
	
	private Connection getConnection() throws SQLException {
		
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_payrole?useSSL=false";
		String userName = "root";
		String password = "Nandini@23";
		Connection connection;
		
		System.out.println("Connecting to the database : "+jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is Succcessfully Established!! "+connection);
		
		return connection;
	}
	
	public List<EmployeePayroleData> readData(){
		
		String sqlStatement = "SELECT emp_id, emp_name, basic_pay, start FROM employee JOIN payrole ON employee.payrolel_id = payrole.payrole_id;";
		List<EmployeePayroleData> employeePayroleList = new ArrayList<>();
				
		try (Connection connection = getConnection();){
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			
			while(resultSet.next()) {
				int id = resultSet.getInt("emp_id");
				String name = resultSet.getString("emp_name");
				double basicSalary = resultSet.getDouble("basic_pay");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayroleList.add(new EmployeePayroleData(id, name, basicSalary, startDate));
			}
		}
		catch(SQLException e){
			
			e.printStackTrace();
		}
		return employeePayroleList;
	}
}