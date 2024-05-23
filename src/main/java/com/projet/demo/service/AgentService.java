package com.projet.demo.service;

import com.projet.demo.entity.Client;
import com.projet.demo.repository.AgentRepo;
import com.projet.demo.repository.ClientRepo;

import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;



@Service
@RequiredArgsConstructor
public class AgentService {
    @Autowired
    private ClientRepo repository;
 //   private PasswordEncoder passwordEncoder;
    private final String BRAND_NAME = "NXSMS";
    @Autowired
    private VonageClient vonageClient;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }


    public String registerClient(Client request) {

        if (repository.existsByPhoneNumber(request.getPhoneNumber()) && repository.existsByEmail(request.getEmail() ) ) {
            throw new RuntimeException("Email Or Phone already exists");
        }

        String generatedpassword = generatePassword();
        var Clinet = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .cin(request.getCin())
                .isFirstLogin(true)
                .isPaymentAccountOn(false)
                .createdAt(LocalDateTime.now())
                .isPaymentAccountOn(false)
                .birthdDate(request.getBirthdDate())
                .build();
        var savedAgent = repository.save(Clinet);
        TextMessage message = new TextMessage(BRAND_NAME, request.getPhoneNumber(), "Your password is : " + generatedpassword);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        // Log the response to check for errors
        response.getMessages().forEach(m -> {
            System.out.println("Status: " + m.getStatus() + ", Error: " + m.getErrorText());
        });
        return "success ";
    }

    public String updateClient(Long id, Client client) {
        // Find the client by ID
        Optional<Client> optionalClient = repository.findById(id);

        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();
            existingClient.setFirstName(client.getFirstName());
            existingClient.setLastName(client.getLastName());
            existingClient.setEmail(client.getEmail());
            existingClient.setAddress(client.getAddress());
            existingClient.setCin(client.getCin());
            existingClient.setPhoneNumber(client.getPhoneNumber());
            existingClient.setBirthdDate(client.getBirthdDate());

            // Save the updated client
            repository.save(existingClient);
            return "Client updated successfully";
        } else {
            // Client with the given ID not found
            return "Client with ID " + id + " not found";
        }
    }


    public String deleteClient(Long id) {
        Optional<Client> optionalClient = repository.findById(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            repository.delete(client);
            return "Client deleted successfully";
        } else {
            return "Client with ID " + id + " not found";
        }
    }

}
