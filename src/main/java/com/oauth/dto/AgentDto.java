package com.oauth.dto;


import com.oauth.config.RoleType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AgentDto {
    private int id;

    @NotEmpty(message = "Name can't be Empty")
    @Size(min = 2, max = 20, message = "Name can't be less that 2 character")
    private String agentName;

    @Email
    @NotBlank(message = "Add your email here")
    private String email;

    @NotBlank(message = "Password must contain alpha-numeric and special character value")
    @Size(min = 6, max = 20, message = "Make a secure password with special characters")
    private String password;

    @NotBlank(message = "Mobile Number is Required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Must contain 10 digit")
    private String mobileNumber;

    @NotBlank(message = "City name is Required")
    @Size(min = 3, max = 20,message= "City name can't be empty")
    private String agentCity;

    @NotBlank(message = "State is Required")
    @Size(min = 3, max = 20, message = "State name can't be null")
    private String agentState;

    private int roleId;

    private RoleDto roleDto;
}
