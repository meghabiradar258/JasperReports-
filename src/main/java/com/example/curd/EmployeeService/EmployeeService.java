package com.example.curd.EmployeeService;

import com.example.curd.EmployeeRepository.EmployeeRepository;
import com.example.curd.model.Employee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final String REPORT_PATH = "C:\\Users\\Admin\\Desktop\\tdittask\\Report\\"; 
   

    @Autowired
    private EmployeeRepository employeeRepository;

    // Save Employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Get Employee by ID
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    // Export Jasper Report
    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        List<Employee> employees = employeeRepository.findAll();

        File file = ResourceUtils.getFile("classpath:emp.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Data Source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Spring Boot");

        // Fill Report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export Report
        if ("html".equalsIgnoreCase(reportFormat)) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, REPORT_PATH + "employee_report.html");
        } else if ("pdf".equalsIgnoreCase(reportFormat)) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, REPORT_PATH + "employee_report.pdf");

            
        } else {
            return "Invalid report format. Use 'pdf' or 'html'.";
        }

        return "Report generated at: " + REPORT_PATH;
    }
}
