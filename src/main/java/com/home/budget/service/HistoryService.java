package com.home.budget.service;

import com.home.budget.model.History;

import java.util.List;
import java.util.Optional;

public interface HistoryService {

    Optional<History> findById(Long id);
    List<History> getAll();
    History add(History history);
}
