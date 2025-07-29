package com.oauth.controller;

import com.oauth.dto.AgentDto;
import com.oauth.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    public ResponseEntity<AgentDto> create(@RequestBody AgentDto agentDto){
        return null;
    }

}
