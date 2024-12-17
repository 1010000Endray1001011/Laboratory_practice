package com.kitisgang.lpz68;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class WordSearchService {

    private final Path uploadDir;

    public WordSearchService() {
        this.uploadDir = Paths.get(System.getProperty("user.home"), "wordSearchUploads");
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для загрузки", e);
        }
    }

    public void saveFile(MultipartFile file, String sessionId) throws IOException {
        // Создаем директорию для сессии
        Path sessionDir = uploadDir.resolve(sessionId);
        Files.createDirectories(sessionDir);

        // Сохраняем файл
        Path filePath = sessionDir.resolve("uploaded.txt");
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        // Проверяем, что файл действительно создан
        if (!Files.exists(filePath)) {
            throw new IOException("Файл не был сохранен");
        }
    }

    public SearchResult searchWord(String searchWord, String sessionId) throws IOException {
        Path filePath = uploadDir.resolve(sessionId).resolve("uploaded.txt");

        // Подробная проверка файла
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Файл не найден для сессии: " + sessionId);
        }

        if (!Files.isReadable(filePath)) {
            throw new IOException("Файл не доступен для чтения: " + sessionId);
        }

        int wordCount = 0;
        int totalWords = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Улучшенная обработка слов
                String[] words = line.trim().split("\\s+");
                if (line.trim().isEmpty()) {
                    continue; // Пропускаем пустые строки
                }

                totalWords += words.length;

                for (String word : words) {
                    // Очищаем слово от знаков препинания
                    word = word.replaceAll("[^а-яА-Яa-zA-Z0-9]", "").toLowerCase();
                    if (word.equals(searchWord.toLowerCase())) {
                        wordCount++;
                    }
                }
            }
        }

        // Проверка на случай пустого файла
        if (totalWords == 0) {
            return new SearchResult(0, 0.0, 0);
        }

        double frequency = (double) wordCount / totalWords * 100;
        return new SearchResult(wordCount, frequency, totalWords);
    }

    // Метод для проверки существования файла
    public boolean fileExists(String sessionId) {
        Path filePath = uploadDir.resolve(sessionId).resolve("uploaded.txt");
        return Files.exists(filePath) && Files.isReadable(filePath);
    }
}