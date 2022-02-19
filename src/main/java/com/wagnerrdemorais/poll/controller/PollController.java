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

    /**
     * Put endpoint, receives a PollForm, updates it if found,
     * and returns the updated Dto, else, responds with badRequest
     * @param poll PollForm
     * @return ResponseEntity<PollDto>
     */
    @PutMapping("/update")
    public ResponseEntity<PollDto> updatePoll(@RequestBody PollForm poll) {
        if (poll.getId() == null || !pollService.pollExistsById(poll.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Poll updatedPoll = pollService.savePoll(poll.toEntity());
        PollDto pollDto = PollDto.fromEntity(updatedPoll);
        return ResponseEntity.ok(pollDto);
    }

    /**
     * Delete endpoint, receives a Poll id, removes it if found,
     * else, responds with badRequest
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

}
