package faang.school.postservice.service;

import faang.school.postservice.dto.LikeDto;

public interface LikeService {
    void likePost(LikeDto likeDto);

    void deleteLikePost(LikeDto likeDto);

    void likeComment(LikeDto likeDto);

    void deleteLikeComment(LikeDto likeDto);
}
