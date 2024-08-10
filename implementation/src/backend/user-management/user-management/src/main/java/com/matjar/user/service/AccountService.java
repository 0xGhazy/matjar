package com.matjar.user.service;

import com.matjar.user.dto.AccountDto;
import com.matjar.user.dto.Response;
import com.matjar.user.exception.DuplicateEmailException;
import com.matjar.user.model.Account;
import com.matjar.user.model.AccountStatus;
import com.matjar.user.model.Address;
import com.matjar.user.repository.AccountRepository;
import com.matjar.user.validation.DateTimeValidation;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Log4j2
public class AccountService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final AccountRepository accountRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AddressService addressService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
    }

    public Response insertAccount(AccountDto accountDto) {
        String uuid = UUID.randomUUID().toString();
        Account StoredAccount = null;
        Address address;

        Account account = toEntity(accountDto);
        account.setId(uuid);
        account.setStatus(AccountStatus.PENDING_ACTIVATION.name());
        account.setCreatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Date dob = DateTimeValidation.validateDob(accountDto.getDob());
        account.setDateOfBirth(dob);

        try{
            StoredAccount = accountRepository.save(account);
            log.info("account created successfully with id: {}", uuid);
            address = addressService.insert(new Address(uuid));
            log.info("address object with id: {} created successfully for account: {}", address.getId(), uuid);
        } catch (DataIntegrityViolationException e) {
            log.error("duplicate email found with {}, exception: {}", account.getEmail(), e.getMessage());
            throw new DuplicateEmailException("provided email address is already exist");
        }

        // TODO: produce message to message queue for activation email

        return  Response
                .builder()
                .message("account created successfully")
                .data(toDto(StoredAccount))
                .status(HttpStatus.CREATED)
                .build();
    }










    private Account toEntity(AccountDto accountDto) { return modelMapper.map(accountDto, Account.class); }
    private AccountDto toDto(Account account) { return modelMapper.map(account, AccountDto.class); }

}
