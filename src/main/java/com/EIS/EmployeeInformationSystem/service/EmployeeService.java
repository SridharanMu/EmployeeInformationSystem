package com.EIS.EmployeeInformationSystem.service;

import com.EIS.EmployeeInformationSystem.model.Employee;
import com.EIS.EmployeeInformationSystem.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getEmployee() {
        return employeeRepository.findAll();
    }

    @Transactional
    public String createEmployee(Employee emp) {
        try {

            if (!employeeRepository.existsByEmail(emp.getEmail())) {
                //No need to set id value.In memeory table its got  create already while debugging once all the task are completed later change this funcionality
                emp.setId(null == employeeRepository.findMaxId() ?  1: employeeRepository.findMaxId() + 1);
                emp.setEmployeeId(emp.getId());
                employeeRepository.save(emp);

                return "Employee record created successfully. ";
            } else {
                return "Employee already exists in the database.";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public String UploadEmployeeList(List<Employee> emplist) {
        employeeRepository.saveAll(emplist);
        return "done";
    }

    @Transactional
    public Employee getEMpById(long id) {
        Optional<Employee> e = employeeRepository.findById(id);
        if (e.isPresent()) {
            return e.get();
        }
        return null;
    }

    @Transactional
    public String updateEmployeeById(Employee emp) {
        //if (employeeRepository.existsByEmail(emp.getEmail())) {
        try {
            List<Employee> employee = employeeRepository.findByEmail(emp.getEmail());
            employee.stream().forEach(s -> {
                Employee employeeToBeUpdate = employeeRepository.findById(s.getId()).get();
                employeeToBeUpdate.setFirstName(emp.getFirstName());
                employeeToBeUpdate.setLastName(emp.getLastName());
                employeeToBeUpdate.setEmail(emp.getEmail());
                employeeToBeUpdate.setDateOfBirth(emp.getDateOfBirth());
                employeeToBeUpdate.setDateOfJoining(emp.getDateOfJoining());
                employeeToBeUpdate.setGrade(emp.getGrade());
                employeeRepository.save(employeeToBeUpdate);
            });
            return "Employee record updated.";
        }
        catch (Exception e) {
            throw e;
        }
//        } else {
//            return "Employee does not exists in the database.";
//        }
    }

    @Transactional
    public void deleteEmployeeById(long id) {
        this.employeeRepository.deleteById(id);
    }

    public List<Employee> getByKeyword(Employee emp){
        return employeeRepository.findByEmployeesSearch(emp.getFirstName(), emp.getLastName(), emp.getDateOfBirth(),
                emp.getDateOfJoining(), emp.getGrade());
    }

}
