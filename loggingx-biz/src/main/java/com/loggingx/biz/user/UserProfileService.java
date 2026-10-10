package com.loggingx.biz.user;

import com.loggingx.common.exception.NotFoundException;
import com.loggingx.dao.account.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserAccountRepository userAccountRepository;

    public UserProfileService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserProfile getByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .map(account -> new UserProfile(account.id(), account.username(), account.role(), account.createdAt()))
                .orElseThrow(() -> new NotFoundException("User account was not found"));
    }
}
