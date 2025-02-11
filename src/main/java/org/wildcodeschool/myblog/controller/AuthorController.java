package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleAuthorDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    public AuthorController(
            AuthorRepository authorRepository,
            ArticleRepository articleRepository,
            ArticleAuthorRepository articleAuthorRepository
    ) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAll() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<AuthorDTO> authorDTOS = authors.stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(authorDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getById(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(author));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);

        if (author.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : author.getArticleAuthors()) {
                Article article = articleAuthor.getArticle();
                article = articleRepository.findById(article.getId()).orElse(null);
                if (article == null) {
                    return ResponseEntity.notFound().build();
                }

                articleAuthor.setArticle(article);
                articleAuthor.setAuthor(savedAuthor);

                articleAuthorRepository.save(articleAuthor);
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedAuthor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author author = authorRepository.findById(id).orElse(null);

        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        author.setFirstname(authorDetails.getFirstname());
        author.setLastname(authorDetails.getLastname());

        if (authorDetails.getArticleAuthors() != null) {
            for (ArticleAuthor oldArticleAuthor : authorDetails.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : authorDetails.getArticleAuthors()) {
                Article article = articleAuthorDetails.getArticle();
                article = articleRepository.findById(article.getId()).orElse(null);
                if (article == null) {
                    return ResponseEntity.notFound().build();
                }

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);

                for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                    articleAuthorRepository.save(articleAuthor);
                }

                author.setArticleAuthors(updatedArticleAuthors);
            }
        }

        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.ok(convertToDTO(savedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);

        if (author == null) {
            return ResponseEntity.notFound().build();
        }

        if (author.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : author.getArticleAuthors()) {
                articleAuthorRepository.delete(articleAuthor);
            }
        }

        authorRepository.delete(author);
        return ResponseEntity.noContent().build();
    }

    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstname(author.getFirstname());
        authorDTO.setLastname(author.getLastname());
        if (author.getArticleAuthors() != null) {
            authorDTO.setArticleAuthorDTOs(author.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getArticle() != null)
                    .map(ArticleAuthor -> {
                        ArticleAuthorDTO articleAuthorDTO = new ArticleAuthorDTO();
                        articleAuthorDTO.setId(ArticleAuthor.getId());
                        articleAuthorDTO.setArticleId(ArticleAuthor.getArticle().getId());
                        articleAuthorDTO.setAuthorId(ArticleAuthor.getAuthor().getId());
                        articleAuthorDTO.setContribution(ArticleAuthor.getContribution());
                        return articleAuthorDTO;
                    })
                    .toList()
            );
        }
        return authorDTO;
    }
}
