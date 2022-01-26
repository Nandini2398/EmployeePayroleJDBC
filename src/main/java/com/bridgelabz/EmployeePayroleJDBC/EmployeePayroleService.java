package com.bridgelabz.EmployeePayroleJDBC;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeePayroleService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	public EmployeePayroleService() {
	}
	private List<EmployeePayroleData> employeePayroleList;
	private void readEmployeePayroleData(Scanner consoleInputReader) {
		System.out.println("Enter the Employee Id : ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter the Employee Name : ");
		String name = consoleInputReader.next();
		System.out.println("Enter the Employee Salary : ");
		double salary = consoleInputReader.nextDouble();
		employeePayroleList.add(new EmployeePayroleData(id, name, salary, LocalDate.now()));
	}
	public EmployeePayroleService(List<EmployeePayroleData> employeePayroleList) {
		this.employeePayroleList = employeePayroleList;
	}
	public void writeEmployeePayroleData(IOService ioService) {
		if(ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roster to Console\n" + employeePayroleList);
		else if(ioService.equals(IOService.FILE_IO))
			new EmployeePayroleFileIOService().writeData(employeePayroleList);
	}
	public void printData(IOService fileIo) {
		if(fileIo.equals(IOService.FILE_IO)) new EmployeePayroleFileIOService().printData();
	}
	public long countEntries(IOService fileIo) {
		if(fileIo.equals(IOService.FILE_IO)) 
			return new EmployeePayroleFileIOService().countEntries();
		return 0;
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
			this.employeePayroleList = new employeePayroleDBService().readData();
		return this.employeePayroleList;
	}
	public static void main(String[] args) {
		System.out.println(" Welcome To Employee Payroll Application \n");
		ArrayList<EmployeePayroleData> employeePayroleList  = new ArrayList<EmployeePayroleData>();
		EmployeePayroleService employeePayroleService = new EmployeePayroleService(employeePayroleList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayroleService.readEmployeePayroleData(consoleInputReader);
		employeePayroleService.writeEmployeePayroleData(IOService.CONSOLE_IO);		
	}
}