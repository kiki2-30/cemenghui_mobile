package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.ConferenceCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceCategoryRepository extends CrudRepository<ConferenceCategory, Integer> {
} 