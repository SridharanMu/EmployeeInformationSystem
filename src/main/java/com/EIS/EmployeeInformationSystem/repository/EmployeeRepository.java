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


    @Query(value = "select * from Employee  s where s.first_name = :keyword " +
            "or s.last_name =:keyword or s.date_of_birth =:keyword or  s.date_of_joining = :keyword " +
            " or s.grade =:keyword ", nativeQuery = true)
    public List<Employee> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from Employee  s where s.first_name = :firstName " +
            "or s.last_name =:lastName or s.date_of_birth =:dob or  s.date_of_joining = :doj " +
            " or s.grade =:grade ", nativeQuery = true)
    public List<Employee> findByEmployeesSearch(@Param("firstName") String firstName,
                                                @Param("lastName") String lastName,
                                                @Param("dob") String dob,
                                                @Param("doj") String doj,
                                                @Param("grade") String grade
    );
}


