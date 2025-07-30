package com.oauth.controller;

import com.oauth.dto.AgentDto;
import com.oauth.dto.JwtAuthResponse;
import com.oauth.dto.LoginDto;
import com.oauth.entity.Agent;
import com.oauth.exception.ResourceNotFoundException;
import com.oauth.repo.AgentRepo;
import com.oauth.security.JwtTokenHelper;
import com.oauth.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<AgentDto> create(@RequestBody AgentDto agentDto){
        AgentDto agentDto1 = this.agentService.createAgent(agentDto);
        return new ResponseEntity<AgentDto>(agentDto1, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDto> getById(@PathVariable Integer id){
        AgentDto agentDto = this.agentService.getById(id);
        return new ResponseEntity<AgentDto>(agentDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AgentDto>> getAll(){
        List<AgentDto> agents = this.agentService.getAllAgent();
        return new ResponseEntity<List<AgentDto>>(agents, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        this.agentService.deleteAgent(id);
        return  ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentDto> update(@RequestBody AgentDto agentDto, @PathVariable Integer id){
        AgentDto agentDto1 = this.agentService.updateAgent(agentDto, id);
        return new ResponseEntity<AgentDto>(agentDto1, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto request) {

        // 1. Authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load user
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // 3. Get role from agent
        Agent agent = agentRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Agent","Agent Id",0));

        String role = agent.getRole().getName();

        // 4. Generate token
        String token = jwtTokenHelper.generateToken(userDetails, role);

        // 5. Return response
        return ResponseEntity.ok(new JwtAuthResponse(token, role));
    }



}
