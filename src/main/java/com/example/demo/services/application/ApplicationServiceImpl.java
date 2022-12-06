package com.example.demo.services.application;

import com.example.demo.models.Application;
import com.example.demo.repositories.application.ApplicationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    ApplicationJpaRepository applicationJpaRepository;

    @Override
    public Application create(Application application) {
        return applicationJpaRepository.save(application);
    }

    @Override
    public List<Application> readAll() {
        return applicationJpaRepository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        if (applicationJpaRepository.existsById(id)) {
            applicationJpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Long id, Application application) {
        if (applicationJpaRepository.existsById(id)) {
            application.setApplicationId(id);
            applicationJpaRepository.save(application);
            return true;
        }
        return false;
    }

    @Override
    public List<Application> readByUnionMemberSurname(String surname) {
        return applicationJpaRepository.getByUnionMemberSurname(surname);
    }

}
