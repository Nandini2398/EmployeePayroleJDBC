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
	public List<EmployeePayroleData> getEmployeePayroleData(String name) {
		
		List<EmployeePayroleData> employeePayroleList = null;
		if(this.employeePayroleDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayroleDataStatement.setString(1,name);
			ResultSet resultSet = employeePayroleDataStatement.executeQuery();
			employeePayroleList = this.getEmployeePayroleData(resultSet);	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayroleList;
	}
	public List<EmployeePayroleData> readData(){
		
		String sqlStatement = "SELECT emp_id, emp_name, basic_pay, start FROM employee JOIN payroll ON employee.payrole_id = payrole.payrole_id;";
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
	public int updateEmployeeData(String name, double salary) {
		
		return this.updateEmployeeDataUsingStatement(name,salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) {
		
		String sqlStatement = String.format("UPDATE payrole ,employee SET net_pay = %2f WHERE employee.payrole_id = payrole.payrole_id AND emp_name = '%s';", salary, name);
		
		try (Connection connection = getConnection()){
			java.sql.Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlStatement);
		}
		catch(SQLException e){
			e.printStackTrace();
		}		
		return 0;
	}
	private void prepareStatementForEmployeeData() {
		
		try {
			Connection connection = this.getConnection();
			String sqlStatement = "SELECT * FROM employee,payrole WHERE employee.payrole_id = payrole.payrole_id AND name = ?;";
			employeePayroleDataStatement = connection.prepareStatement(sqlStatement);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
    private void preparedStatementForEmployeeDataBasedOnStartDate() {
		
		try {
			Connection connection = this.getConnection();
			String sqlStatement = "SELECT * FROM employee,payrole WHERE employee.payrole_id = payrole.payrole_id and start BETWEEN CAST(? AS DATE) AND DATE(NOW());";
			employeePayroleDataStatement = connection.prepareStatement(sqlStatement);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
    public List<EmployeePayroleData> getEmployeeDetailsBasedOnNameUsingStatement(String name) {
		
		String sqlStatement = String.format("SELECT * FROM employee,payrole WHERE employee.payrole_id = payroll.payrole_id and name = '%s';",name);
		List<EmployeePayroleData> employeePayrollList = new ArrayList<>();
				
		try (Connection connection = getConnection();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			employeePayroleList = this.getEmployeePayroleData(resultSet);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return employeePayroleList;
		
	}	
    public List<EmployeePayroleData> getEmployeeDetailsBasedOnStartDateUsingPreparedStatement(String startDate) {
		
		List<EmployeePayroleData> employeePayrollList = null;
		if(this.employeePayroleDataStatement == null)
			this.preparedStatementForEmployeeDataBasedOnStartDate();
		try {
			employeePayroleDataStatement.setString(1,startDate);
			ResultSet resultSet = employeePayroleDataStatement.executeQuery();
			employeePayroleList = this.getEmployeePayroleData(resultSet);	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayroleList;
	}
    public List<EmployeePayroleData> getEmployeeDetailsBasedOnStartDateUsingStatement(String startDate) {
		String sqlStatement = String.format("SELECT * FROM employee,payrole WHERE employee.payrole_id = payrole.payrole_id and start BETWEEN CAST('%s' AS DATE) AND DATE(NOW());",startDate);
		List<EmployeePayroleData> employeePayrollList = new ArrayList<>();
		try (Connection connection = getConnection();){
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			employeePayroleList = this.getEmployeePayroleData(resultSet);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return employeePayroleList;
	}
}
