package com.example.curd.EmployeeController;

import com.example.curd.EmployeeService.EmployeeService;
import com.example.curd.model.Employee;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Save an Employee
    @PostMapping("/save")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employee != null ? new ResponseEntity<>(employee, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Generate Report (PDF / HTML)
    @GetMapping("/report/{format}")
    public ResponseEntity<String> generateReport(@PathVariable String format) {
        try {
            String reportPath = employeeService.exportReport(format);
            return ResponseEntity.ok("Report successfully generated: " + reportPath);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("JRXML file not found: " + e.getMessage());
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating report: " + e.getMessage());
        }
    }
}
