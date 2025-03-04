package com.company.insight.org_structure_analyzer.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.company.insight.org_structure_analyzer.model.Employee;
import com.company.insight.org_structure_analyzer.util.InputCSVReader;
import com.opencsv.exceptions.CsvValidationException;

/**
 * Analysis for
 - managers with min salary at least 20% more than the average salary and max salary up to 50% of average salary
 - Identify all employees which have more than 4 managers between them and the CEO.
 */
public class OrgStructureAnalyzer {

	private Map<Integer, Employee> employees = new HashMap<>();

	private InputCSVReader inputCSVReader = new InputCSVReader();

	public void loadEmployees(String filePath) throws CsvValidationException, IOException {
		employees = inputCSVReader.readEmployees(filePath);
	}

	public void analyzeSalaries() {
		
		System.out.println("Salary Analysis starts !!!");
		
		for(Employee manager: employees.values()) {
			if(!manager.getSubordinates().isEmpty()) {
				double avgSubordinateSalary = manager
												.getSubordinates()
												.stream()
												.mapToDouble(emp -> emp.getSalary())
												.average()
												.orElse(0);
				double minAllowedSalary = avgSubordinateSalary * 1.2;
				double maxAllowedSalary = avgSubordinateSalary * 1.5;
				
				if (manager.getSalary() < minAllowedSalary) {
					System.out.println("Manager " + manager.getFirstName() + " " + manager.getLastName() +  
			                   " earns " + (minAllowedSalary - manager.getSalary()) + " LESS than allowed!");

				    
				} else if (manager.getSalary() > maxAllowedSalary) {
				    System.out.println("Manager " + manager.getFirstName() + " " + manager.getLastName() +  
			                   " earns " + (manager.getSalary() - maxAllowedSalary) + " MORE than allowed!");

				}
			}
		}
		
		System.out.println("Salary Analysis ends !!!");
		
	}
	
	public void analyzeReportingLines() {
		
		System.out.println("Reporting Line Analysis starts !!!");
		
        for (Employee emp : employees.values()) {
            int levels = getReportingLevels(emp);
            if (levels > 4) {
            	System.out.println("Employee " + emp.getFirstName() + " " + emp.getLastName() + 
                        " has " + levels + " levels above, which is too long!");
            }
        }
        
        System.out.println("Reporting Line Analysis ends !!!");
    }

    private int getReportingLevels(Employee emp) {
        int levels = 0;
        while (emp.getManagerId() != null) {
            levels++;
            emp = employees.get(emp.getManagerId());
        }
        return levels;
    }
	

}
