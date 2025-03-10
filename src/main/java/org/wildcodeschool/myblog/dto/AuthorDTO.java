package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.wildcodeschool.myblog.validation.OnCreate;
import org.wildcodeschool.myblog.validation.OnUpdate;

import java.util.List;

public class AuthorDTO {
    @NotNull(groups = OnUpdate.class, message = "ID is required for update")
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Firstname must not be empty")
    @Size(min = 2, max = 50, message = "The firstname must contain between 2 and 50 characters")
    private String firstname;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Lastname must not be empty")
    @Size(min = 2, max = 50, message = "The lastname must contain between 2 and 50 characters")
    private String lastname;

    private List<ArticleAuthorDTO> articleAuthorDTOs;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public List<ArticleAuthorDTO> getArticleAuthorDTOs() { return articleAuthorDTOs; }
    public void setArticleAuthorDTOs(List<ArticleAuthorDTO> articleAuthorDTOs) { this.articleAuthorDTOs = articleAuthorDTOs; }
}
