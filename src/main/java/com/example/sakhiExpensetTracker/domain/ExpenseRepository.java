package com.example.sakhiExpensetTracker.domain;


import java.util.List;
import java.time.LocalDate;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByAppuser(AppUser appuser);
    


    List<Expense> findByAppuserAndDate(AppUser appuser, LocalDate date);

    List<Expense> findByAppuserAndCategory(AppUser appuser, Category category);

    List<Expense> findByAppuserAndDateAndCategory(AppUser appuser, LocalDate date, Category category);

    
    List<Expense> findByAppuserAndRemarkContainingIgnoreCase(AppUser appuser, String remark);
    List<Expense> findByAppuserAndDateAndRemarkContainingIgnoreCase(AppUser appuser, LocalDate date, String remark);
    List<Expense> findByAppuserAndCategoryAndRemarkContainingIgnoreCase(AppUser appuser, Category category, String remark);
    List<Expense> findByAppuserAndDateAndCategoryAndRemarkContainingIgnoreCase(AppUser appuser, LocalDate date, Category category, String remark);

	


}