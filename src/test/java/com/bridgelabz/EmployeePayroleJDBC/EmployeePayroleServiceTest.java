package com.bridgelabz.EmployeePayroleJDBC;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.bridgelabz.EmployeePayroleJDBC.EmployeePayroleService.IOService;

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
		employeePayroleService.writeEmployeePayroleData(IOService.FILE_IO);
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
}