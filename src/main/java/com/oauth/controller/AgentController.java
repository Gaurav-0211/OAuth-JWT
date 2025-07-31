package com.oauth.controller;

import com.oauth.dto.AgentDto;
import com.oauth.dto.JwtAuthResponse;
import com.oauth.dto.LoginDto;
import com.oauth.entity.Agent;
import com.oauth.exception.ResourceNotFoundException;
import com.oauth.repo.AgentRepo;
import com.oauth.security.JwtTokenHelper;
import com.oauth.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<AgentDto> create(@RequestBody @Valid AgentDto agentDto){
        try {
            AgentDto agentDto1 = this.agentService.createAgent(agentDto);
            return new ResponseEntity<AgentDto>(agentDto1, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDto> getById(@PathVariable Integer id){
        AgentDto agentDto = this.agentService.getById(id);
        return new ResponseEntity<AgentDto>(agentDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AgentDto>> getAll(){
        List<AgentDto> agents = this.agentService.getAllAgent();
        return new ResponseEntity<List<AgentDto>>(agents, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) {
        this.agentService.deleteAgent(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User with ID " + id + " has been successfully deleted.");

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AgentDto> update(@RequestBody AgentDto agentDto, @PathVariable Integer id){
        AgentDto agentDto1 = this.agentService.updateAgent(agentDto, id);
        return new ResponseEntity<AgentDto>(agentDto1, HttpStatus.OK);
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
