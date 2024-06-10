package com.example.user_management_system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Model representing an User details")
public class UserDto {

    @Schema(description = "unique username", example = "shankar.ghimire")
    @NotBlank(message = "Username is mandatory")
    @Size(min = 2, max = 30)
    private String username;

    @Schema(description = "first name", example = "shankar")
    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 30)
    private String firstName;

    @Schema(description = "last name", example = "ghimire")
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 30)
    private String lastName;

    @Schema(description = "valid email", example = "ghimire@mail.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Date of birth in yyy-mm-dd format", example = "1980-03-21")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    //response only
    private Long id;

}
