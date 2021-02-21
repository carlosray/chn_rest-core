package ru.vas.restcore.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vas.restcore.api.dto.JwtTokenDTO;
import ru.vas.restcore.api.dto.UserRegisterDTO;
import ru.vas.restcore.db.domain.PersonInfo;
import ru.vas.restcore.db.domain.RoleEntity;
import ru.vas.restcore.db.domain.UserEntity;
import ru.vas.restcore.db.repo.RoleRepository;
import ru.vas.restcore.db.repo.UserRepository;
import ru.vas.restcore.exception.UsernameAlreadyExists;
import ru.vas.restcore.jwt.JwtUtils;
import ru.vas.restcore.service.UserService;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public JwtTokenDTO login(String username, String password) {
        final UserDetails userDetails = this.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new JwtTokenDTO(jwtUtils.generateToken(userDetails));
        }
        throw new BadCredentialsException("Некорректный пароль");
    }

    @Override
    public UserEntity register(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new UsernameAlreadyExists();
        }
        final UserEntity userEntity = parseRegisterDTO(userRegisterDTO);
        return userRepository.save(userEntity);
    }

    private UserEntity parseRegisterDTO(UserRegisterDTO userRegisterDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final PersonInfo personInfo = objectMapper.convertValue(userRegisterDTO, PersonInfo.class);
        return new UserEntity()
                .withUsername(userRegisterDTO.getUsername())
                .withPassword(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .withPersonInfo(personInfo)
                .withRoles(roles());
    }

    private Set<RoleEntity> roles() {
        return roleRepository.findByRoleName("ROLE_USER")
                .map(Collections::singleton)
                .orElse(Collections.emptySet());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Не найден пользователь с логином '%s'", username)));
    }

}
