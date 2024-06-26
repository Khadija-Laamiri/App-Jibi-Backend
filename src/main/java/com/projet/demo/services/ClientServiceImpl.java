package com.projet.demo.services;

import com.projet.demo.entity.Client;
import com.projet.demo.entity.AgentService;
import com.projet.demo.entity.Operation;
import com.projet.demo.entity.PaymentAccount;
import com.projet.demo.mapper.*;
import com.projet.demo.model.*;
import com.projet.demo.repository.AgentServiceRepository;
import com.projet.demo.repository.ClientRepository;
import com.projet.demo.repository.OperationRepository;
import com.projet.demo.repository.PaymentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private PaymentAccountRepository paymentAccountRepository;
    @Autowired
    private AgentServiceRepository agentServiceRepository;
    @Autowired
    private OperationRepository operationRepository;

    @Override
    public long getAccountId(long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));
        return client.getPaymentAccount().getPaymentAccountId();
    }

    @Override
    public ClientProfileResponse getAccount(long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));
        return ClientProfileResponse.builder().firstName(client.getFirstName()).lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber()).email(client.getEmail()).build();
    }

    /*public RegisterAgentResponse changePassword(ClientRequest request) {
        Optional<Client> optionalClient = clientRepository.findByPhoneNumber(request.getPhoneNumber());
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.setPassword(passwordEncoder.encode(request.getNewPassword()));
            client.setIsFirstLogin(false);
            clientRepository.save(client);
            return RegisterAgentResponse.builder().message("Password updated successfully").build();
        } else {
            return RegisterAgentResponse.builder().message("Client not found").build();
        }
    }*/

    @Override
    public List<CreaditorsRequest> getAllCreditors(){
        List<Client> agents = clientRepository.findAllAgentWithRoleClient();
        return agents.stream()
                .map(CreditorsMapper::ConvertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<AgentServiceResponse> getAllServicesByAgentId(Long agentId) {
        List<AgentService> servicesAgent = agentServiceRepository.findAllByAgentId(agentId);
        return servicesAgent.stream()
                .map(AgentServiceMapper::ConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentAccountResponse getPaymentAccountByClientId(long id) {
        PaymentAccount paymentAccount = paymentAccountRepository.findPaymentAccountByClientId(id);
        if (paymentAccount != null) {
            return PaymentAccountMapper.ConvertToDto(paymentAccount);
        }
        return null;
    }

    @Override
    public ClientProfileResponse getClientById(long id) {
        Client client = clientRepository.findClientByClientId(id);
        if (client != null) {
            return ClientMapper.ConvertToDto(client);
        }
        return null;
    }


    @Override
    public ClientProfileResponse getClientByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber)
                .map(ClientMapper::ConvertToDto)
                .orElse(null);
    }

    @Override
    public List<OperationResponse> getClientOperation(String phoneNumber) {
        List<Operation> operations = operationRepository.findOperationsByPhoneNumber(phoneNumber);
        return operations.stream()
                .map(OperationMapper::ConvertToDto)
                .collect(Collectors.toList());
    }

    public RegisterAgentResponse changePassword(ClientRequest request) {

        Client client = clientRepository.findByPhoneNum(request.getPhoneNumber());
        System.out.println(client);
        if (!(client == null) ) {
            client.setPassword(passwordEncoder.encode(request.getNewPassword()));
            System.out.println(request.getNewPassword());
            client.setIsFirstLogin(false);
            clientRepository.save(client);
            return RegisterAgentResponse.builder().message("Password updated successfully").build();
        } else {
            return RegisterAgentResponse.builder().message("Client not found").build();
        }
    }

}








