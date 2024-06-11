package com.projet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OperationResponse {

    private Long id;

    private String ref;

    private String clientName;

    private String creditorName;

    private String serviceName;

    private double amount;

    private String doItAt;


}
