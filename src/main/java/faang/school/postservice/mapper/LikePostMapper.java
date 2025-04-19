package faang.school.postservice.mapper;

import faang.school.postservice.dto.LikePostDto;
import faang.school.postservice.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LikePostMapper {

    @Mapping(source = "postId", target = "post.id")
    Like toEntity(LikePostDto likeDto);

    @Mapping(source = "post.id", target = "postId")
    LikePostDto toDto(Like like);
}
