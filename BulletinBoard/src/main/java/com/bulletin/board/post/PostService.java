package com.bulletin.board.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.bulletin.board.DataNotFoundException;
import com.bulletin.board.comment.Comment;
import com.bulletin.board.user.SiteUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	
	private Specification<Post> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Post> p, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  
                Join<Post, SiteUser> u1 = p.join("author", JoinType.LEFT);
                Join<Post, Comment> a = p.join("commentList", JoinType.LEFT);
                Join<Comment, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(p.get("title"), "%" + kw + "%"),
                        cb.like(p.get("content"), "%" + kw + "%"),
                        cb.like(u1.get("username"), "%" + kw + "%"),
                        cb.like(a.get("content"), "%" + kw + "%"),
                        cb.like(u2.get("username"), "%" + kw + "%"));
            }
        };
    }
	
	public Page<Post> getList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Post> spec = search(kw);
		return this.postRepository.findAll(spec, pageable);
	}
	
	public Post getPost(Integer id) {  
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
        	Post post1 = post.get();        	
        	post1.setView(post1.getView()+1);        	
        	this.postRepository.save(post1);
            return post1;
        } else {
            throw new DataNotFoundException("post not found");
        }
    }
	
	public void create(String title, String content, SiteUser user) {
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setCreateDate(LocalDateTime.now());
        p.setAuthor(user);
        this.postRepository.save(p);
    }
	
	public void modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        this.postRepository.save(post);
    }
	
	public void delete(Post post) {
        this.postRepository.delete(post);
    }
	
	public void vote(Post post, SiteUser siteUser) {
        post.getVoter().add(siteUser);
        this.postRepository.save(post);
    }
	
	public List<Post> getPostsByUser(String username){
		return this.postRepository.findByAuthor_Username(username);
	}
}
