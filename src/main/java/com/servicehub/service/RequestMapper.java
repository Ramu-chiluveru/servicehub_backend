package com.servicehub.service;

import com.servicehub.dto.ProposalDTO;
import com.servicehub.dto.RequestDTO;
import com.servicehub.model.Proposal;
import com.servicehub.model.Requests;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public RequestDTO toRequestDTO(Requests request)
    {
        return new RequestDTO(
                request.getId(),
                request.getCategory(),
                request.getDescription(),
                request.getPrice(),
                request.getStatus().toString(),
                request.getCreatedAt(),
                request.getCompletedAt(),
                request.getPriority(),
                toProposalDTOs(request.getProposals())
        );
    }

    private List<ProposalDTO> toProposalDTOs(List<Proposal> proposals) {
        return proposals.stream()
                .map(this::toProposalDTO)
                .collect(Collectors.toList());
    }

    private ProposalDTO toProposalDTO(Proposal proposal) {
        ProposalDTO dto = new ProposalDTO();
        dto.setId(proposal.getId());
        dto.setMessage(proposal.getMessage());
        dto.setProposedPrice(proposal.getProposedPrice());
        dto.setProviderName(proposal.getProvider().getFirstName());
        dto.setProviderRating(proposal.getProviderRating());
        dto.setSubmittedAt(proposal.getSubmittedAt());
        dto.setProviderId(proposal.getProvider().getId());
        return dto;
    }
}
