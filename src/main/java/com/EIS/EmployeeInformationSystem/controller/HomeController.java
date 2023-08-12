package com.EIS.EmployeeInformationSystem.controller;


import com.EIS.EmployeeInformationSystem.model.Employee;
import com.EIS.EmployeeInformationSystem.model.User;
import com.EIS.EmployeeInformationSystem.service.EmployeeService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String home(Model model)
    {
        return "login";
    }
    @GetMapping("/logout")
    public String logout(Model model)
    {
        return "login";
    }


    @GetMapping("/loadData")
    public String loadData(Model model)
    {
        List<Employee> listEmployees= employeeService.getEmployee();
        model.addAttribute("listEmployees",listEmployees);
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User u , HttpSession session, Model model)
    {
        if(u.getUserName().equals("admin") && u.getPassword().equals("admin")) {
        List<Employee> listEmployees = employeeService.getEmployee();
        model.addAttribute("listEmployees", listEmployees);
        return "index";
        }
        else
         {
            return "redirect:/login";
         }
    }

    @GetMapping("/addEmployee")
    public String addEmployee()
    {
        return "addEmployee";
    }

    @GetMapping("/search")
    public String search()
    {
        return "search";
    }


    @RequestMapping(path = {"/searchbykey"})
    public String searchbykey(@ModelAttribute Employee e,Model model) {
        if ((employeeService.getEMpById(e.getEmployeeId())!=null) &&
                e.getFirstName() != null || e.getLastName()!=null || e.getEmployeeId()>0) {
            List<Employee> employee = employeeService.getByKeyword(e);
            model.addAttribute("listEmployees", employee);
            model.addAttribute("message", "search completed");
        } else {
            List<Employee> employee = employeeService.getEmployee();
            model.addAttribute("listEmployees", employee);
            model.addAttribute("message", "Please search no records found");
        }
        model.addAttribute("status", true);
        return "index";
    }



    @PostMapping("/save")
    public String save(@ModelAttribute Employee e , HttpSession session)
    {
      var result=employeeService.createEmployee(e);
      session.setAttribute("msg",result);
      return "redirect:/loadData";

    }

    @GetMapping("/Modify/{id}")
    public String edit(@PathVariable int id, Model m) {
        Employee e = employeeService.getEMpById(id);
        m.addAttribute("employee", e);
        return "Modify";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model m) {
        Employee e = employeeService.getEMpById(id);
        m.addAttribute("employee", e);
        return "view";
    }

    @PostMapping("/update")
    public String updateEmp(@ModelAttribute Employee e, HttpSession session) {
        employeeService.updateEmployeeById(e);
        session.setAttribute("msg", "Employee Data Update Sucessfully..");
        return "redirect:/loadData";
    }


    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {
        // call delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/loadData";
    }


    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response)
            throws Exception {

        // set file name and content type
        String filename = "Employee-List.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        // create a csv writer
        StatefulBeanToCsv<Employee> writer =
                new StatefulBeanToCsvBuilder<Employee>
                        (response.getWriter())
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(false).build();

        // write all employees to csv file
        writer.write(employeeService.getEmployee());

    }

    @PostMapping("/upload")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Object> csvToBean = new CsvToBeanBuilder<>(reader)
                        .withType(Employee.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<Object> employee = csvToBean.parse();
                // TODO: save users in DB?

                //employeeService.UploadEmployeeList(employee);

                for (int i = 0; i < employee.size(); i++) {

                    employeeService.createEmployee((Employee)employee.get(i));
                }

                // save users list on model
                model.addAttribute("listEmployees", employee);
                model.addAttribute("status", true);


            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "index";
    }
}
