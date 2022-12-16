package br.com.notes.Notes.controller;

import br.com.notes.Notes.models.Note;
import br.com.notes.Notes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@ResponseStatus(HttpStatus.OK)
@ResponseBody
@CrossOrigin("*")
@EnableCaching
public class NotesController {

    @Autowired
    private NotesRepository notesRepository;

    @GetMapping
    @Cacheable(cacheNames = "notes-all", unless = "#result.size() == 0")
    public List<Note> getNotes() {
        System.out.println("Buscando Notas");
        return notesRepository.findAll();
    }

    @PostMapping
    @Caching(
            put = {@CachePut(value = "note", key = "#note.id")},
            evict = {@CacheEvict(value = "notes-all", allEntries = true)}
    )
    public Note saveNote(@RequestBody Note note) {
        return notesRepository.save(note);
    }

    @PutMapping("/{id}")
    @Caching(
            put = {@CachePut(value = "note", key = "#id")},
            evict = {@CacheEvict(value = "notes-all", allEntries = true)}
    )
    public Note putNote(@PathVariable(name = "id") Long id, @RequestBody Note note) {
        Note noteBanco = notesRepository
                .findById(id)
                .orElse(new Note());

        noteBanco.setText(note.getText());
        noteBanco.setUrgent(note.getUrgent());

        return notesRepository.save(noteBanco);
    }

    @DeleteMapping("/{id}")
    @Caching(
            evict = {
                    @CacheEvict(value = "note", allEntries = true),
                    @CacheEvict(value = "notes-all", allEntries = true)
            }
    )
    public void deleteNote(@PathVariable(name = "id") Long id) {
        notesRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    @CachePut(value = "note", key = "#id")
    public Optional<Note> findById(@PathVariable(name = "id") Long id) {
        return this.notesRepository.findById(id);
    }

}