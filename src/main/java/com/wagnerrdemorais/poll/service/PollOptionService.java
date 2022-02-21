package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import org.springframework.stereotype.Service;

/**
 * Service to handle PollOption operations
 */
@Service
public class PollOptionService {

    private final OptionRepository optionRepository;

    /**
     * All args Constructor
     * @param optionRepository OptionRepository
     */
    public PollOptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * Return PollOption for given ID
     * @param id Long
     * @return PollOption
     */
    public PollOption getById(Long id) {
        return optionRepository.getById(id);
    }

    /**
     * Saves PollOption to database
     * @param pollOption PollOption
     * @return PollOption
     */
    public PollOption save(PollOption pollOption) {
        return optionRepository.save(pollOption);
    }
}
