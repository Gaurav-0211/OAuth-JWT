package com.oauth.repo;

import com.oauth.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepo extends JpaRepository<Agent, Integer> {
    boolean existsByEmail(String email);
    Optional<Agent> findByEmail(String email);
}
