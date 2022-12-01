package com.example.demo.repositories.position;

import com.example.demo.models.Application;
import com.example.demo.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionJpaRepository extends JpaRepository<Position, Long> {
    Position getByPositionId(Long positionId);

    Position getByPositionTitle(String title);

    Position readByPositionTitle(String title);
}