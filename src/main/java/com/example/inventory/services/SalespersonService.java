package com.example.inventory.services;

import com.example.inventory.dto.SalespersonDTO;
import com.example.inventory.entity.Salesperson;
import com.example.inventory.repository.SalespersonRepository;
import com.example.inventory.transformer.SalespersonTransformer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalespersonService {
    private final SalespersonRepository repo;

    public SalespersonService(SalespersonRepository repo) {
        this.repo = repo;
    }

    public SalespersonDTO create(SalespersonDTO dto) {
        Salesperson saved = repo.save(SalespersonTransformer.toEntity(dto));
        return SalespersonTransformer.toDTO(saved);
    }

    public List<SalespersonDTO> getAll() {
        return repo.findAll().stream().map(SalespersonTransformer::toDTO).toList();
    }
}
