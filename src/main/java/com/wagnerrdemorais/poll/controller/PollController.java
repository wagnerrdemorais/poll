package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.dto.PollDto;
import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.service.PollService;
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
     * @param pollService PollService
     */
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    /**
     * Get endpoint, returns a List of PollDto
     * @return ResponseEntity<List<Poll>>
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
     * @param poll PollForm
     * @return ResponseEntity<PollDto>
     */
    @PostMapping("/add")
    public ResponseEntity<PollDto> addPoll(@RequestBody PollForm poll) {
        Poll savedPoll = pollService.savePoll(poll.toEntity());
        PollDto pollDto = PollDto.fromEntity(savedPoll);
        return ResponseEntity.ok(pollDto);
    }

}
