package com.marcu.mobile.server.lab3.service;

import com.marcu.mobile.server.lab3.model.MusicalInstrument;
import com.marcu.mobile.server.lab3.repository.MusicalInstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MusicalInstrumentServiceImpl implements MusicalInstrumentService {

    private MusicalInstrumentRepository musicalInstrumentRepository;

    @Autowired
    public MusicalInstrumentServiceImpl(MusicalInstrumentRepository musicalInstrumentRepository){
        this.musicalInstrumentRepository = musicalInstrumentRepository;
    }

    @Override
    public List<MusicalInstrument> getAll() {
        return musicalInstrumentRepository.findAll();
    }

    @Override
    @Transactional
    public Long save(MusicalInstrument musicalInstrument) {
        return musicalInstrumentRepository.save(musicalInstrument).getId();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        musicalInstrumentRepository.deleteById(id);
    }

    @Override
    public MusicalInstrument update(MusicalInstrument musicalInstrument) {
        return musicalInstrumentRepository.save(musicalInstrument);
    }
}
