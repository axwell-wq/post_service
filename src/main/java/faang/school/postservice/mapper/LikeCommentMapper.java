package faang.school.postservice.mapper;

import faang.school.postservice.dto.LikeCommentDto;
import faang.school.postservice.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LikeCommentMapper {

    @Mapping(source = "commentId", target = "comment.id")
    Like toEntity(LikeCommentDto likeCommentDto);

    @Mapping(source = "comment.id", target = "commentId")
    LikeCommentDto toDto(Like like);
}
