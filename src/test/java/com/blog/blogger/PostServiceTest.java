package com.blog.blogger;


import com.blog.blogger.entity.Post;
import com.blog.blogger.payload.PostDto;
import com.blog.blogger.repository.PostRepository;
import com.blog.blogger.service.PostService;
import com.blog.blogger.service.impl.PostServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {

    @Mock
    public PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;


    @Test
    public void createPostTest()
    {
        //Given
        PostDto postDto = new PostDto();
        postDto.setTitle("Title");
        postDto.setDescription("Description");
        postDto.setContent("Content");

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        when(postRepository.save(ArgumentMatchers.any(Post.class))).thenReturn(post);

        //when
        PostDto result = postService.createPost(postDto);

        //then
        assertEquals(postDto.getTitle(),result.getTitle());
        assertEquals(postDto.getDescription(),result.getDescription());
        assertEquals(postDto.getContent(), result.getContent());
        assertEquals("Post is created", result.getMessage());

    }

    @Test
    public void testDeletePost() {
        // Given
        long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));

        // When
        postService.deletePost(postId);

        // Then
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    public void testGetAllPosts() {
        // Given
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        Pageable pageable = Mockito.mock(Pageable.class);

// Create a list of Post objects to represent the content of the page
        List<Post> posts = new ArrayList<>();
// Add some Post objects to the list (for example purposes)
        posts.add(new Post());
        posts.add(new Post());
// Create a Page object using the list of Post objects and the mock Pageable
        Page<Post> pagePosts = new PageImpl<>(posts, pageable, posts.size());

// Now you can use the pagePosts object in your Mockito stubbing
// For example:
        when(postRepository.findAll(any(Pageable.class))).thenReturn(pagePosts);

        //when
        List<PostDto> result = postService.getAllPosts(pageNo, pageSize, sortBy);

        // Then
        assertEquals(posts.size(), result.size());
    }

    @Test
    public void testUpdatePost() {
        // Given
        long postId = 1L;
        PostDto postDto = new PostDto();
        postDto.setTitle("Updated Title");
        postDto.setDescription("Updated Description");
        postDto.setContent("Updated Content");

        Post post = new Post();
        post.setId(postId);
        post.setTitle("Title");
        post.setDescription("Description");
        post.setContent("Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        PostDto result = postService.updatePost(postId, postDto);

        // Then
        assertEquals(postDto.getTitle(), result.getTitle());
        assertEquals(postDto.getDescription(), result.getDescription());
        assertEquals(postDto.getContent(), result.getContent());
    }

}
