package com.oauth.service;

import com.oauth.dto.AgentDto;
import com.oauth.dto.AgentResponse;

import java.util.List;

public interface AgentService {
    AgentDto createAgent(AgentDto agentDto);

    AgentDto getById(Integer id);

    List<AgentDto> getAllAgent();

    void deleteAgent(Integer id);

    AgentDto updateAgent(AgentDto agentDto, Integer id);

    List<AgentDto> getByState(String stateName);

    List<AgentDto> getByCity(String cityName);

    AgentResponse getAllAgentByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

}
