package com.home.budget.repository;

import com.home.budget.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Optional<History> findById(Long id);
    List<History> findAll();
    <S extends History> S save(S history);
}
