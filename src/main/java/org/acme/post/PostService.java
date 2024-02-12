package org.acme.post;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class PostService {


    @Inject
    EntityManager em;
    UUID uuid;

    public List<Post> findAll() {
    List<Post> posts = em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
    return posts;
    }

    public Post find(Long id) {
        return em.find(Post.class, id);
    } 

    public Long countPosts() {
        return em.createQuery("SELECT COUNT(p) FROM Post p", Long.class).getSingleResult(); 
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Post createPost(Post post, UUID userUUID) {
        post.setPostUserUUID(userUUID);
        em.persist(post);
        return post;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deletePost(Long id, UUID userUUID) {
    Post post = em.find(Post.class, id);
    if (post != null && post.getPostUserUUID().equals(userUUID)) {
        em.remove(post);
    } else {
        
        throw new EntityNotFoundException("Post not found or does not belong to the user");
    }
    }

    // @Transactional(Transactional.TxType.REQUIRED)
    // public void editPost(UUID userUUID, Long id, Post editedPost) {
    //     Post post = em.find(Post.class, id);
    //     if (post != null && post.getPostUserUUID().equals(userUUID)) {
    //         post.setImgUrl(editedPost.getImgUrl());
    //         post.setName(editedPost.getName());
    //         post.setPrice(editedPost.getPrice());
    //         em.merge(editedPost);
    //     } else {
            
    //         throw new EntityNotFoundException("Post not found or does not belong to the user");
    //     }
    // }

}
