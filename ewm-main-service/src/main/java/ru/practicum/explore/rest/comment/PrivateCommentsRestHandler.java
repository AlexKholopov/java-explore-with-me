package ru.practicum.explore.rest.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.comments.CommentInput;
import ru.practicum.explore.model.comments.CommentOutput;
import ru.practicum.explore.service.commentsService.CommentsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
public class PrivateCommentsRestHandler {

    private final CommentsService commentsService;


    @PostMapping
    public ResponseEntity<CommentOutput> addComment(@PathVariable Long eventId, @PathVariable Long userId,
                                                    @Valid @RequestBody CommentInput commentInput) {
        return new ResponseEntity<>(commentsService.addComment(commentInput, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public CommentOutput updateComment(@PathVariable Long commentId,
                                       @PathVariable Long userId,
                                       @Valid @RequestBody CommentInput commentInput) {
        return commentsService.updateComment(commentInput, userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        commentsService.deleteComment(commentId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
