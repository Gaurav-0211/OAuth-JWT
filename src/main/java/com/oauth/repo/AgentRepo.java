package com.oauth.repo;

import com.oauth.dto.AgentDto;
import com.oauth.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface AgentRepo extends JpaRepository<Agent, Integer> {

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Agent u WHERE u.email = :email")
    Agent findByEmail(String email);

    @Query("SELECT u FROM Agent u WHERE u.agentState = :state")
    List<Agent> findByState(String state);

    @Query("SELECT u FROM Agent u WHERE u.agentCity = :city")
    List<Agent> findByCity(String city);

    @Query("SELECT u FROM Agent u WHERE u.agentName LIKE CONCAT(:name, '%')")
    List<Agent> findByAgentNameStartWith(String name);

    @Query("SELECT u FROM Agent u WHERE u.agentName LIKE CONCAT('%',:name)")
    List<Agent> findByAgentNameEndWith(String name);

    @Query("SELECT u FROM Agent u ORDER BY agentName Desc")
    List<Agent> findByAgentNameDesc();

}
