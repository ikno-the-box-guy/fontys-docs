package me.ikno.documentapi.services;

import me.ikno.documentapi.exceptions.DuplicateDirectoryException;
import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.repositories.DirectoryRepository;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static me.ikno.documentapi.utils.IdUtils.compressUuid;

@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;

    public DirectoryService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public List<DirectoryModel> getDirectories(int userId) {
        return directoryRepository.findByOwnerId(userId);
    }

    public @Nullable DirectoryModel getDirectoryById(String id, int userId) {
        DirectoryModel directory = directoryRepository.findById(id).orElse(null);

        if(directory == null) {
            return null;
        }

        if(directory.getOwnerId() != userId) {
            return null;
        }

        return directory;
    }

    public DirectoryModel createDirectory(String name, String parentId, int ownerId) {
        DirectoryModel parentDirectory = directoryRepository.findById(parentId).orElse(null);

        if(parentDirectory == null) {
            return null;
        }

        if(parentDirectory.getOwnerId() != ownerId) {
            return null;
        }

        String directoryId = compressUuid(UUID.randomUUID().toString());

        DirectoryModel directoryModel = new DirectoryModel();
        directoryModel.setId(directoryId);
        directoryModel.setDisplayName(name);
        directoryModel.setParentId(parentId);
        directoryModel.setOwnerId(ownerId);

        try {
            return directoryRepository.save(directoryModel);
        } catch (Exception e) {
            throw new DuplicateDirectoryException("Directory with name " + name + " already exists");
        }
    }

    @Async
    public CompletableFuture<String> createRootDirectory(int ownerId) {
        String directoryId = compressUuid(UUID.randomUUID().toString());

        DirectoryModel directoryModel = new DirectoryModel();
        directoryModel.setId(directoryId);
        directoryModel.setDisplayName("root");
        directoryModel.setParentId(directoryId);
        directoryModel.setOwnerId(ownerId);

        String id = directoryRepository.save(directoryModel).getId();

        return CompletableFuture.completedFuture(id);
    }
}
