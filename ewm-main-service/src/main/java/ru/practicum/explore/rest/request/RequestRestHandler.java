package ru.practicum.explore.rest.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.request.RequestOutput;
import ru.practicum.explore.service.requestService.RequestsService;

import java.util.List;

@RestController("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestRestHandler {
    private final RequestsService requestsService;

    @GetMapping
    public List<RequestOutput> getUserRequests(@PathVariable long userId) {
        return requestsService.getUserRequests(userId);
    }

    @PostMapping
    public RequestOutput createRequest(@PathVariable long userId, @RequestParam long eventId) {
        return requestsService.saveRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestOutput cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        return requestsService.cancelRequest(userId, requestId);
    }
}
