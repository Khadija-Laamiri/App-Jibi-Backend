package com.projet.demo.services;


import com.projet.demo.entity.*;
import com.projet.demo.entity.AgentService;
import com.projet.demo.model.FeedAccountRequest;
import com.projet.demo.model.FeedAccountResponse;
import com.projet.demo.model.PaymentRequest;
import com.projet.demo.model.PaymentResponse;
import com.projet.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class CmiService {


    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @Autowired
    private AgentServiceRepository agentServiceRepository;

    @Autowired
    private OperationRepository operationRepository;


    // feed payment Account
    public FeedAccountResponse feedPaymentAccount(FeedAccountRequest feedAccountRequest) {

        Long idClient = feedAccountRequest.getIdClient();
        PaymentAccount paymentAccount = clientRepository.findClientByClientId(idClient).getPaymentAccount();
        BankAccount bankAccount = clientRepository.findClientByClientId(idClient).getBankAccount();


        double amount = feedAccountRequest.getAmount();

        if (bankAccount == null) {
            return new FeedAccountResponse( "Compte bancaire introuvable");
        }

        if (bankAccount.getBalance() < amount) {
            return new FeedAccountResponse("Solde insuffisant");
        }

        try {
            validateAmount(paymentAccount, amount);
        } catch (Exception e) {
            return new FeedAccountResponse(e.getMessage());
        }

        paymentAccount.setAccountBalance(paymentAccount.getAccountBalance() + amount);
        bankAccount.setBalance(bankAccount.getBalance() - amount);

        bankAccountRepository.save(bankAccount);
        paymentAccountRepository.save(paymentAccount);

        return new FeedAccountResponse("Alimentation du compte réussie");
    }




    // validate amount by payment account type
    private void validateAmount(PaymentAccount paymentAccount, double amount) throws Exception {
        String type = paymentAccount.getType();

        switch (type) {
            case "200":
                if (amount > 200) {
                    throw new Exception("Impossible d'alimenter avec ce montant");
                }
                break;
            case "5000":
                if (amount > 5000) {
                    throw new Exception("Impossible d'alimenter avec ce montant");
                }
                break;
            case "20000":
                if (amount > 20000) {
                    throw new Exception("Impossible d'alimenter avec ce montant");
                }
                break;
        }
    }


    public void doTransaction(Client agent, Client client, double amount){

        BankAccount bankAccount_creditor = agent.getBankAccount();
        PaymentAccount paymentAccount = client.getPaymentAccount();

        paymentAccount.setAccountBalance(paymentAccount.getAccountBalance() - amount);
        bankAccount_creditor.setBalance(bankAccount_creditor.getBalance() + amount);

        paymentAccountRepository.save(paymentAccount);
        bankAccountRepository.save(bankAccount_creditor);

    }


    public PaymentResponse payCreditorService(PaymentRequest paymentRequest){

        Client creditor = clientRepository.findAgentByClientId(paymentRequest.getIdCreditor());
        Client client = clientRepository.findClientByClientId(paymentRequest.getIdClient());
        AgentService service = agentServiceRepository.findServiceById(paymentRequest.getIdService());

        double amount = paymentRequest.getAmount();

        if (client.getPaymentAccount().getAccountBalance() >= amount) {
            doTransaction(creditor, client, amount);
            Operation operation = new Operation();
            operation.setRef(paymentRequest.getRefOperation());
            operation.setAmount(amount);
            operation.setCreditorName(creditor.getFirstName() + " " + creditor.getLastName());
            operation.setIdCreditor(paymentRequest.getIdCreditor());
            operation.setServiceName(service.getName());
            operation.setDoItAt(new Date());
            operation.setClient(client);
            operationRepository.save(operation);

            return new PaymentResponse(1,"Transaction effectuée avec succès.");
        } else {
            return new PaymentResponse(0,"Solde insuffisant.");
        }
    }




}
