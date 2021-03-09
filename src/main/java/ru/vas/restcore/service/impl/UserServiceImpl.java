package ru.vas.restcore.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vas.restcore.api.dto.JwtTokenDTO;
import ru.vas.restcore.api.dto.UserInfoDTO;
import ru.vas.restcore.api.dto.UserRegisterDTO;
import ru.vas.restcore.db.domain.PersonInfo;
import ru.vas.restcore.db.domain.RoleEntity;
import ru.vas.restcore.db.domain.UserEntity;
import ru.vas.restcore.db.repo.RoleRepository;
import ru.vas.restcore.db.repo.UserRepository;
import ru.vas.restcore.exception.UsernameAlreadyExists;
import ru.vas.restcore.jwt.JwtUtils;
import ru.vas.restcore.service.SecurityService;
import ru.vas.restcore.service.UserService;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Set<RoleEntity> defaultRoles;
    private final SecurityService securityService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public UserServiceImpl(JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, SecurityService securityService) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultRoles = roleRepository.findByRoleNameIn(Sets.newHashSet("ROLE_USER"));
    }

    @Override
    public JwtTokenDTO login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(jwtUtils::generateToken)
                .map(JwtTokenDTO::new)
                .orElseThrow(() -> new BadCredentialsException("Неверный логин или пароль"));
    }

    @Override
    public UserEntity register(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new UsernameAlreadyExists();
        }
        final UserEntity userEntity = parseRegisterDTO(userRegisterDTO);
        return userRepository.save(userEntity);
    }

    @Override
    public void saveUserInfo(UserInfoDTO dto) {
        final UserEntity currentUser = securityService.currentUser();
        final PersonInfo personInfo = objectMapper.convertValue(dto, PersonInfo.class);
        currentUser.setPersonInfo(personInfo);
        currentUser.setEmail(dto.getEmail());
        userRepository.save(currentUser);
    }

    @Override
    public UserInfoDTO getUserInfo() {
        final UserEntity currentUser = securityService.currentUser();
        return UserInfoDTO.builder()
                .email(currentUser.getEmail())
                .firstName(currentUser.getPersonInfo().getFirstName())
                .lastName(currentUser.getPersonInfo().getLastName())
                .city(currentUser.getPersonInfo().getCity())
                .country(currentUser.getPersonInfo().getCountry())
                .company(currentUser.getPersonInfo().getCompany())
                .info(currentUser.getPersonInfo().getInfo())
                .build();
    }

    private UserEntity parseRegisterDTO(UserRegisterDTO userRegisterDTO) {
        final PersonInfo personInfo = objectMapper.convertValue(userRegisterDTO, PersonInfo.class);
        return new UserEntity()
                .withUsername(userRegisterDTO.getUsername())
                .withPassword(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .withEmail(userRegisterDTO.getEmail())
                .withPersonInfo(personInfo)
                .withRoles(defaultRoles);
    }

}
