package ru.practicum.explore.rest.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.request.RequestOutput;
import ru.practicum.explore.service.requestService.RequestsService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestRestHandler {
    private final RequestsService requestsService;

    @GetMapping
    public List<RequestOutput> getUserRequests(@PathVariable long userId) {
        return requestsService.getUserRequests(userId);
    }

    @PostMapping
    public ResponseEntity<RequestOutput> createRequest(@PathVariable long userId, @RequestParam long eventId) {
        return new ResponseEntity<>(requestsService.saveRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestOutput cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        return requestsService.cancelRequest(userId, requestId);
    }
}
