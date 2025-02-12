package org.wildcodeschool.myblog.service;


import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.mapper.ImageMapper;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

import java.util.List;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(
            ImageRepository imageRepository,
            ImageMapper imageMapper
    ) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDTO> getAll() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::convertToDTO).toList();
    }

    public ImageDTO getById(Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return null;
        }
        return imageMapper.convertToDTO(image);
    }

    public ImageDTO create(Image image) {
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO update(Long id, Image imageDetails) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return null;
        }
        image.setUrl(imageDetails.getUrl());
        Image updatedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(updatedImage);
    }

    public boolean delete(Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return false;
        }
        imageRepository.delete(image);
        return true;
    }
}
