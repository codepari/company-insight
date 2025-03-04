package com.company.insight.org_structure_analyzer;

import java.io.IOException;

import com.company.insight.org_structure_analyzer.service.OrgStructureAnalyzer;
import com.opencsv.exceptions.CsvValidationException;


/**
 * Analyze the organizational structure of a company.
 * 
 * Simple program which will read the file and report:
 * 	- which managers earn less than they should, and by how much
 *	- which managers earn more than they should, and by how much
 *  - which employees have a reporting line which is too long, and by how much
 */
public class OrgStructureAnalyzerApplication {

	
	public static void main(String[] args) {
		
		
		String filePath = "employees.csv";
		
		OrgStructureAnalyzer orgStructureAnalyzer = new OrgStructureAnalyzer();
		
		try {
            //1. Load the employees from CSV
			orgStructureAnalyzer.loadEmployees(filePath);
            
            //2. Run the analysis on employee salaries and levels
			orgStructureAnalyzer.analyzeSalaries();
			orgStructureAnalyzer.analyzeReportingLines();
			
        } catch (IOException e) {
        	System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (CsvValidationException e) {
        	System.out.println("CSV validation error occurred: " + e.getMessage());
        	e.printStackTrace();
		} catch (IllegalArgumentException e) {
        	System.err.println("Employee Duplicate found stopping the analysis: " + e.getMessage());
		}
        
	}

}
