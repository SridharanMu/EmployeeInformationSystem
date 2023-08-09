package com.EIS.EmployeeInformationSystem.repository;

import com.EIS.EmployeeInformationSystem.model.Employee;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    public boolean existsById(int id);

    public List<Employee> findById(int id);

    public List<Employee> findByEmail(String email);

    public boolean existsByEmail(String email);

    @Query("select max(s.id) from Employee  s")
    public Integer findMaxId();

    @Query(value = "select * from Employee  s where s.first_Name like %:keyword%", nativeQuery = true)
    public List<Employee> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from Employee  s where s.employee_Id like %:employee_Id% or s.first_name like %:first_name% " +
            "or s.last_name like %:last_name% or s.date_of_birth like %:date_of_birth% or  s.date_of_joining like %:date_of_joining%" +
            " or s.grade like %:grade% ", nativeQuery = true)
    public List<Employee> findByEmployeesSearch(@Param("employee_Id") Long employee_Id,
                                                @Param("first_name") String first_name,
                                                @Param("last_name") String last_name,
                                                @Param("date_of_birth") String date_of_birth,
                                                @Param("date_of_joining") String date_of_joining,
                                                @Param("grade") String grade
    );
}


