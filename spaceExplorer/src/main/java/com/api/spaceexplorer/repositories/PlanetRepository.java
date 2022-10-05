package com.api.spaceexplorer.repositories;

import com.api.spaceexplorer.model.entities.PlanetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanetRepository extends JpaRepository<PlanetEntity, Integer> {

    Optional<PlanetEntity> findPlanetEntityByPlanetName(String name);
}
