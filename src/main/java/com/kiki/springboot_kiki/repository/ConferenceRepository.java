package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.Conference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends CrudRepository<Conference, Integer> {
} 