package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.PollOptionForm;
import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import com.wagnerrdemorais.poll.repository.PollRepository;
import com.wagnerrdemorais.poll.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to handle Poll operations
 */
@Service
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    /**
     * All args constructor
     *
     * @param pollRepository   PollRepository
     * @param optionRepository OptionRepository
     * @param userRepository
     */
    public PollService(PollRepository pollRepository, OptionRepository optionRepository, UserRepository userRepository) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all Poll objects from database
     *
     * @return List<Poll>
     */
    public List<Poll> getPollList() {
        return this.pollRepository.findAll();
    }

    /**
     * Return a Poll for given ID
     *
     * @param id Long
     * @return Poll
     */
    public Poll getPollById(Long id) {
        return this.pollRepository.getById(id);
    }

    /**
     * Save given Poll object returning the saved object
     *
     * @param poll Poll
     * @return Poll
     */
    public Poll savePoll(Poll poll) {
        return this.pollRepository.save(poll);
    }

    /**
     * Convert given PollForm to Poll Object and saves it, returning the saved object
     *
     * @param pollForm PollForm
     * @return Poll
     */
    public Poll savePoll(PollForm pollForm) {
        Poll poll = getPollFromFormUpdatingOptions(pollForm);
        return this.savePoll(poll);
    }

    /**
     * Delete Poll by its Id
     *
     * @param id Long
     */
    public void deleteById(Long id) {
        this.pollRepository.deleteById(id);
    }

    /**
     * Check if Poll exists in database
     *
     * @param id Long
     * @return boolean
     */
    public boolean pollExistsById(Long id) {
        return this.pollRepository.existsById(id);
    }

    /**
     * Return a Poll from given PollForm
     *
     * @param pollForm PollForm
     * @return Poll
     */
    private Poll getPollFromFormUpdatingOptions(PollForm pollForm) {
        Poll poll;

        if (pollForm.getId() == null) {
            poll = new Poll();
        } else {
            poll = this.getPollById(pollForm.getId());
        }

        poll.setTitle(pollForm.getTitle());
        poll.setDescription(pollForm.getDescription());

        if (pollForm.getUserId() != null) {
            User user = userRepository.getById(pollForm.getUserId());
            poll.setUser(user);
        }

        List<PollOption> optionList = updatePollOptions(pollForm.getOptionList(), poll);
        poll.setOptionList(optionList);
        return poll;
    }

    public boolean hasUser(Long pollId) {
        Poll poll = pollRepository.getById(pollId);
        return poll.getUser() != null;
    }

    public List<Poll> findAllByUserId(Long userId) {
        return pollRepository.findAllByUserId(userId);
    }

    public List<Poll> findAllByIdInAndUserId(List<Long> ids, Long userId) {
        return pollRepository.findAllByIdInAndUserId(ids, userId);
    }

    /**
     * For given List<PollOptionForm>, converts to PollOption saves all options, returning all saved PollOption
     *
     * @param optionFormList List<PollOptionForm>
     * @param poll           Poll
     * @return List<PollOption>
     */
    private List<PollOption> updatePollOptions(List<PollOptionForm> optionFormList, Poll poll) {
        return optionFormList.stream()
                .map(opt -> {
                    PollOption pollOption;
                    if (opt.getId() == null) {
                        pollOption = new PollOption(null, opt.getTitle(), new ArrayList<>());
                        pollOption.setPoll(poll);
                    } else {
                        pollOption = optionRepository.getById(opt.getId());
                        pollOption.setTitle(opt.getTitle());
                    }
                    return optionRepository.save(pollOption);
                }).collect(Collectors.toList());
    }

    public List<Poll> saveAll(List<Poll> pollList) {
        return pollRepository.saveAll(pollList);
    }
}
