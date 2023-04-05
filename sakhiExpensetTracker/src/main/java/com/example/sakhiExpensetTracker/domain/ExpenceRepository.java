package com.example.sakhiExpensetTracker.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface ExpenceRepository extends CrudRepository<Expence, Long> {
    List<Expence> findByAppuser(AppUser appuser);
}