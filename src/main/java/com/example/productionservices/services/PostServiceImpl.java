package com.example.productionservices.services;

import com.example.productionservices.dtos.PostDto;
import com.example.productionservices.entities.Post;
import com.example.productionservices.repositories.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private  ModelMapper modelMapper = null;
    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public PostDto createPost(PostDto postDto) {
        Post toSavePost=modelMapper.map(postDto,Post.class);
        Post savedPost=postRepository.save(toSavePost);
        return modelMapper.map(savedPost,PostDto.class);
    }

    @Override
    public List<PostDto> getAllPost() {
        List<Post> allPosts=postRepository.findAll();
        return allPosts
                .stream()
                .map(post->modelMapper.map(post,PostDto.class) )
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(Long id) {
        Optional<Post> post=postRepository.findById(id);
        return post.map(post1 -> modelMapper.map(post1,PostDto.class)).orElse(null);
    }

    @Override
    public PostDto updatePost(Long postId, PostDto postDto) {
//        Post post=modelMapper.map(postDto,Post.class);
        Post olderPost=postRepository.findById(postId).orElse(null);

        postDto.setId(postId);
        modelMapper.map(postDto,olderPost);

        if(!postRepository.findById(postId).isPresent()){
            return null;
        }
        Post savedPost=postRepository.save(olderPost);
        return modelMapper.map(savedPost,PostDto.class);
    }
}
