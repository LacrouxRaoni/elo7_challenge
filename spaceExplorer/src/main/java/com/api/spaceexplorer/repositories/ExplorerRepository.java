package com.api.spaceexplorer.repositories;

import com.api.spaceexplorer.model.entities.ExplorerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExplorerRepository extends JpaRepository<ExplorerEntity, Integer> {
    Optional<ExplorerEntity> findExplorerEntityByExplorerName(String name);

    void deleteExplorerEntityByExplorerName(String name);

}
