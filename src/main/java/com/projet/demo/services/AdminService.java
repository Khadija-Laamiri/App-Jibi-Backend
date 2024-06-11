package com.projet.demo.services;


import com.projet.demo.config.JwtService;
import com.projet.demo.entity.BankAccount;
import com.projet.demo.entity.Client;
import com.projet.demo.entity.Role;
import com.projet.demo.model.AgentRequest;
import com.projet.demo.model.RegisterAgentResponse;
import com.projet.demo.repository.BankAccountRepository;
import com.projet.demo.repository.ClientRepository;
import com.projet.demo.token.Token;
import com.projet.demo.token.TokenRepository;
import com.projet.demo.token.TokenType;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:4200")
@Service
@RequiredArgsConstructor
public class AdminService {
    private final ClientRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BankAccountRepository bankAccountRepository;

    private static final String CHARACTERS = "0123456789";
    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public Client registerAgent(AgentRequest request) {
        if (repository.existsByPhoneNumber(request.getPhoneNumber()) && repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Phone num already exists Or Email");
        }
        BankAccount bankAccount= BankAccount.builder()
                .balance(100000000)
                .build();
        bankAccountRepository.save(bankAccount);
        String generatedPassword =generatePassword();
        var Agent = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .email(request.getEmail())
                .cin(request.getCin())
                .birthDate(request.getBirthDate())
                .phoneNumber(request.getPhoneNumber())
                .commercialRn(request.getCommercialRn())
                .patentNumber(request.getPatentNumber())
                .password(passwordEncoder.encode(generatedPassword))
                .image(request.getImage())
                .role(Role.AGENT)
                .build();
        Agent.setBankAccount(bankAccount);
        Agent.setIsFirstLogin(true);
        String formattedPhoneNumber=formatPhoneNumber(request.getPhoneNumber());
        System.out.println(formattedPhoneNumber);
        var savedAgent = repository.save(Agent);
        var jwtToken = jwtService.generateToken(Agent);

        saveUserToken(savedAgent, jwtToken);

        /*VonageClient client = VonageClient.builder().apiKey("b5813842").apiSecret("pDcUfRrQBiEAbd7s").build();
        System.out.println(client);
        TextMessage message = new TextMessage("Jibi LKLCSF",
                formattedPhoneNumber,
                "Bonjour "+ request.getFirstName() + ", votre mot de passe est "+ generatedPassword + "."
        );
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }*/
        System.out.println(RegisterAgentResponse.builder().message("success"+generatedPassword).build());
        return savedAgent;
    }

    public String formatPhoneNumber(String phoneNumber) {

        String formatted = phoneNumber.substring(1);
        return "212" + formatted;
    }

    private void saveUserToken(Client user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Client user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public List<Client> findAll() {
        return repository.findAllAgentWithRoleClient();
    }

    public Client findById(Long id) {
        return repository.findAgentByClientId(id);
    }


  public RegisterAgentResponse updateAgent(Long id , AgentRequest userUpdateRequest) {
        Client agent=
               repository.findAgentByClientId(id);
                       if(agent!=null) {
                     agent.setFirstName(userUpdateRequest.getFirstName());
                     agent.setLastName(userUpdateRequest.getLastName());
                     agent.setEmail(userUpdateRequest.getEmail());
                     agent.setAddress(userUpdateRequest.getAddress());
                     agent.setPhoneNumber(userUpdateRequest.getPhoneNumber());
                     agent.setCommercialRn(userUpdateRequest.getCommercialRn());
                     agent.setPatentNumber(userUpdateRequest.getPatentNumber());

                           repository.save(agent);
                       }else {
                           System.out.println("The Agent with the Id Given not exist in the database ");
                       }

            return RegisterAgentResponse.builder().message("Agent updated successfully").build();
        }

    public RegisterAgentResponse delete(Long id) {
        Client agent = repository.findAgentByClientId(id);
        if(agent !=null){
            repository.delete(agent);
            return RegisterAgentResponse.builder().message("Deleted with Success").build();
        }else {
            return RegisterAgentResponse.builder().message("Error during Deleting").build();
        }
    }

}

