package com.example.demo.services.position;

import com.example.demo.models.Position;
import com.example.demo.repositories.position.PositionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    PositionJpaRepository positionJpaRepository;

    @Override
    public Position create(Position position) {
        positionJpaRepository.save(position);
        return position;
    }

    @Override
    public List<Position> readAll() {
        return positionJpaRepository.findAll();
    }

    @Override
    public Position readById(Long positionId) {
        return positionJpaRepository.getByPositionId(positionId);
    }
}
