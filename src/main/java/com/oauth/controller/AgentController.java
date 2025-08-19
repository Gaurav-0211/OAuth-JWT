package com.oauth.controller;

import com.oauth.config.AppConstants;
import com.oauth.dto.*;
import com.oauth.entity.Agent;
import com.oauth.exception.NoUserExist;
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

    // Api to Register a new user or deleted account can re- register from here
    @PostMapping("/register")
    public ResponseEntity<Response> create(@RequestBody @Valid AgentDto agentDto){
            AgentDto agentDto1 = this.agentService.createAgent(agentDto);
            Response response = Response.buildResponse(
            "success",
                    "Agent Registered Successfully",
                    201,
                    agentDto1,
                    "Process Executed Successfully"
            );
            return ResponseEntity.ok(response);
    }

    // Get Agent by id api
    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Integer id)
    {
        AgentDto agentDto = this.agentService.getById(id);
        Response response = Response.buildResponse(
                "success",
                "Agent Fetched Successful",
                200,
                agentDto,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }

    // Get all Agents stored in db
    @GetMapping("/all")
    public ResponseEntity<Response> getAll(){
        List<AgentDto> agents = this.agentService.getAllAgent();
        Response response = Response.buildResponse(
                "success",
                "Agent Fetched Successful",
                200,
                agents,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }

    // Delete an existing agent from record
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Integer id) {
        this.agentService.deleteAgent(id);
        Response response = Response.buildResponse(
                "success",
                "Agent Deleted Successful",
                200,
                null,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }

    // Get all Agent by state
    @GetMapping("/state")
    public ResponseEntity<Response> getAgentsByState(@RequestParam String state){
       List<AgentDto> agents =  this.agentService.getByState(state);
        Response response = Response.buildResponse(
                "SUCCESS",
                "All Agent Fetched Successfully",
                HttpStatus.OK.value(),
                agents,
                "Execution success"
        );
        return ResponseEntity.ok(response);
    }

    // Get all Agent by city
    @GetMapping("/city")
    public ResponseEntity<Response> getAgentsByCity(@RequestParam String city){
        List<AgentDto> agents = this.agentService.getByCity(city);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Agent fetched success",
                HttpStatus.OK.value(),
                agents,
                "Executed success"
                );
        return ResponseEntity.ok(response);
    }


    // Update an Agent details after register
    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@RequestBody AgentDto agentDto, @PathVariable Integer id){
        AgentDto agentDto1 = this.agentService.updateAgent(agentDto, id);
        Response response = Response.buildResponse(
                "success",
                "Agent Updated Successful",
                200,
                agentDto1,
                "Executed Successfully"
        );
        return ResponseEntity.ok(response);
    }

    // Get agents in pages like in one page limited user will be visible rather than entier database record
    @GetMapping("/getAll-byPage")
    public ResponseEntity<Response> getAllAgentsByPage(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        AgentResponse agents = this.agentService.getAllAgentByPage(pageNumber, pageSize, sortBy, sortDir);
        Response response;
        if(agents == null){
             response = Response.buildResponse(
                    "FAILED",
                    "No Agent Fetched",
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    "Execution success"
            );
        }else{
             response = Response.buildResponse(
                    "Success",
                    "All Agent Fetched Success",
                    HttpStatus.OK.value(),
                    agents,
                    "Executed success"
            );
        }
        return ResponseEntity.ok(response);
    }


    // Login Agent to get the token for getting api based on their role
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
                .orElseThrow(() -> new NoUserExist("Agent not found"));

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
