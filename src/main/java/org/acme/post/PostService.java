package org.acme.post;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
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
    public Post createPost(Post post) {
        post.setPostUserUUid(UUID.randomUUID());
        em.persist(post);
        return post;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deletePost(Long id) {
        em.remove(em.getReference(Post.class, id));
    } 
}
