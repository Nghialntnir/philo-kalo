package com.nlnt.philokalo_server.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nlnt.philokalo_server.config.GlobalMapperConfig;
import com.nlnt.philokalo_server.dto.request.CategoryRequest;
import com.nlnt.philokalo_server.dto.response.CategoryResponse;
import com.nlnt.philokalo_server.dto.response.CategorySimpleResponse;
import com.nlnt.philokalo_server.model.Category;

@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "artworkSet", ignore = true)
    @Mapping(target = "categorySet", ignore = true)
    Category toCategory(CategoryRequest request);

    @Mapping(target = "parent", expression = "java(mapSimpleCategory(category.getParent()))")
    @Mapping(target = "children", expression = "java(mapChildren(category))")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "artworkSet", ignore = true)
    @Mapping(target = "categorySet", ignore = true)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);

    default CategorySimpleResponse mapSimpleCategory(Category category) {
        if (category == null) {
            return null;
        }
        return CategorySimpleResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }

    default Set<CategorySimpleResponse> mapChildren(Category category) {
        if (category.getCategorySet() == null) {
            return new HashSet<>();
        }
        return category.getCategorySet().stream()
                .map(this::mapSimpleCategory)
                .collect(Collectors.toSet());
    }
}
