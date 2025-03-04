package com.company.insight.org_structure_analyzer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.insight.org_structure_analyzer.model.Employee;
import com.company.insight.org_structure_analyzer.service.OrgStructureAnalyzer;
import com.company.insight.org_structure_analyzer.util.InputCSVReader;
import com.opencsv.exceptions.CsvValidationException;

@ExtendWith(MockitoExtension.class)
class OrgStructureAnalyzerApplicationTests {

    @Mock
    private InputCSVReader inputCSVReader;
    
    @InjectMocks
    private OrgStructureAnalyzer orgStructureAnalyzer;
    
    private Map<Integer, Employee> employees;
    
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    @BeforeEach
    void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        employees = new HashMap<>();
        
        Employee ceo = new Employee(123,"Joe","Doe", 60000, null);
        Employee manager1 = new Employee(124,"Martin","Chekov",45000,123);
        Employee emp1 = new Employee(125,"Bob","Ronstad",47000,123);
        Employee manager2 = new Employee(300,"Alice","Hasacat",50000,124);
        Employee manager3 = new Employee(305,"Brett","Hardleaf",34000,300);
        Employee manager4 = new Employee(306,"Brett-Senior","Hardleaf",35000,305);
        Employee emp2 = new Employee(307,"Brett-Junior","Hardleaf",35000,306);
        
        ceo.getSubordinates().add(manager1);
        ceo.getSubordinates().add(emp1);
        manager1.getSubordinates().add(manager2);
        manager2.getSubordinates().add(emp2);
        
        employees.put(123, ceo);
        employees.put(124, manager1);
        employees.put(125, emp1);
        employees.put(300, manager2);
        employees.put(305, manager3);
        employees.put(306, manager4);
        employees.put(307, emp2);
        
        // Inject the employees map using reflection
        Field employeesField = OrgStructureAnalyzer.class.getDeclaredField("employees");
        employeesField.setAccessible(true);
        employeesField.set(orgStructureAnalyzer, employees);
        
        // Redirect System.out to capture printed output
        System.setOut(new PrintStream(outputStreamCaptor));
        
    }
    
    @Test
    void testLoadEmployees() throws CsvValidationException, IOException {
        when(inputCSVReader.readEmployees(anyString())).thenReturn(employees);
        
        orgStructureAnalyzer.loadEmployees("dummy.csv");
        
        //verifies that read Employees is called just once to load data from CSV
        verify(inputCSVReader, times(1)).readEmployees("dummy.csv");
    }
    
    @Test
    void testAnalyzeSalaries() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        
        orgStructureAnalyzer.analyzeSalaries();
        
        
        // Get console output
        String consoleOutput = outputStreamCaptor.toString();

        //The test validates that messages are logged correctly in case managers with min salary at least 20% 
        //more than the average salary and max salary up to 50% of average salary condition is violated
        assertTrue(consoleOutput.contains("Manager Martin Chekov earns 15000.0 LESS than allowed!"),
                "Expected salary warning message not found in console output.");
    }
    
    @Test
    void testAnalyzeReportingLines() {
        orgStructureAnalyzer.analyzeReportingLines();
        
        // Get console output
        String consoleOutput = outputStreamCaptor.toString();

        //The test ensures correct detection of excessive reporting levels..Verify expected warning message
        assertTrue(consoleOutput.contains("Employee Brett-Junior Hardleaf has 5 levels above, which is too long!"),
                "Expected salary warning message not found in console output.");
    }
}

