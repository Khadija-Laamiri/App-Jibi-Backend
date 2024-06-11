package com.projet.demo.controller;

import com.projet.demo.model.*;
import com.projet.demo.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/client/infos")
@PreAuthorize("hasRole('CLIENT')")
public class ClientController {

    private final ClientService clientservice;


    @GetMapping("/services/{agentId}")
    @PreAuthorize("hasAuthority('client:read')")
    public List<AgentServiceResponse> getAllServicesByAgentId(@PathVariable Long agentId){
        return clientservice.getAllServicesByAgentId(agentId);
    }

    @GetMapping("/history/{phoneNumberClient}")
    @PreAuthorize("hasAuthority('client:read')")
    public List<OperationResponse> getAllServicesByAgentId(@PathVariable String phoneNumberClient){
        return clientservice.getClientOperation(phoneNumberClient);
    }

    @GetMapping("/PaymentAccount/{id}")
    @PreAuthorize("hasAuthority('client:read')")
    public PaymentAccountResponse getPaymentAccount(@PathVariable("id")  long id ){
        return  clientservice.getPaymentAccountByClientId(id);
    }

    @GetMapping("/Profile/{id}")
    @PreAuthorize("hasAuthority('client:read')")
    public ClientProfileResponse getClient(@PathVariable("id")  long id ){
        return  clientservice.getClientById(id);
    }

    @GetMapping("/Profile/Phone/{phoneNumber}")
    @PreAuthorize("hasAuthority('client:read')")
    public ClientProfileResponse getClientByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber){
        return  clientservice.getClientByPhoneNumber(phoneNumber);
    }

    @PreAuthorize("hasAuthority('client:update')")
    @PostMapping("/changePassword")
    public ResponseEntity<RegisterAgentResponse> changePassword(@RequestBody ClientRequest request) {
        RegisterAgentResponse client = clientservice.changePassword(request);
        return ResponseEntity.ok(client);
    }

}
