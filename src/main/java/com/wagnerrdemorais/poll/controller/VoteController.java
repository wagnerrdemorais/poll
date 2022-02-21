package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.VoteForm;
import com.wagnerrdemorais.poll.dto.VoteDto;
import com.wagnerrdemorais.poll.model.Vote;
import com.wagnerrdemorais.poll.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for voting endpoints
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    /**
     * All Args Constructor
     * @param voteService VoteService
     */
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /**
     * Receives an optionId and opinion to generate a new vote
     * @param optId String
     * @param opinion String
     * @return ResponseEntity<VoteDto>
     */
    @GetMapping("/new")
    public ResponseEntity<VoteDto> vote(String optId, String opinion) {
        VoteForm voteForm = new VoteForm(Long.valueOf(optId), opinion);
        Vote vote = voteService.saveVote(voteForm);
        VoteDto voteDto = new VoteDto(vote.getId(), vote.getOpinion(), vote.getPollOption().getId());
        return ResponseEntity.ok(voteDto);
    }
}
