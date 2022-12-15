package br.com.notes.Notes.controller;

import br.com.notes.Notes.models.Note;
import br.com.notes.Notes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@ResponseBody
public class NotesController {

    @Autowired
    private NotesRepository notesRepository;

    @GetMapping
    public List<Note> getNotes() {
        return notesRepository.findAll();
    }

    @PostMapping
    public Note saveNote(@RequestBody Note note) {
        return notesRepository.save(note);
    }

    @PutMapping
    public Note putNote(@RequestParam(name = "id") Long id, @RequestBody Note note) {
        Note noteBanco = notesRepository
                .findById(id)
                .orElse(new Note());

        noteBanco.setText(note.getText());
        noteBanco.setUrgent(note.getUrgent());

        return notesRepository.save(noteBanco);
    }

    @DeleteMapping
    public void deleteNote(@RequestParam(name = "id") Long id) {
        notesRepository.deleteById(id);
    }

}
