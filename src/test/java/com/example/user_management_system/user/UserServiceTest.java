package com.example.user_management_system.user;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService sut;

    @Mock
    UserRepository repository;

    @Spy
    UserMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(UserMapper.class);
        sut.mapper = mapper;
    }


    @Test
    @DisplayName("Should create user and return back user id")
    void testCreateUser() {
        //arrange
        UserDto userDto = createUserDto();
        User user = createUser();
        when(repository.save(any())).thenReturn(user);

        //act
        UserDto response = sut.createUser(userDto);

        //assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(12L);
        assertThat(response.getUsername()).isEqualTo("shankar");

    }

    @NotNull
    private User createUser() {
        User user = new User();
        user.setId(12L);
        user.setUsername("shankar");
        user.setEmail("shankar.ghimire@mail.com");
        user.setFirstName("shankar");
        user.setLastName("ghimire");

        return user;
    }

    @NotNull
    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername("shankar");
        userDto.setEmail("shankar.ghimire@mail.com");
        userDto.setFirstName("shankar");
        userDto.setLastName("ghimire");
        return userDto;
    }

    @Test
    @DisplayName("Should return user details matching username")
    void testGetUserByUsername() {
        //arrange
        when(repository.findByUsername(any())).thenReturn(Optional.of(createUser()));

        //act
        UserDto user = sut.getUserByUsername("shankar");

        //assert
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("shankar");

    }
}