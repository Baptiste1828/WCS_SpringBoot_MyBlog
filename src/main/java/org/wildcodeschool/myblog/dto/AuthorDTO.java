package org.wildcodeschool.myblog.dto;

import java.util.List;

public class AuthorDTO {
    private Long id;
    private String firstname;
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
