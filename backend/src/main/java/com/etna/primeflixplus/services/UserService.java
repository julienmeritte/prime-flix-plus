package com.etna.primeflixplus.services;

import com.etna.primeflixplus.dtos.PaginationDto;
import com.etna.primeflixplus.dtos.UserDto;
import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.enums.UserRole;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() throws CustomGlobalException {
        /*Pageable paging = PageRequest.of(paginationDto.getPage(), paginationDto.getSize(), Sort.by("mail").ascending());
        Page<User> usersSort = userRepository.findAll(paging);*/
        List<User> users = userRepository.findAll();
        if (users.isEmpty())
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.USER_GET_ALL_FAILED);
        return users;
    }

    public User getUserById(Integer idUser) throws CustomGlobalException {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty())
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.USER_GET_ONE_FAILED);
        return user.get();
    }

    public User modifyUser(Integer id, UserDto userDto) throws CustomGlobalException {
        User user = getUserById(id);
        if (userDto.getMail() != null)
            user.setMail(userDto.getMail());
        if (userDto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getEnabled() != null)
            user.setEnabled(userDto.getEnabled());
        if (userDto.getRole() != null && manageUserRoleFromInt(userDto.getRole()) != null)
            user.setRole(manageUserRoleFromInt(userDto.getRole()));
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Integer id) throws CustomGlobalException {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    private UserRole manageUserRoleFromInt(Integer role) {
        switch (role) {
            case 0:
                return UserRole.ROLE_USER;
            case 1:
                return UserRole.ROLE_SUPPORT;
            case 2:
                return UserRole.ROLE_AFFILIATE;
            case 3:
                return UserRole.ROLE_ADMIN;
            default:
                return null;
        }
    }


}
