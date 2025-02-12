package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.mapper.ArticleMapper;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final AuthorRepository authorRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(
            ArticleRepository articleRepository,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            ArticleAuthorRepository articleAuthorRepository,
            AuthorRepository authorRepository,
            ArticleMapper articleMapper
    ) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorRepository = authorRepository;
        this.articleMapper = articleMapper;
    }

    public List<ArticleDTO> getAll() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(articleMapper::convertToDTO).toList();
    }

    public ArticleDTO getById(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }
        return articleMapper.convertToDTO(article);
    }

    public List<ArticleDTO> getByTitle(String searchTerms) {
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        return articles.stream().map(articleMapper::convertToDTO).toList();
    }

    public List<ArticleDTO> getByContent(String searchTerms) {
        List<Article> articles = articleRepository.findByContentContaining(searchTerms);
        return articles.stream().map(articleMapper::convertToDTO).toList();
    }

    public List<ArticleDTO> getCreatedAfter(LocalDateTime searchDate) {
        List<Article> articles = articleRepository.findByCreatedAtAfter(searchDate);
        return articles.stream().map(articleMapper::convertToDTO).toList();
    }

    public List<ArticleDTO> getFiveLast() {
        List<Article> articles = articleRepository.findTop5ByOrderByCreatedAtDesc();
        return articles.stream().map(articleMapper::convertToDTO).toList();
    }

    public ArticleDTO create(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            article.setCategory(category);
        }

        if (article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null;
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }

        Article savedArticle = articleRepository.save(article);

        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                Author author = articleAuthor.getAuthor();
                author = authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return null;
                }

                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);

                articleAuthorRepository.save(articleAuthor);
            }
        }

        return articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO update(Long id, Article articleDetails) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        if (articleDetails.getCategory() != null) {
            Category category = categoryRepository.findById(articleDetails.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            article.setCategory(category);
        }

        if (articleDetails.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : articleDetails.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null;
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }

        if (articleDetails.getArticleAuthors() != null) {
            articleAuthorRepository.deleteAll(article.getArticleAuthors());

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
                Author author = articleAuthorDetails.getAuthor();
                author = authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return null;
                }

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);
            }

            articleAuthorRepository.saveAll(updatedArticleAuthors);

            article.setArticleAuthors(updatedArticleAuthors);
        }

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updatedArticle);
    }

    public boolean delete(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return false;
        }

        if (article.getArticleAuthors() != null) {
            articleAuthorRepository.deleteAll(article.getArticleAuthors());
        }
        articleRepository.delete(article);
        return true;
    }
}
