package com.projet.demo.controller;


import com.projet.demo.dto.ClientDTO;
import com.projet.demo.dto.PaymentAccountDTO;
import com.projet.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;



   // public  ResponseEntity<ClientDTO> getAccount(@PathVariable("id")  long id ){
      //  return  new ResponseEntity<>(clientService.getAccountById(id), HttpStatus.OK);
    //}
   @GetMapping("/profile/Account/{id}")
    public ResponseEntity<?> getAccount(@PathVariable("id")  long id ){
        return ResponseEntity.ok(clientService.getAccountById(id));
    }


    @GetMapping("/profile/Account/{phoneNumber}")
    public  ResponseEntity<ClientDTO> getAccountByPhoneNumber(@PathVariable("phoneNumber")  String phoneNumber ){
        return  new ResponseEntity<>(clientService.getAccountByPhoneNumber(phoneNumber), HttpStatus.OK);
    }

    @GetMapping("/profile/PaymentAccount/{id}")
    public  ResponseEntity<PaymentAccountDTO> getPaymentAccount(@PathVariable("id")  long id ){
        return  new ResponseEntity<>(clientService.getPaymentAccount(id), HttpStatus.OK);
    }



}