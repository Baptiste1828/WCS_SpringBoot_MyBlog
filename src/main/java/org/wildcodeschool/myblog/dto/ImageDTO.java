package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.wildcodeschool.myblog.validation.OnUpdate;

import java.util.List;

public class ImageDTO {
    @NotNull(groups = OnUpdate.class, message = "ID is required for update")
    private Long id;

    @URL(message = "Image URL must be valid")
    private String url;

    private List<Long> articleIds;

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public List<Long> getArticleIds() { return articleIds; }
    public void setArticleIds(List<Long> articleIds) { this.articleIds = articleIds; }
}
