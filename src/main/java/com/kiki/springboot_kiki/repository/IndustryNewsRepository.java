package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.IndustryNews;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryNewsRepository extends CrudRepository<IndustryNews, Integer> {
} 