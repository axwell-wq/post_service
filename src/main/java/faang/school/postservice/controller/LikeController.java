package faang.school.postservice.controller;

import faang.school.postservice.dto.LikeCommentDto;
import faang.school.postservice.dto.LikeDto;
import faang.school.postservice.dto.LikePostDto;
import faang.school.postservice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likePost")
    public void likePost(@RequestBody LikePostDto likeDto) {
        likeService.likePost(likeDto);
    }

    @DeleteMapping("/deleteLikePost")
    public void deleteLikePost(@RequestBody LikeDto likeDto) {
        likeService.deleteLikePost(likeDto);
    }

    @PostMapping("/likeComment")
    public void likeComment(@RequestBody LikeCommentDto likeDto) {
        likeService.likeComment(likeDto);
    }

    @DeleteMapping("/deleteLikeComment")
    public void deleteLikeComment(@RequestBody LikeDto likeDto) {
        likeService.deleteLikeComment(likeDto);
    }
}
