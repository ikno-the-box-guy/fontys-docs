package me.ikno.documentapi.services;

import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.repositories.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;

    public DirectoryService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public List<DirectoryModel> getDirectories() {
        return directoryRepository.findAll();
    }

    public @Nullable DirectoryModel getDirectoryById(Integer id) {
        return directoryRepository.findById(id).orElse(null);
    }

    public int createDirectory(String name, int parentId, int ownerId) {
        DirectoryModel directoryModel = new DirectoryModel();
        directoryModel.setDisplayName(name);
        directoryModel.setParentId(parentId);
        directoryModel.setOwnerId(ownerId);

        return directoryRepository.save(directoryModel).getId();
    }
}
