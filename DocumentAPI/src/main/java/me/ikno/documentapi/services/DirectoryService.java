package me.ikno.documentapi.services;

import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.repositories.DirectoryRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public String createDirectory(String name, String parentId, int ownerId) {
        DirectoryModel parentDirectory = directoryRepository.findById(parentId).orElse(null);

        if(parentDirectory == null) {
            return "";
        }

        if(parentDirectory.getOwnerId() != ownerId) {
            return "";
        }

        String directoryId = compressUuid(UUID.randomUUID().toString());

        DirectoryModel directoryModel = new DirectoryModel();
        directoryModel.setId(directoryId);
        directoryModel.setDisplayName(name);
        directoryModel.setParentId(parentId);
        directoryModel.setOwnerId(ownerId);

        return directoryRepository.save(directoryModel).getId();
    }

    public String createRootDirectory(int ownerId) {
        String directoryId = compressUuid(UUID.randomUUID().toString());

        DirectoryModel directoryModel = new DirectoryModel();
        directoryModel.setId(directoryId);
        directoryModel.setDisplayName("root");
        directoryModel.setParentId(directoryId);
        directoryModel.setOwnerId(ownerId);

        return directoryRepository.save(directoryModel).getId();
    }
}
