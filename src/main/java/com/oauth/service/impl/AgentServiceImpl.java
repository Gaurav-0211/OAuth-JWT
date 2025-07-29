package com.oauth.service.impl;

import com.oauth.dto.AgentDto;
import com.oauth.entity.Agent;
import com.oauth.exception.ResourceNotFoundException;
import com.oauth.repo.AgentRepo;
import com.oauth.service.AgentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepo agentRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public AgentDto createAgent(AgentDto agentDto) {
        if(agentRepo.existByEmail(agentDto.getAgentEmail())){
            throw new RuntimeException("Agent Already exist");
        }
        Agent agent = this.mapper.map(agentDto, Agent.class);
        this.agentRepo.save(agent);
        return this.mapper.map(agent,AgentDto.class);
    }

    @Override
    public AgentDto getById(Integer id) {
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Agent","Agent Id", id));
        return this.mapper.map(agent, AgentDto.class);
    }

    @Override
    public List<AgentDto> getAllAgent() {
        List<Agent> agents = this.agentRepo.findAll();
        return agents.stream().map((agent)-> this.mapper.map(agent,AgentDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteAgent(Integer id) {
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Agent","Agent Id", id));
        agentRepo.delete(agent);
    }

    @Override
    public AgentDto updateAgent(AgentDto agentDto, Integer id) {
        return null;
    }
}
