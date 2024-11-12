package com.customer.account.service;

import com.customer.account.dto.AccountInfo;
import com.customer.account.dto.TransactionInfo;
import com.customer.account.dto.TransactionRequest;
import com.customer.account.dto.UserInfoResponse;
import com.customer.account.entity.Account;
import com.customer.account.entity.CustomerDetail;
import com.customer.account.exceptionhandler.ConnectivityException;
import com.customer.account.exceptionhandler.CustomerNotFoundException;
import com.customer.account.repository.AccountRepository;
import com.customer.account.repository.CustomerDetailRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerDetailRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public AccountServiceImpl() {
    }

    @Transactional
    public Optional<Account> openAccount(Long customerId, double initialCredit) throws CustomerNotFoundException, ConnectivityException {

        CustomerDetail customerDetail = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with customerId : " + customerId));

        Account account = new Account();
        account.setCustomerDetail(customerDetail);
        account.setBalance(initialCredit);
        Account savedAccount = accountRepository.save(account);

        // If initial credit is provided, create a transaction in the Transaction Service
        createTransaction(customerId, initialCredit, account);

        return Optional.of(new Account(savedAccount.getId(), customerDetail, savedAccount.getBalance()));
    }

    public void createTransaction(Long customerId, double initialCredit, Account account) throws ConnectivityException {
        if (initialCredit > 0) {
            TransactionRequest transactionRequest = new TransactionRequest(account.getId(), initialCredit);
            try {
                restTemplate.postForObject("http://localhost:8082/api/transactions/create", transactionRequest, String.class);
            } catch (RestClientException e) {
                throw new ConnectivityException("Please try again later to open account for customer : " + customerId);
            }
        }
    }

    public Optional<UserInfoResponse> getAccountInfo(Long customerId) throws CustomerNotFoundException {

        CustomerDetail customerDetail = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with customerId : " + customerId));

        UserInfoResponse response = new UserInfoResponse();
        response.setName(customerDetail.getName());
        response.setSurname(customerDetail.getSurname());

        double totalBalance = 0.0;
        List<Account> accounts = accountRepository.findByCustomerDetail(customerDetail);

        for (Account account : accounts) {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountId(account.getId());
            accountInfo.setBalance(account.getBalance());
            totalBalance += account.getBalance();

            // Retrieve transactions for each account from the Transaction Service
            ResponseEntity<List<TransactionInfo>> transactionInfoListResp = null;
            try {
                transactionInfoListResp = restTemplate.exchange(
                        "http://localhost:8082/api/transactions/account/" + account.getId(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
            } catch (RestClientException e) {
                log.info("Issue occurred with connecting transaction microservice ");
            }

            List<TransactionInfo> transactionInfoList = new ArrayList<>();
            if (transactionInfoListResp != null && transactionInfoListResp.getStatusCode().is2xxSuccessful()) {
                transactionInfoList = transactionInfoListResp.getBody();
            }

            accountInfo.setTransactions(transactionInfoList);
            response.getAccounts().add(accountInfo);
        }

        response.setBalance(totalBalance);
        return Optional.of(response);
    }
}
