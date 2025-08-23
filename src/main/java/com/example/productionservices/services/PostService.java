package com.example.productionservices.services;

import com.example.productionservices.dtos.PostDto;
import com.example.productionservices.entities.Post;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public interface PostService {

     PostDto createPost(PostDto postDto) ;
     List<PostDto> getAllPost();
    public PostDto getPostById(Long id);

     PostDto updatePost(Long postId, PostDto postDto);
}
