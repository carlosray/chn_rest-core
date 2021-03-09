package ru.vas.restcore.service;

import ru.vas.restcore.api.dto.JwtTokenDTO;
import ru.vas.restcore.api.dto.UserInfoDTO;
import ru.vas.restcore.api.dto.UserRegisterDTO;
import ru.vas.restcore.db.domain.UserEntity;

public interface UserService {
    /**
     * Проверка логина и пароля, если ок - то выдать jwt токен
     * @param username логин
     * @param password пароль
     * @return токен
     */
    JwtTokenDTO login(String username, String password);

    /**
     * Процесс регистрации
     * @param userRegisterDTO инфо для регистрации
     * @return true - успешно, false - провал
     */
    UserEntity register(UserRegisterDTO userRegisterDTO);

    /**
     * Сохранить пользовательское инфо
     * @param dto инфо
     */
    void saveUserInfo(UserInfoDTO dto);

    /**
     * Получить пользовательское инфо
     * @return инфо
     */
    UserInfoDTO getUserInfo();

}
