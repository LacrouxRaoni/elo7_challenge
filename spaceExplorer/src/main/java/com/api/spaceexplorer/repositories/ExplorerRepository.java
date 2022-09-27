package com.api.spaceexplorer.repositories;

import com.api.spaceexplorer.model.entities.ExplorerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExplorerRepository extends JpaRepository<ExplorerEntity, Integer> {

    boolean existsByExplorerName(String explorerName);

    Optional<ExplorerEntity> findExplorerEntityByExplorerName(String name);
    void deleteExplorerEntityByExplorerName(String explorerName);
}
