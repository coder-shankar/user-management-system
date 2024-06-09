package com.example.user_management_system.user;


import com.example.user_management_system.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    protected UserMapper mapper;

    public UserDto createUser(UserDto dto) {
        User user = repository.save(mapper.toEntity(dto));
        return mapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        return repository.findByUsername(username)
                         .map(mapper::toDto)
                         .orElseThrow(() -> new ResourceNotFoundException("User not Found"));
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

    public UserDto getUserByEmail(String email) {
        return repository.findByEmail(email)
                         .map(mapper::toDto)
                         .orElseThrow(() -> new ResourceNotFoundException("User not Found"));
    }

    public UserDto updateUser(Long id, UserDto userDetails) {
        Long userId = repository.findById(id)
                                .map(User::getId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
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
