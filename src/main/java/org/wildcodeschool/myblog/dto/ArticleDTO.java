package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDTO {
    @NotNull(message = "Item ID cannot be null")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 50, message = "The title must contain between 2 and 50 characters")
    private String title;

    @NotBlank(message = "Content must not be empty")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;

    @PastOrPresent(message = "The update date cannot be in the future")
    private LocalDateTime updatedAt;

    private String categoryName;

    private List<String> imageUrls;

    @NotEmpty(message = "An article must have at least one author")
    private List<ArticleAuthorDTO> articleAuthorDTOs;

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public List<ArticleAuthorDTO> getArticleAuthorDTOs() { return articleAuthorDTOs; }
    public void setArticleAuthorDTOs(List<ArticleAuthorDTO> articleAuthorDTOs) { this.articleAuthorDTOs = articleAuthorDTOs; }
}
