package com.example.productionservices.controllers;

import com.example.productionservices.dtos.PostDto;
import com.example.productionservices.services.PostService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping
    public ResponseEntity<List<PostDto>> findAll(){
        return ResponseEntity.ok(postService.getAllPost());
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        PostDto savedPost=postService.createPost(postDto);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> findById(@PathVariable Long postId){
        PostDto postDto=postService.getPostById(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);

    }
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId,@RequestBody PostDto postDto){
        PostDto postDto1=postService.updatePost(postId,postDto);
        if(postDto1==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postDto1, HttpStatus.OK);
    }
}
