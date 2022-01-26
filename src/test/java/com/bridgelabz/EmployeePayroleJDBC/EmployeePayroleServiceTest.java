package com.bridgelabz.EmployeePayroleJDBC;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.bridgelabz.employeepayrolejdbc.EmployeePayroleService.IOService;


public class EmployeePayroleServiceTest 
{
	@Test
	public void given3Employees_WhenWrittenToFile_ShouldMatchEmployeeEntries()
	{
		EmployeePayroleData[] arrayOfEmployees = {
				new EmployeePayroleData(1, "Jeff Bezos", 100000.0, LocalDate.now()),
				new EmployeePayroleData(2, "Bill Gates", 200000.0, LocalDate.now()),
				new EmployeePayroleData(3, "Mark Zuckerberg", 300000.0, LocalDate.now())
		};
		EmployeePayroleService employeePayroleService;
		employeePayroleService = new EmployeePayroleService(Arrays.asList(arrayOfEmployees));
		employeePayroleService.writeEmployeePayrollData(IOService.FILE_IO);
		
		employeePayroleService.printData(IOService.FILE_IO);
		long entries = employeePayroleService.countEntries(IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}
	@Test
	public void givenFile_WhenRead_ShouldReturnNumberOfEntries() {
		EmployeePayroleService employeePayroleService = new EmployeePayroleService();
		long entries = employeePayroleService.readDataFromFile(IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}
	@Test
	public void givenEmployeePayroleInDB_WhenRetrieved_ShouldMatchEmployeeCount(){
		EmployeePayroleService employeePayroleService = new EmployeePayroleService();
		List<EmployeePayroleData> employeePayroleData = employeePayroleService.readEmployeePayroleData(IOService.DB_IO);
		System.out.println(employeePayroleData.size());
		Assert.assertEquals(4, employeePayroleData.size());
	}
	@Test 
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
		EmployeePayroleService employeePayroleService = new EmployeePayroleService();
		List<EmployeePayroleData> employeePayrollData = employeePayroleService.readEmployeePayroleData(IOService.DB_IO);
		employeePayroleService.updateEmployeeSalary("Bill", 7000000.00);
		boolean result = employeePayroleService.checkEmployeePayroleInSyncWithDB("Bill");
		Assert.assertTrue(result);
	}
	@Test
	public void givenName_WhenFound_ShouldReturnEmployeeDetails() {
		EmployeePayroleService employeePayrollService = new EmployeePayroleService();
		String name = "Rosa Diaz";
		List<EmployeePayroleData> employeePayroleData = employeePayroleService.getEmployeeDetailsBasedOnName(IOService.DB_IO, name);
		String resultName = employeePayroleData.get(0).employeeName;
		Assert.assertEquals(name, resultName);
	}
	@Test
	public void givenStartDateRange_WhenMatches_ShouldReturnEmployeeDetails() {
		String startDate = "2013-01-01";
		EmployeePayroleService employeePayroleService = new EmployeePayroleService();
		List<EmployeePayroleData> employeePayroleData = employeePayroleService.getEmployeeDetailsBasedOnStartDate(IOService.DB_IO, startDate);
		System.out.println(employeePayroleData.size());
		Assert.assertEquals(1, employeePayroleData.size());
	}
}
