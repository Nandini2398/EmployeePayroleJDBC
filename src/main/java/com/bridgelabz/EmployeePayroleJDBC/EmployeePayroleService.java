package com.bridgelabz.EmployeePayroleJDBC;

import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;

public class EmployeePayroleService {
	
	private List<EmployeePayroleData> employeePayroleList;
	private EmployeePayrollDBService employeePayroleDBService;

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	public EmployeePayroleService() {
		employeePayroleDBService =  EmployeePayroleDBService.getInstance();
	}
	public EmployeePayroleService(List<EmployeePayroleData> employeePayroleList) {
		this();
		this.employeePayroleList = employeePayroleList;
	}
	public void printData(IOService fileIo) {
		if(fileIo.equals(IOService.FILE_IO)) new EmployeePayroleFileIOService().printData();
	}
	public long countEntries(IOService fileIo) {
		if(fileIo.equals(IOService.FILE_IO)) 
			return new EmployeePayroleFileIOService().countEntries();
		return 0;
	}
	private void readEmployeePayroleData(Scanner consoleInputReader) {
		
		System.out.println("Enter the Employee Id : ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter the Employee Name : ");
		String name = consoleInputReader.next();
		System.out.println("Enter the Employee Salary : ");
		double salary = consoleInputReader.nextDouble();
		employeePayroleList.add(new EmployeePayroleData(id, name, salary));
	}
	private EmployeePayroleData getEmployeePayroleData(String name) {
		return this.employeePayroleList.stream()
				.filter(EmployeePayroleDataItem -> EmployeePayroleDataItem.employeeName.equals(name))
				.findFirst()
				.orElse(null);
	}
	public long readDataFromFile(IOService fileIo) {
		List<String> employeePayroleFromFile = new ArrayList<String>();
		if(fileIo.equals(IOService.FILE_IO)) {
			System.out.println("Employee Details from payroll-file.txt");
			employeePayroleFromFile = new EmployeePayroleFileIOService().readDataFromFile();
		}
		return employeePayroleFromFile.size();
	}
	public List<EmployeePayroleData> readEmployeePayroleData(IOService ioService) {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayroleList = employeePayroleDBService.readData();
		return this.employeePayroleList;
	}	
	public void writeEmployeePayroleData(IOService ioService) {
		if(ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roster to Console\n" + employeePayroleList);
		else if(ioService.equals(IOService.FILE_IO))
			new EmployeePayroleFileIOService().writeData(employeePayroleList);
	}	
	public void updateEmployeeSalary(String name, double salary) {
		int result = employeePayroleDBService.updateEmployeeData(name,salary);
		if(result == 0) 
			return;
		EmployeePayroleData employeePayroleData = this.getEmployeePayroleData(name);
		if(employeePayroleData != null)
			employeePayroleData.employeeSalary = salary;
	}
	public boolean checkEmployeePayroleInSyncWithDB(String name) {
		List<EmployeePayroleData> employeePayroleDataList = employeePayroleDBService.getEmployeePayroleData(name);
		return employeePayroleDataList.get(0).equals(getEmployeePayroleData(name));
	}
	public List<EmployeePayroleData> getEmployeeDetailsBasedOnStartDate(IOService ioService, String startDate) {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayroleList = employeePayroleDBService.getEmployeeDetailsBasedOnStartDateUsingStatement(startDate);
		return this.employeePayroleList;
	}
	public List<EmployeePayroleData> getEmployeeDetailsBasedOnStartDateUsingPreparedStatement(IOService ioService, String startDate) {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayroleList = employeePayroleDBService.getEmployeeDetailsBasedOnStartDateUsingPreparedStatement(startDate);
		return this.employeePayroleList;
	}
	public List<EmployeePayroleData> getEmployeeDetailsBasedOnName(IOService ioService, String name) {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayroleList = employeePayroleDBService.getEmployeeDetailsBasedOnNameUsingStatement(name);
		return this.employeePayroleList;
	}
	public static void main(String[] args) {
		System.out.println(" Welcome To Employee Payrole Application\n");
		ArrayList<EmployeePayroleData> employeePayrollList  = new ArrayList<EmployeePayroleData>();
		EmployeePayroleService employeePayroleService = new EmployeePayroleService(employeePayroleList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayroleService.readEmployeePayroleData(consoleInputReader);
		employeePayroleService.writeEmployeePayroleData(IOService.CONSOLE_IO);		
	}
}