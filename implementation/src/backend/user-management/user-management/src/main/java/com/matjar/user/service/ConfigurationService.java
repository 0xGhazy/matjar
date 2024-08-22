package com.matjar.user.service;

import com.matjar.user.model.Configuration;
import com.matjar.user.repository.ConfigurationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Log4j2
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository repository;

    @Cacheable("configurations")
    public List<Configuration> loadConfigurations() {
        List<Configuration> loadedConfigurations = repository.findAll();
        log.info("method=loadConfigurations, configurationRecordCount={}, message={}",
                 loadedConfigurations.size(), "configuration loaded successfully into configurations cache");
        return loadedConfigurations;
    }

    @CacheEvict(value = "configurations", allEntries = true)
    public void refreshCache() {
        log.info("method=refreshCache, message={}", "'configuration' cache refreshed successfully");
    }

}
