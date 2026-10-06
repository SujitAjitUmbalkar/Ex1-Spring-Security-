package com.example.demo4.SecurityApp.utils;

import com.example.demo4.SecurityApp.dto.PostDTO;
import com.example.demo4.SecurityApp.dto.UserDto;
import com.example.demo4.SecurityApp.entities.UserEntity;
import com.example.demo4.SecurityApp.repositories.UserRepository;
import com.example.demo4.SecurityApp.services.PostService;
import com.example.demo4.SecurityApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity
{
    private final PostService postService;

    public boolean isOwnerOfPost(Long postId)
    {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PostDTO postDTO = postService.getPostById(postId);

        if(postDTO.getAuthor().getId().equals(user.getId()))
        {
            return true;
        }
        return false;
    }


}
