package com.oauth.service.impl;

import com.oauth.config.AppConstants;
import com.oauth.dto.AgentDto;
import com.oauth.entity.Agent;
import com.oauth.entity.Role;
import com.oauth.exception.ResourceNotFoundException;
import com.oauth.repo.AgentRepo;
import com.oauth.repo.RoleRepo;
import com.oauth.service.AgentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService, UserDetailsService {

    @Autowired
    private AgentRepo agentRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public AgentDto createAgent(AgentDto agentDto) {
        if(this.agentRepo.existsByEmail(agentDto.getEmail())){
            throw new RuntimeException("Agent Already exist");
        }
        Agent agent = this.mapper.map(agentDto, Agent.class);
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        Role role = this.roleRepo.findById(AppConstants.NORMAL)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "ID", AppConstants.NORMAL));
        agent.setRole(role);
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
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Agent", "Agent Id", id));
        agent.setEmail(agentDto.getEmail());
        agent.setAgentName(agentDto.getAgentName());
        agent.setPassword(passwordEncoder.encode(agentDto.getPassword()));
        agent.setAgentCity(agentDto.getAgentCity());
        agent.setAgentState(agentDto.getAgentState());
        this.agentRepo.save(agent);
        return this.mapper.map(agent, AgentDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Agent agent = agentRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Agent","Agent Id"+username,0));

        return new org.springframework.security.core.userdetails.User(
                agent.getEmail(),
                agent.getPassword(),
                List.of(new SimpleGrantedAuthority(agent.getRole().getName()))
        );
    }
}
