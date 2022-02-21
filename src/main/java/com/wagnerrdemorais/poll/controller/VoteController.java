package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.VoteForm;
import com.wagnerrdemorais.poll.dto.VoteDto;
import com.wagnerrdemorais.poll.model.Vote;
import com.wagnerrdemorais.poll.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/newVote")
    public ResponseEntity<VoteDto> newVote(@RequestBody VoteForm voteForm) {
        Vote vote = voteService.saveVote(voteForm);
        VoteDto voteDto = new VoteDto(vote.getId(), vote.getOpinion(), vote.getPollOption().getId());
        return ResponseEntity.ok(voteDto);
    }

    @GetMapping("/new")
    public ResponseEntity<VoteDto> vote(String optId, String opinion) {
        VoteForm voteForm = new VoteForm(Long.valueOf(optId), opinion);
        Vote vote = voteService.saveVote(voteForm);
        VoteDto voteDto = new VoteDto(vote.getId(), vote.getOpinion(), vote.getPollOption().getId());
        return ResponseEntity.ok(voteDto);
    }
}
