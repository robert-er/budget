package com.home.budget.service.impl;

import com.home.budget.model.History;
import com.home.budget.repository.HistoryRepository;
import com.home.budget.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository repository;

    @Override
    public Optional<History> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<History> getAll() {
        return repository.findAll();
    }

    @Override
    public History add(History history) {
        return repository.save(history);
    }
}
