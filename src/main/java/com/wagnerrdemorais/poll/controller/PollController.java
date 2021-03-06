package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.dto.PollDto;
import com.wagnerrdemorais.poll.dto.VotePageDto;
import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.service.PollService;
import com.wagnerrdemorais.poll.service.UserService;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest Controller containing endpoints to enable adding, editing and removing Polls
 */
@RestController
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;
    private final UserService userService;

    /**
     * Constructor receiving an instance of PollService
     *
     * @param pollService PollService
     * @param userService UserService
     */
    public PollController(PollService pollService, UserService userService) {
        this.pollService = pollService;
        this.userService = userService;
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
     * @return ResponseEntity<List < VotePageDto.PollOptionLink>>
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
     * Enable the user to claim all polls without user
     * @return ResponseEntity<List<PollDto>>
     */
    @PutMapping("/claimPolls")
    public ResponseEntity<List<PollDto>> claimPolls() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user;
        if (principal instanceof User) {
            user = (User) principal;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Poll> pollList = pollService.findAllByUserId(null);

        pollList.forEach(poll -> poll.setUser(user));

        List<Poll> pollList1 = pollService.saveAll(pollList);

        List<PollDto> pollDtoList = pollList1.stream()
                .map(PollDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pollDtoList);
    }

    /**
     * Return the pollList for given authenticated user
     * @return ResponseEntity<List<PollDto>>
     */
    @GetMapping("/myPolls")
    public ResponseEntity<List<PollDto>> myPools() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Poll> pollList = pollService.findAllByUserId(user.getId());

        List<PollDto> pollDtoList = pollList.stream()
                .map(PollDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pollDtoList);
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
     *
     * @param savedPoll Poll
     * @return List<VotePageDto.PollOptionLink>
     */
    private List<VotePageDto.PollOptionLink> generateVoteLink(Poll savedPoll) {
        return savedPoll.getOptionList().stream()
                .map(opt -> {
                    String href = WebMvcLinkBuilder
                            .linkTo(VoteController.class)
                            .slash("newVote?optId=" + opt.getId() + "&opinion=")
                            .withSelfRel().getHref();
                    return new VotePageDto.PollOptionLink(opt.getTitle(), href);
                }).collect(Collectors.toList());
    }

    /**
     * Get list of polls by user id
     * @param userId Long
     * @return ResponseEntity<List<PollDto>>
     */
    @GetMapping("/listByUserId")
    public ResponseEntity<List<PollDto>> getListByUserId(Long userId) {
        if (userId == null || !userService.userExistsById(userId)) {
            return ResponseEntity.badRequest().build();
        }

        List<Poll> poolListByUserId = pollService.findAllByUserId(userId);
        List<PollDto> pollDtoList = new ArrayList<>();
        if (poolListByUserId != null) {
            pollDtoList = poolListByUserId.stream()
                    .map(PollDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(pollDtoList);
    }

    /**
     * Returns a list of polls with no user
     * @return ResponseEntity<List<PollDto>>
     */
    @GetMapping("/listWithoutUser")
    public ResponseEntity<List<PollDto>> listWithoutUser() {

        List<Poll> poolListByUserId = pollService.findAllByUserId(null);
        List<PollDto> pollDtoList = new ArrayList<>();
        if (poolListByUserId != null) {
            pollDtoList = poolListByUserId.stream()
                    .map(PollDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(pollDtoList);
    }

    /**
     * Changes given list of polls to require authentication for voting
     * @param pollIds List<Long>
     * @return ResponseEntity<List<PollDto>>
     */
    @PutMapping("/requireAuth")
    public ResponseEntity<List<PollDto>> requireAuth(@RequestBody List<Long> pollIds) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user;
        if (principal instanceof User) {
            user = (User) principal;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Poll> pollList = pollService.findAllByIdInAndUserId(pollIds, user.getId());

        pollList.forEach(poll -> poll.setRequireAuth(true));

        List<Poll> saved = pollService.saveAll(pollList);

        List<PollDto> pollDtoList = saved.stream()
                .map(PollDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pollDtoList);
    }

}
