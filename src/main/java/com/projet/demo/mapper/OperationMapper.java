package com.projet.demo.mapper;

import com.projet.demo.entity.Operation;
import com.projet.demo.model.OperationResponse;

import java.text.SimpleDateFormat;

public class OperationMapper {

    static public OperationResponse ConvertToDto(Operation operation){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(operation.getDoItAt());

        return OperationResponse.builder()
                .id(operation.getId())
                .ref(operation.getRef())
                .clientName(operation.getClient().getLastName() + " " + operation.getClient().getFirstName())
                .creditorName(operation.getCreditorName())
                .serviceName(operation.getServiceName())
                .amount(operation.getAmount())
                .doItAt(formattedDate)
                .build();
    }

}
