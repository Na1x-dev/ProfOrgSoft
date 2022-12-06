package com.example.demo.services.application;

import com.example.demo.models.Application;
import com.example.demo.models.Position;

import java.util.List;

public interface ApplicationService {
    Application create(Application application);

    List<Application> readAll();

    boolean delete(Long id);

    boolean update(Long id, Application application);

    List<Application> readByUnionMemberSurname(String surname);
}
