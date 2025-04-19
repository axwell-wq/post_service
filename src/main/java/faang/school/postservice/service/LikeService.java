package faang.school.postservice.service;

import faang.school.postservice.dto.LikeCommentDto;
import faang.school.postservice.dto.LikeDto;
import faang.school.postservice.dto.LikePostDto;
import faang.school.postservice.mapper.LikePostMapper;

public interface LikeService {
    void likePost(LikePostDto likeDto);

    void deleteLikePost(LikeDto likeDto);

    void likeComment(LikeCommentDto likeDto);

    void deleteLikeComment(LikeDto likeDto);
}
