package com.example.sakhiExpensetTracker.domain;


import java.util.List;
import java.time.LocalDate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sakhiExpensetTracker.domain.AppUser;

public interface ExpenceRepository extends JpaRepository<Expence, Long> {
    List<Expence> findByAppuser(AppUser appuser);
    


    List<Expence> findByAppuserAndDate(AppUser appuser, LocalDate date);

    List<Expence> findByAppuserAndCategory(AppUser appuser, Category category);

    List<Expence> findByAppuserAndDateAndCategory(AppUser appuser, LocalDate date, Category category);

    
    List<Expence> findByAppuserAndRemarkContainingIgnoreCase(AppUser appuser, String remark);
    List<Expence> findByAppuserAndDateAndRemarkContainingIgnoreCase(AppUser appuser, LocalDate date, String remark);
    List<Expence> findByAppuserAndCategoryAndRemarkContainingIgnoreCase(AppUser appuser, Category category, String remark);
    List<Expence> findByAppuserAndDateAndCategoryAndRemarkContainingIgnoreCase(AppUser appuser, LocalDate date, Category category, String remark);

	


}