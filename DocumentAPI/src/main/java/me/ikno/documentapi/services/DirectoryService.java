package me.ikno.documentapi.services;

import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.repositories.DirectoryRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;

    public DirectoryService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public List<DirectoryModel> getDirectories(int userId) {
        return directoryRepository.findByOwnerId(userId);
    }

    public @Nullable DirectoryModel getDirectoryById(int id, int userId) {
        DirectoryModel directory = directoryRepository.findById(id).orElse(null);

        if(directory == null) {
            return null;
        }

        if(directory.getOwnerId() != userId) {
            return null;
        }

        return directory;
    }

    public int createDirectory(String name, int parentId, int ownerId) {
        DirectoryModel parentDirectory = directoryRepository.findById(parentId).orElse(null);

        if(parentDirectory == null) {
            return -1;
        }

        if(parentDirectory.getOwnerId() != ownerId) {
            return -1;
        }

        DirectoryModel directoryModel = new DirectoryModel();
        directoryModel.setDisplayName(name);
        directoryModel.setParentId(parentId);
        directoryModel.setOwnerId(ownerId);

        return directoryRepository.save(directoryModel).getId();
    }
}
