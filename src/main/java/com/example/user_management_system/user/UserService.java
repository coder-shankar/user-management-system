package com.example.user_management_system.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    public UserDto createUser(UserDto dto) {
        User user = repository.save(mapper.toEntity(dto));
        return mapper.toDto(user);
    }

    public Optional<UserDto> getUserByUsername(String username) {
        return repository.findByUsername(username)
                         .map(mapper::toDto);
    }

    public List<UserDto> getUsersByFirstName(String firstName) {
        return repository.findByFirstName(firstName).stream()
                         .map(mapper::toDto)
                         .toList();
    }

    public List<UserDto> getUsersByLastName(String lastName) {
        return repository.findByLastName(lastName)
                         .stream()
                         .map(mapper::toDto)
                         .toList();
    }

    public Optional<UserDto> getUserByEmail(String email) {
        return repository.findByEmail(email)
                         .map(mapper::toDto);
    }

    public UserDto updateUser(Long id, UserDto userDetails) {
        Long userId = repository.findById(id)
                                .map(User::getId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
        User user = mapper.toEntity(userDetails);
        user.setId(userId);
        repository.save(user);

        return mapper.toDto(user);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable)
                         .map(mapper::toDto);
    }
}
