package com.matjar.user.repository;

import com.matjar.user.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
    Configuration findByName(String name);

}
