package com.wagnerrdemorais.poll.repository;

import com.wagnerrdemorais.poll.model.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<PollOption, Long> {
}
