package com.loggingx.dao.account;

import java.util.Optional;

public interface UserAccountRepository {

    Optional<UserAccount> findByUsername(String username);

    boolean saveIfAbsent(UserAccount account);
}
