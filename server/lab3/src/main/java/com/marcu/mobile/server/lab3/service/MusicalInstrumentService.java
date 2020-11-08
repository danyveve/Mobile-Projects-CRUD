package com.marcu.mobile.server.lab3.service;

import com.marcu.mobile.server.lab3.model.MusicalInstrument;

import java.util.List;

public interface MusicalInstrumentService {
    List<MusicalInstrument> getAll();
    Long save(MusicalInstrument musicalInstrument);
    void deleteById(Long id);
    MusicalInstrument update(MusicalInstrument musicalInstrument);
}
