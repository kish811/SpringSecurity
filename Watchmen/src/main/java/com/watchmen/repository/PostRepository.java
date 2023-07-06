package com.watchmen.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.watchmen.model.Post;

public interface PostRepository extends ListCrudRepository<Post, Integer> {

}
