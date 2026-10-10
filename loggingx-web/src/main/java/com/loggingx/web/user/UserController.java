package com.loggingx.web.user;

import com.loggingx.biz.user.UserProfile;
import com.loggingx.biz.user.UserProfileService;
import com.loggingx.common.api.ApiResponse;
import com.loggingx.web.user.dto.UserProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserProfileService userProfileService;

    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> currentUser(Authentication authentication) {
        UserProfile profile = userProfileService.getByUsername(authentication.getName());
        return ApiResponse.success(new UserProfileResponse(
                profile.id(), profile.username(), profile.role(), profile.createdAt()
        ));
    }

}
