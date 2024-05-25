package com.projet.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.projet.demo.model.CreditorType;
import com.projet.demo.model.CustomerOrder;
import com.projet.demo.model.PaymentAccount;
import com.projet.demo.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "transactionId")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;
    private double amount;
    private String creditor;
    @Temporal(TemporalType.DATE)
    private Date date;
    private TransactionStatus transactionStatus;
    private String description;
    private CreditorType creditorType;
    private String phoneNumber;
    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore // Add this annotation to break the potential circular reference

    private CustomerOrder order;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentAccountId")
    @JsonIgnoreProperties("transactions")
    @JsonIgnore
    private PaymentAccount paymentAccount;

}
