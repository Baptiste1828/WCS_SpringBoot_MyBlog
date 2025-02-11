package org.wildcodeschool.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;

import java.util.List;

public interface ArticleAuthorRepository extends JpaRepository<ArticleAuthor, Long> {

    public List<ArticleAuthor> findByArticle(Article article);

    public List<ArticleAuthor> findByAuthor(Author author);

    public List<ArticleAuthor> findByContribution(String contribution);
}
