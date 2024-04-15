package com.blog.blogger;

import com.blog.blogger.controller.PostController;
import com.blog.blogger.payload.PostDto;
import com.blog.blogger.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostControllerTest {

 public MockMvc mockMvc;

 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();

 @Mock
 PostService postService;

 @InjectMocks
private PostController postController;


  @Before
 public void setup(){
   this.mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

 }

 @Test
    public void getAllPostsTest()
 {
     //Given
     int pageSize = 3;
     int pageNo = 0;
     String sortBy = "id";

     List<PostDto> postDtos = new ArrayList<>();
     Mockito.when(postService.getAllPosts(pageNo,pageSize,sortBy)).thenReturn(postDtos);

     //When
     ResponseEntity<List<PostDto>> response = postController.getAllPosts(pageNo,pageSize,sortBy);

     //Then
     assertEquals(HttpStatus.OK, response.getStatusCode());
     assertEquals(postDtos, response.getBody());

 }

 @Test
 public void createPostTest()
 {
     //Given
     PostDto postDto = new PostDto();
     BindingResult bindingResult = mock(BindingResult.class);
     when(bindingResult.hasErrors()).thenReturn(false);
     when(postService.createPost(postDto)).thenReturn(postDto);

     //when
     ResponseEntity<?> response = postController.createPost(postDto,bindingResult);

     //then
     assertEquals(HttpStatus.CREATED,response.getStatusCode());
     assertEquals(postDto,response.getBody());

 }

    @Test
    public void updatePostTest()
 {
     //Given
      long postId = 2L;
      PostDto postDto = new PostDto();
      when(postService.updatePost(postId,postDto)).thenReturn(postDto);

      //when
     ResponseEntity<PostDto> response = postController.updatePost(postId,postDto);

     //then
     assertEquals(HttpStatus.OK,response.getStatusCode());
     assertEquals(postDto,response.getBody());
 }

     @Test
     public void deletePost()
     {
         //given
         long postId = 1L;

         //when
         ResponseEntity<String> response = postController.deletePost(postId);

         //Then
         assertEquals(HttpStatus.OK,response.getStatusCode());
         assertEquals("Post is deleted",response.getBody());


     }
}
