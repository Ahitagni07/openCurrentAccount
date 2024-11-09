package com.customer.account.repository;

import com.customer.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByCustomerId(Long customerId);
}
