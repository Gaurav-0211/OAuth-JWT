package com.oauth.service.impl;

import com.oauth.dto.AgentDto;
import com.oauth.dto.AgentResponse;
import com.oauth.entity.Agent;
import com.oauth.entity.Role;
import com.oauth.exception.NoUserExist;
import com.oauth.exception.UserAlreadyExist;
import com.oauth.repo.AgentRepo;
import com.oauth.repo.RoleRepo;
import com.oauth.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
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

    // Register a new agent
    @Override
    public AgentDto createAgent(AgentDto agentDto) {
        log.info("Agent register api in Impl triggered");
        if(this.agentRepo.existsByEmail(agentDto.getEmail())){
            throw new UserAlreadyExist("Agent Already exist");
        }
        Agent agent = this.mapper.map(agentDto, Agent.class);
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));

        Role role = roleRepo.findById(agentDto.getRoleId()).orElseThrow(() -> new NoUserExist("Role Not found"));
        agent.setRole(role);

        this.agentRepo.save(agent);
        log.info("Agent register api in Impl executed");
        return this.mapper.map(agent,AgentDto.class);
    }

    // get a single agent by its id
    @Override
    public AgentDto getById(Integer id) {
        log.info("Get Agent By Id api in Service Impl triggered");
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()-> new NoUserExist("No User exist with the given Id"));
        log.info("Get user by Id service Impl executed");
        return this.mapper.map(agent, AgentDto.class);
    }

    // Get all agent details
    @Override
    public List<AgentDto> getAllAgent() {
        log.info("Get all agent service Impl triggered");
        List<Agent> agents = this.agentRepo.findAll();
        log.info("Get all agent service impl executed");
        return agents.stream().map((agent)-> this.mapper.map(agent,AgentDto.class)).collect(Collectors.toList());
    }

    // Delete an agent from the list
    @Override
    public void deleteAgent(Integer id) {
        log.info("Delete agent in service impl triggered");
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()->new NoUserExist("No Agent Exist with the given Id"));
        agentRepo.delete(agent);
        log.info("Delete agent in service Impl executed");
    }

    // Update agent details
    @Override
    public AgentDto updateAgent(AgentDto agentDto, Integer id) {
        log.info("Update Agent in service impl triggered");
        Agent agent = this.agentRepo.findById(id)
                .orElseThrow(()-> new NoUserExist("No Agent exist with the given Id"));
        agent.setEmail(agentDto.getEmail());
        agent.setAgentName(agentDto.getAgentName());
        agent.setPassword(passwordEncoder.encode(agentDto.getPassword()));
        agent.setAgentCity(agentDto.getAgentCity());
        agent.setAgentState(agentDto.getAgentState());
        this.agentRepo.save(agent);
        log.info("Update user in service impl executed");
        return this.mapper.map(agent, AgentDto.class);
    }

    // Get all Agents BY state name
    @Override
    public List<AgentDto> getByState(String stateName) {
        log.info("Get Agent by state in service impl triggered");
        List<Agent> agents = agentRepo.findByState(stateName);
        if(agents == null){
            throw new NoUserExist("No state exist with the given name");
        }
        log.info("Get all Agent by state in service impl executed");
        return agents.stream().map((agent)-> this.mapper.map(agent,AgentDto.class)).collect(Collectors.toList());
    }

    // Get all agents by city name
    @Override
    public List<AgentDto> getByCity(String cityName) {
        log.info("Get all Agent by city in service impl triggered");
        List<Agent> agents = agentRepo.findByCity(cityName);
        if(agents == null){
            throw new NoUserExist("No agent found with the given city");
        }
        log.info("Get all agent by city in service impl executed");
        return agents.stream().map((agent)-> this.mapper.map(agent, AgentDto.class)).collect(Collectors.toList());
    }

    @Override
    public AgentResponse getAllAgentByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Get all agent in pages service impl triggered");
        Sort sort = sortDir != null && sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Agent> agentPage = agentRepo.findAll(pageable);

        List<AgentDto> agentDtos = agentPage
                .getContent()
                .stream()
                .map(agent -> mapper.map(agent, AgentDto.class))
                .collect(Collectors.toList());

        log.info("Get all agent by pages in service impl executed");
        return AgentResponse.builder()
                .content(agentDtos)
                .pageNumber(agentPage.getNumber())
                .pageSize(agentPage.getSize())
                .totalElements(agentPage.getTotalElements())
                .totalPage(agentPage.getTotalPages())
                .lastPage(agentPage.isLast())
                .build();
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
