package com.projet.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refOperation;

    private double amount;

    private String creditorName;

    private Long idCreditor;

    private String serviceName;

    private LocalDateTime doItAt;

    @ManyToOne
    private Client client;


}