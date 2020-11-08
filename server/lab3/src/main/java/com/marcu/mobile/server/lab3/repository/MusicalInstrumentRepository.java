package com.marcu.mobile.server.lab3.repository;

import com.marcu.mobile.server.lab3.model.MusicalInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicalInstrumentRepository extends JpaRepository<MusicalInstrument, Long> {
}
