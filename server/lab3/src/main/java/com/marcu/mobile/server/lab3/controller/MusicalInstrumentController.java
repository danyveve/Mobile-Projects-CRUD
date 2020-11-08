package com.marcu.mobile.server.lab3.controller;

import com.marcu.mobile.server.lab3.model.MusicalInstrument;
import com.marcu.mobile.server.lab3.service.MusicalInstrumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/musicalInstrumentsApi")
public class MusicalInstrumentController {
    private MusicalInstrumentService musicalInstrumentService;

    @GetMapping(value = "/getAll")
    public List<MusicalInstrument> getAll() {
        return musicalInstrumentService.getAll();
    }

    @PostMapping(value = "/save", consumes = "application/json")
    public Long save(@RequestBody MusicalInstrument musicalInstrument) {
        return this.musicalInstrumentService.save(musicalInstrument);
    }

    @PutMapping(value = "/update", consumes = "application/json")
    public MusicalInstrument update(@RequestBody MusicalInstrument musicalInstrument) {
        return this.musicalInstrumentService.update(musicalInstrument);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        this.musicalInstrumentService.deleteById(id);
    }

    @GetMapping(value = "/get")
    public String getString() {
        return "Hello";
    }
}
