package com.company.insight.org_structure_analyzer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.company.insight.org_structure_analyzer.model.Employee;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

/**
 * Read Data from CSV file
 */
public class InputCSVReader {

	public Map<Integer, Employee> readEmployees(String filePath) throws IOException, CsvValidationException {
		
		Map<Integer, Employee> employees = new HashMap<>();
		Set<Integer> duplicateCheck = new HashSet<>();
		
		ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);
        
        try (InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                CSVReader csvReader = new CSVReader(br)) {

			csvReader.readNext(); 
			String[] line;
			
			while ((line = csvReader.readNext()) != null) {
				
				int empId = Integer.parseInt(line[0].trim());

                // Check for duplicates
                if (duplicateCheck.contains(empId)) {
                    //System.err.println("Duplicate Employee ID detected: " + empId);
                    throw new IllegalArgumentException("Duplicate Employee ID detected: " + empId);
                }
                duplicateCheck.add(empId);
                
				employees.put(empId, new Employee(Integer.parseInt(line[0].trim()), 
												line[1], 
												line[2], 
												Double.parseDouble(line[3].trim()),
												line[4].isEmpty() ? null : Integer.parseInt(line[4].trim())));
			}
		}
		
		// Get the subordinates
		for (Employee emp : employees.values()) {
            if (emp.getManagerId() != null) {
                employees.get(emp.getManagerId()).getSubordinates().add(emp);
            }
        }
		
		return employees;
	}

}
