package com.codealpha.notesapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class NoteRepositoryTests {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void savingANote_assignsAnIdAndPersistsIt() {
        Note note = new Note();
        note.setTitle("Test note");
        note.setContent("Some content");

        Note saved = noteRepository.save(note);

        assertNotNull(saved.getId());

        Note found = noteRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Test note", found.getTitle());
    }
}
