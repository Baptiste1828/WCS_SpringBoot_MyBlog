package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.wildcodeschool.myblog.validation.OnCreate;
import org.wildcodeschool.myblog.validation.OnUpdate;

import java.util.List;

public class CategoryDTO {
    @NotNull(groups = OnUpdate.class, message = "ID is required for update")
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Name must not be empty")
    @Size(min = 2, max = 50, message = "The name must contain between 2 and 50 characters")
    private String name;

    private List<ArticleDTO> articles;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<ArticleDTO> getArticles() { return articles; }
    public void setArticles(List<ArticleDTO> articles) { this.articles = articles; }
}
