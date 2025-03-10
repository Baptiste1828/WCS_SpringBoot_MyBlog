package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ArticleAuthorDTO {

    private Long id;

    @NotNull(message = "Article ID must not be null")
    @Positive(message = "Article ID must be a positive number")
    private Long articleId;

    @NotNull(message = "Author ID must not be null")
    @Positive(message = "Author ID must be a positive number")
    private Long authorId;

    @NotBlank(message = "Author contribution must not be empty")
    private String contribution;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getContribution() { return contribution; }
    public void setContribution(String contribution) { this.contribution = contribution; }
}