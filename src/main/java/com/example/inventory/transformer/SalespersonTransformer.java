package com.example.inventory.transformer;

import com.example.inventory.dto.SalespersonDTO;
import com.example.inventory.entity.Salesperson;

public class SalespersonTransformer {
    public static Salesperson toEntity(SalespersonDTO dto) {
        return new Salesperson(null, dto.getName(), dto.getPhone(), dto.getEmail());
    }

    public static SalespersonDTO toDTO(Salesperson entity) {
        SalespersonDTO dto = new SalespersonDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}
