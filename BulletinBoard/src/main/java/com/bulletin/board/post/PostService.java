package com.bulletin.board.post;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.bulletin.board.DataNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	
	public List<Post> getList(){
		return this.postRepository.findAll();
	}
	
	public Post getPost(Integer id) {  
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new DataNotFoundException("post not found");
        }
    }
	
	public void create(String title, String content) {
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setCreateDate(LocalDateTime.now());
        this.postRepository.save(p);
    }
}
