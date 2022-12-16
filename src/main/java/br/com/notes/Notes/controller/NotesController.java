package br.com.notes.Notes.controller;

import br.com.notes.Notes.models.Note;
import br.com.notes.Notes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@ResponseBody
@CrossOrigin("*")
@EnableCaching
public class NotesController {

    @Autowired
    private NotesRepository notesRepository;

    @GetMapping
    @Cacheable(cacheNames = "notes")
    public List<Note> getNotes() {
        System.out.println("Buscando Notas");
        return notesRepository.findAll();
    }

    @PostMapping
    @CachePut(cacheNames = "notes", key = "#note.id")
    public Note saveNote(@RequestBody Note note) {
        return notesRepository.save(note);
    }

    @PutMapping("/{id}")
    @CachePut(cacheNames = "notes", key = "#note.id")
    public Note putNote(@PathVariable(name = "id") Long id, @RequestBody Note note) {
        Note noteBanco = notesRepository
                .findById(id)
                .orElse(new Note());

        noteBanco.setText(note.getText());
        noteBanco.setUrgent(note.getUrgent());

        return notesRepository.save(noteBanco);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(cacheNames = "notes", key = "#note.id")
    public void deleteNote(@PathVariable(name = "id") Long id) {
        notesRepository.deleteById(id);
    }

}
