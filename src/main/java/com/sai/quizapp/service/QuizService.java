package com.sai.quizapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sai.quizapp.dao.QuestionDao;
import com.sai.quizapp.dao.QuizDao;
import com.sai.quizapp.entities.Question;
import com.sai.quizapp.entities.Quiz;
import com.sai.quizapp.model.QuestionWrapper;
import com.sai.quizapp.model.QuizResponse;

@Service
public class QuizService {

	@Autowired
	QuizDao quizdao;

	@Autowired
	QuestionDao questionDao;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
		List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
		Quiz quiz = new Quiz();
		quiz.setTitle(title);
		quiz.setQuestions(questions);
		quizdao.save(quiz);
		return new ResponseEntity<>("quiz created sucessfully", HttpStatus.CREATED);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuiz(Integer quizId) {
		Optional<Quiz> quiz = quizdao.findById(quizId);
		List<Question> questionsFromDB = quiz.get().getQuestions();
		List<QuestionWrapper> questionForUser = new ArrayList<>();
		for (Question q : questionsFromDB) {
			QuestionWrapper questionWrapper = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(),
					q.getOption2(), q.getOption3(), q.getOption4());
			questionForUser.add(questionWrapper);
		}
		return new ResponseEntity<>(questionForUser, HttpStatus.OK);
	}

	public ResponseEntity<Integer> calculateScore(Integer quizId, List<QuizResponse> responses) {
		Optional<Quiz> quiz = quizdao.findById(quizId);
		List<Question> questions = quiz.get().getQuestions();
		Integer score = 0;
		int i = 0;
		for (QuizResponse q : responses) {
			// Optional<Question> qes = questionDao.findById(q.getId());
			if (q.getResponse().equals(questions.get(i).getRightAnswer())) {
				score++;
			}
			i++;
		}
		return new ResponseEntity<Integer>(score, HttpStatus.OK);
	}

}
