package com.oauth.controller;

import com.oauth.dto.AgentDto;
import com.oauth.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

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

}
