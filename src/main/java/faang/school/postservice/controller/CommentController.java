package faang.school.postservice.controller;

import faang.school.postservice.dto.CommentDto;
import faang.school.postservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/createComment")
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @PutMapping("/updateComment")
    public CommentDto updateComment(@RequestBody CommentDto commentDto) {
        return commentService.updateComment(commentDto);
    }

    @GetMapping("/getCommentByPostId/{postId}")
    public List<CommentDto> getCommentByPostId(@PathVariable Long postId) {
        return commentService.getCommentByPostId(postId);
    }

    @DeleteMapping("/deleteComment")
    public void deleteComment(@RequestBody CommentDto commentDto) {
        commentService.deleteComment(commentDto);
    }
}
