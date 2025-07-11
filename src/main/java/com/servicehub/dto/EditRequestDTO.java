package com.servicehub.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditRequestDTO
{
    private String category;
    private String description;
    private Double price;
    private String priority;
}
