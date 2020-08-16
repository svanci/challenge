package svancar.hoval.challenge.service;

import java.io.InputStream;
import java.util.Map;

public interface FileStorageService {

    void save(String path, String fileName, Map<String, String> optionalMetaData, InputStream inputStream);

    byte[] load(String path, String key);
}
