package com.spring.boot.movie.app.services;

import com.spring.boot.movie.app.model.Actor;
import com.spring.boot.movie.app.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public List<Actor> findAll() {
        return (List<Actor>) actorRepository.findAll();
    }

    @Override
    public Actor findById(Long id) {

        List<Actor> actors = (List<Actor>) actorRepository.findAll();

        for (Actor actor : actors) {
            if (actor.getId().equals(id)) {
                return actor;
            }
        }
        throw new RuntimeException("Actor Does Not Exist");
    }

    @Override
    public Actor save(Actor object) {
        return null;
    }

    @Override
    public void delete(Actor object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
