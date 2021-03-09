package ru.vas.restcore.service;

import ru.vas.restcore.db.domain.UserEntity;

public interface SecurityService {

    /**
     * Текущий пользователь
     */
    UserEntity currentUser();
}
