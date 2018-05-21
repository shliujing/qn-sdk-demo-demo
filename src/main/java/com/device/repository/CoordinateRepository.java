package com.device.repository;

import com.device.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Coordinate findById(long id);

    long deleteById(Long id);

    List<Coordinate> findAllByTId(long tId);
}