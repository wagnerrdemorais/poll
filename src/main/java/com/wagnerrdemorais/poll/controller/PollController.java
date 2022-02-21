package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.dto.PollDto;
import com.wagnerrdemorais.poll.dto.VotePageDto;
import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.service.PollService;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest Controller containing endpoints to enable adding, editing and removing Polls
 */
@RestController
@RequestMapping("/poll")
public class PollController {

    private final PollService pollService;

    /**
     * Constructor receiving an instance of PollService
     *
     * @param pollService PollService
     */
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    /**
     * Get endpoint, returns a List of PollDto
     *
     * @return ResponseEntity<List < Poll>>
     */
    @GetMapping("/list")
    public ResponseEntity<List<PollDto>> getPollList() {
        List<Poll> pollList = pollService.getPollList();
        List<PollDto> pollDtos = pollList.stream()
                .map(PollDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(pollDtos);
    }

    /**
     * Post endpoint, receives a PollForm object, saves it and returns
     * the saved object Dto.
     *
     * @param pollForm PollForm
     * @return ResponseEntity<List<VotePageDto.PollOptionLink>>
     */
    @PostMapping("/add")
    public ResponseEntity<List<VotePageDto.PollOptionLink>> addPoll(@RequestBody PollForm pollForm) {
        Poll savedPoll = pollService.savePoll(pollForm);

        List<VotePageDto.PollOptionLink> pollOptionLinks = generateVoteLink(savedPoll);
        return ResponseEntity.ok(pollOptionLinks);
    }

    /**
     * Put endpoint, receives a PollForm, updates it if found,
     * and returns the updated Dto, else, responds with badRequest
     *
     * @param pollForm PollForm
     * @return ResponseEntity<PollDto>
     */
    @PutMapping("/update")
    public ResponseEntity<PollDto> updatePoll(@RequestBody PollForm pollForm) {
        if (pollForm.getId() == null || !pollService.pollExistsById(pollForm.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Poll updatedPoll = pollService.savePoll(pollForm);
        PollDto pollDto = PollDto.fromEntity(updatedPoll);
        return ResponseEntity.ok(pollDto);
    }

    /**
     * Delete endpoint, receives a Poll id, removes it if found,
     * else, responds with badRequest
     *
     * @param id Long
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePollById(Long id) {
        if (id == null || !pollService.pollExistsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        pollService.deleteById(id);
        return ResponseEntity.ok("Poll deleted!");
    }

    /**
     * Get endpoint, receives a Poll id, return PollDto if found,
     * else, responds with badRequest
     *
     * @param id Long
     * @return ResponseEntity<PollDto>
     */
    @GetMapping("/get")
    public ResponseEntity<PollDto> getPollById(Long id) {
        if (id == null || !pollService.pollExistsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        PollDto pollDto = PollDto.fromEntity(pollService.getPollById(id));
        return ResponseEntity.ok(pollDto);
    }

    /**
     * Given a Poll, generates a List of links for voting
     * @param savedPoll Poll
     * @return List<VotePageDto.PollOptionLink>
     */
    private List<VotePageDto.PollOptionLink> generateVoteLink(Poll savedPoll) {
        return savedPoll.getOptionList().stream()
                .map(opt -> {
                    String href = WebMvcLinkBuilder
                            .linkTo(VoteController.class)
                            .slash("new?optId=" + opt.getId()+"&opinion=")
                            .withSelfRel().getHref();
                    return new VotePageDto.PollOptionLink(opt.getTitle(), href);

                }).collect(Collectors.toList());
    }

}
