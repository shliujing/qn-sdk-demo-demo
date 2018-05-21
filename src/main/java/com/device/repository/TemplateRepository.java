package com.device.repository;

import com.device.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Template findById(long id);

    Long deleteById(Long id);
}