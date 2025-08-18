package com.oauth.controller;

import com.oauth.dto.AgentDto;
import com.oauth.dto.JwtAuthResponse;
import com.oauth.dto.LoginDto;
import com.oauth.dto.Response;
import com.oauth.entity.Agent;
import com.oauth.exception.ResourceNotFoundException;
import com.oauth.repo.AgentRepo;
import com.oauth.security.JwtTokenHelper;
import com.oauth.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AgentRepo agentRepo;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @PostMapping("/register")
    public ResponseEntity<Response> create(@RequestBody @Valid AgentDto agentDto){
            AgentDto agentDto1 = this.agentService.createAgent(agentDto);
            Response response = Response.buildResponse(
            "success",
                    "User Created Successfully",
                    201,
                    agentDto1,
                    "Process Executed Successfully"
            );
            return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Integer id)
    {
        AgentDto agentDto = this.agentService.getById(id);
        Response response = Response.buildResponse(
                "success",
                "UserFetched Successful",
                200,
                agentDto,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll(){
        List<AgentDto> agents = this.agentService.getAllAgent();
        Response response = Response.buildResponse(
                "success",
                "UserFetched Successful",
                200,
                agents,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Integer id) {
        this.agentService.deleteAgent(id);
        Response response = Response.buildResponse(
                "success",
                "User Deleted Successful",
                200,
                null,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@RequestBody AgentDto agentDto, @PathVariable Integer id){
        AgentDto agentDto1 = this.agentService.updateAgent(agentDto, id);
        Response response = Response.buildResponse(
                "success",
                "User Updated Successful",
                200,
                agentDto1,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto request) {

        // 1. Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // 3. Get agent from DB
        Agent agent = agentRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "Email", 0));

        // 4. Compare role
        String dbRole = agent.getRole().getName().name(); // assuming enum stored in DB
        String requestRole = request.getRole().name();

        if (!dbRole.equalsIgnoreCase(requestRole)) {
            throw new RuntimeException("Access denied: Role mismatch. You are not authorized as " + requestRole);
        }

        // 5. Generate JWT with role
        String token = jwtTokenHelper.generateToken(userDetails, dbRole);

        // 6. Return token and role in response
        return ResponseEntity.ok(new JwtAuthResponse(token, dbRole));
    }

}
