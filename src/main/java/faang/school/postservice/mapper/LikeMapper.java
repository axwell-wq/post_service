package faang.school.postservice.mapper;

import faang.school.postservice.dto.LikeDto;
import faang.school.postservice.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "post.id", target = "postId")
    LikeDto toDto(Like like);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "commentId", target = "comment.id")
    @Mapping(source = "postId", target = "post.id")
    Like toEntity(LikeDto likeDto);
}
