package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.VoteForm;
import com.wagnerrdemorais.poll.dto.VoteDto;
import com.wagnerrdemorais.poll.model.Vote;
import com.wagnerrdemorais.poll.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

    private List<VoteDto> voteToDtoList(List<Vote> votes) {
        return votes.stream().map(vote -> new VoteDto(vote.getId(),
                        vote.getOpinion(),
                        vote.getPollOption().getId()))
                .collect(Collectors.toList());
    }
}
