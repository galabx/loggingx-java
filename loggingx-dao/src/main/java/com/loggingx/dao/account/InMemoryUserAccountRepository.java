package com.loggingx.dao.account;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserAccountRepository implements UserAccountRepository {

    private final Map<String, UserAccount> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return Optional.ofNullable(accounts.get(username));
    }

    @Override
    public boolean saveIfAbsent(UserAccount account) {
        return accounts.putIfAbsent(account.username(), account) == null;
    }
}
