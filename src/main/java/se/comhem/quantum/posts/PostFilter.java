package se.comhem.quantum.posts;

import se.comhem.quantum.model.Post;

import java.util.Map;
import java.util.Objects;

class PostFilter {
    
    static boolean newOrUpdatedPost(Post newPost, Map<String, Post> postsCached) {
        Post post = postsCached.get(newPost.getKey());
        return post == null || !Objects.equals(post.getUpdateDate(), newPost.getUpdateDate());
    }
}
