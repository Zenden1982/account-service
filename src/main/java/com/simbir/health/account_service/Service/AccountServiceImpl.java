package com.simbir.health.account_service.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;
import com.simbir.health.account_service.Class.DTO.AccountCreatedDTO;
import com.simbir.health.account_service.Interface.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final KafkaTemplate<String, AccountCreatedDTO> kafkaTemplate;

    @Override
    public String createAccount(AccountCreateDTO createAccountDTO) {
        String accountId = UUID.randomUUID().toString();
        AccountCreatedDTO accountCreatedDTO = new AccountCreatedDTO(accountId, createAccountDTO.getFirstName(),
                createAccountDTO.getLastName(), createAccountDTO.getUsername(), createAccountDTO.getPassword());

        CompletableFuture<SendResult<String, AccountCreatedDTO>> future = kafkaTemplate.send("account.events.created",
                accountId, accountCreatedDTO);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message='{}' due to: {}", accountCreatedDTO, exception.getMessage());
            } else {
                log.info("sent message='{}' with offset={}", accountCreatedDTO, result.getRecordMetadata().offset());
            }
        });
        return "OK";
    }

}
