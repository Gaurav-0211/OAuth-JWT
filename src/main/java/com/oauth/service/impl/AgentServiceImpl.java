package com.oauth.service.impl;

import com.oauth.dto.AgentDto;
import com.oauth.entity.Agent;
import com.oauth.entity.Role;
import com.oauth.exception.NoUserExist;
import com.oauth.exception.UserAlreadyExist;
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
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
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
            throw new UserAlreadyExist("Agent Already exist");
        }
        Agent agent = this.mapper.map(agentDto, Agent.class);
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));

        Role role = roleRepo.findById(agentDto.getRoleId()).orElseThrow(() -> new NoUserExist("Role Not found"));
        agent.setRole(role);

        this.agentRepo.save(agent);
        return this.mapper.map(agent,AgentDto.class);
    }

    @Override
    public AgentDto getById(Integer id) {
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()-> new NoUserExist("No User exist with the given Id"));
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
                .orElseThrow(()->new NoUserExist("No Agent Exist with the given Id"));
        agentRepo.delete(agent);
    }

    @Override
    public AgentDto updateAgent(AgentDto agentDto, Integer id) {
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()-> new NoUserExist("No Agent exist with the given Id"));
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
                .orElseThrow(() -> new NoUserExist("No User exist with the given name"));

        String roleName = "ROLE_" + agent.getRole().getName().name(); // Properly formatted

        return new org.springframework.security.core.userdetails.User(
                agent.getEmail(),
                agent.getPassword(),
                List.of(new SimpleGrantedAuthority(roleName))
        );
    }

}
