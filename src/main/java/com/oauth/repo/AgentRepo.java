package com.oauth.repo;

import com.oauth.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AgentRepo extends JpaRepository<Agent, Integer> {

    boolean existsByEmail(String email);

    Optional<Agent> findByEmail(String email);

    @Query("SELECT u FROM Agent u WHERE u.agentState = :state")
    List<Agent> findByState(String state);

    @Query("SELECT u FROM Agent u WHERE u.agentCity = :city")
    List<Agent> findByCity(String city);
}
