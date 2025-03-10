package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.BadRequestException;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(
            AuthorRepository authorRepository,
            ArticleRepository articleRepository,
            ArticleAuthorRepository articleAuthorRepository,
            AuthorMapper authorMapper
    ) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().map(authorMapper::convertToDTO).toList();
    }

    public AuthorDTO getById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " was not found."));
        return authorMapper.convertToDTO(author);
    }

    public AuthorDTO create(Author author) {
        Author savedAuthor = authorRepository.save(author);

        if (author.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : author.getArticleAuthors()) {
                Article article = articleAuthor.getArticle();
                article = articleRepository.findById(article.getId())
                        .orElseThrow(() -> new BadRequestException("Article with id " + articleAuthor.getArticle().getId() + " does not exist. Unable to create author."));
                articleAuthor.setArticle(article);
                articleAuthor.setAuthor(savedAuthor);

                articleAuthorRepository.save(articleAuthor);
            }
        }
        return authorMapper.convertToDTO(savedAuthor);
    }

    public AuthorDTO update(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " was not found."));
        author.setFirstname(authorDetails.getFirstname());
        author.setLastname(authorDetails.getLastname());

        if (authorDetails.getArticleAuthors() != null) {
            articleAuthorRepository.deleteAll(authorDetails.getArticleAuthors());

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : authorDetails.getArticleAuthors()) {
                Article article = articleAuthorDetails.getArticle();
                article = articleRepository.findById(article.getId())
                        .orElseThrow(() -> new BadRequestException("Article with id " + articleAuthorDetails.getArticle().getId() + " does not exist. Unable to update author."));
                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);

                articleAuthorRepository.saveAll(updatedArticleAuthors);

                author.setArticleAuthors(updatedArticleAuthors);
            }
        }

        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(savedAuthor);
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " was not found."));
        if (author.getArticleAuthors() != null) {
            articleAuthorRepository.deleteAll(author.getArticleAuthors());
        }
        authorRepository.delete(author);
    }
}
