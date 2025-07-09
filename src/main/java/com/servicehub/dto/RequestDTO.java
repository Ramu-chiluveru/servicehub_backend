//package com.servicehub.dto;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import com.servicehub.model.Proposal;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@Data
//@AllArgsConstructor
//public class RequestDTO {
//    private String id;
//    private String category;
//    private String description;
//    private Double price;
//    private String status;
//    private LocalDateTime createdAt;
//    private LocalDateTime completedAt;
//    private List<Proposal> proposals;
//}

package com.servicehub.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private String id;
    private String category;
    private String description;
    private Double price;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String priority; // Added to match your Requests entity
    private List<ProposalDTO> proposals; // Using ProposalDTO instead of Proposal entity
}