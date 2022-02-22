package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.VoteForm;
import com.wagnerrdemorais.poll.dto.PollDto;
import com.wagnerrdemorais.poll.dto.PollVotesDto;
import com.wagnerrdemorais.poll.dto.VoteDto;
import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.Vote;
import com.wagnerrdemorais.poll.service.PollService;
import com.wagnerrdemorais.poll.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Rest Controller for voting endpoints
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;
    private final PollService pollService;

    /**
     * All Args Constructor
     *
     * @param voteService VoteService
     * @param pollService PollService
     */
    public VoteController(VoteService voteService, PollService pollService) {
        this.voteService = voteService;
        this.pollService = pollService;
    }

    /**
     * Receives an optionId and opinion to generate a new vote
     *
     * @param voteForm VoteForm
     * @return ResponseEntity<VoteDto>
     */
    @PostMapping("/new")
    public ResponseEntity<VoteDto> vote(@RequestBody VoteForm voteForm) {
        Vote vote = voteService.saveVote(voteForm);
        VoteDto voteDto = new VoteDto(vote.getId(), vote.getOpinion(), vote.getPollOption().getId());
        return ResponseEntity.ok(voteDto);
    }

    /**
     * Return PollVotesDto containing the voteCount and opinions for each option
     *
     * @param pollId Long
     * @return ResponseEntity<PollDto>
     */
    @GetMapping("/getVotes")
    public ResponseEntity<PollVotesDto> getPollVotes(Long pollId) {
        if (pollId == null || !pollService.pollExistsById(pollId)) {
            return ResponseEntity.badRequest().build();
        }

        Poll poll = pollService.getPollById(pollId);
        PollDto pollDto = PollDto.fromEntity(poll);
        PollVotesDto pollVotesDto = createPollVotesDto(pollDto);

        return ResponseEntity.ok(pollVotesDto);
    }

    /**
     * Creates PollVotesDto for given PollDto
     *
     * @param pollDto PollDto
     * @return PollVotesDto
     */
    private PollVotesDto createPollVotesDto(PollDto pollDto) {
        PollVotesDto pollVotesDto = new PollVotesDto();
        pollVotesDto.setPollTitle(pollDto.getTitle());
        pollVotesDto.setPollDescription(pollDto.getDescription());
        pollVotesDto.setOptionVotes(generateVoteCount(pollDto));
        return pollVotesDto;
    }

    /**
     * Creates a List of optionVotesDto containing the vote count and opinions for each option of the poll
     *
     * @param pollDto PollDto
     * @return List<PollVotesDto.OptionVotesDto>
     */
    private List<PollVotesDto.OptionVotesDto> generateVoteCount(PollDto pollDto) {
        List<PollVotesDto.OptionVotesDto> optionVotesDtoList = new ArrayList<>();

        pollDto.getOptionList().forEach(opt -> {
            PollVotesDto.OptionVotesDto optionVotesDto = new PollVotesDto.OptionVotesDto();
            optionVotesDto.setOptionTitle(opt.getTitle());
            optionVotesDto.setVoteCount(opt.getVoteList().size());

            optionVotesDto.setOpinions(opt.getVoteList().stream()
                    .map(VoteDto::getOpinion)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

            optionVotesDtoList.add(optionVotesDto);
        });

        return optionVotesDtoList;
    }
}
