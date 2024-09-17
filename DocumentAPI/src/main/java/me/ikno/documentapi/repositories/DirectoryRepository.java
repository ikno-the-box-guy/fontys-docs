package me.ikno.documentapi.repositories;

import me.ikno.documentapi.models.DirectoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectoryRepository extends JpaRepository<DirectoryModel, Integer>{

}
