package com.sai.quizapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sai.quizapp.entities.Quiz;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer>{

}
