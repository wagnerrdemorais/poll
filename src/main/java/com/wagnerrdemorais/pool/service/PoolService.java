package com.wagnerrdemorais.pool.service;

import com.wagnerrdemorais.pool.model.Poll;
import com.wagnerrdemorais.pool.repository.PoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoolService {

    private final PoolRepository poolRepository;

    public PoolService(PoolRepository poolRepository) {
        this.poolRepository = poolRepository;
    }

    public List<Poll> getPoolList() {
        return this.poolRepository.findAll();
    }

    public Poll getPoolById(Long id) {
        return this.poolRepository.getById(id);
    }

    public Poll savePoll(Poll poll) {
        return this.poolRepository.save(poll);
    }

    public void deletePoll(Poll poll) {
        this.poolRepository.delete(poll);
    }

}
