package com.oauth.service;

import com.oauth.dto.AgentDto;

import java.util.List;

public interface AgentService {
    AgentDto createAgent(AgentDto agentDto);
    AgentDto getById(Integer id);
    List<AgentDto> getAllAgent();
    void deleteAgent(Integer id);
    AgentDto updateAgent(AgentDto agentDto, Integer id);
}
