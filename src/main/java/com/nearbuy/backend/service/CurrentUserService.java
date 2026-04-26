package com.nearbuy.backend.service;

import com.nearbuy.backend.entity.User;

public interface CurrentUserService {

    User getCurrentUser();

    Long getCurrentUserId();
}
