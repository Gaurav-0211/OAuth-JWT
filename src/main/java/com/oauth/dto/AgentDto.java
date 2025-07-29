package com.oauth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgentDto {
    private int id;

    @NotBlank(message = "Name cannot be Blank")
    private String agentName;

    @Email
    @NotBlank(message = "Add your email here")
    private String email;

    @NotBlank(message = "Password must contain alpha-numeric and special character value")
    private String password;

    @NotBlank(message = "Mobile Number is Required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Must contain 10 digit")
    private String mobileNumber;

    @NotBlank(message = "City name is Required")
    private String agentCity;

    @NotBlank(message = "State is Required")
    private String agentState;

}
