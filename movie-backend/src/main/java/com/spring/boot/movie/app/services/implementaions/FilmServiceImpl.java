package com.spring.boot.movie.app.services.implementaions;

import com.spring.boot.movie.app.model.Film;
import com.spring.boot.movie.app.repositories.FilmRepository;
import com.spring.boot.movie.app.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    @Cacheable("findAll")
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film findById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    @Override
    @CacheEvict(value = "film", allEntries = true)
    public Film save(Film object) {
        return filmRepository.save(object);
    }

    @Override
    public void delete(Film object) {
        filmRepository.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        filmRepository.deleteById(id);
    }

    @Override
    @Cacheable("findByTitleContaining")
    public List<Film> findByTitleContaining(@RequestParam("title") String title) {
        return filmRepository.findByTitleContaining(title);
    }

    @Override
    public List<Film> findFilmByCategoryId(@Param("categoryId") Long categoryId) {
        return filmRepository.findFilmByCategoryId(categoryId);
    }

    @Override
    public List<Film> findFilmByActorId(@Param("actorId") Long actorId) {
        return filmRepository.findFilmByCategoryId(actorId);
    }

}
