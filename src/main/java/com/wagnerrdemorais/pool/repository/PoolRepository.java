package com.wagnerrdemorais.pool.repository;

import com.wagnerrdemorais.pool.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolRepository extends JpaRepository<Poll, Long> {
}
