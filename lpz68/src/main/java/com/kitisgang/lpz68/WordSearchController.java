package com.kitisgang.lpz68;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WordSearchController {

    @Autowired
    private WordSearchService wordSearchService;

    @GetMapping("/")
    public String showSearchForm(Model model, @RequestParam(required = false) String sessionId) {
        if (sessionId == null) {
            sessionId = java.util.UUID.randomUUID().toString();
        }
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("fileUploaded", wordSearchService.fileExists(sessionId));
        return "index";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("sessionId") String sessionId,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Пожалуйста, выберите файл");
            return "redirect:/?sessionId=" + sessionId;
        }

        try {
            wordSearchService.saveFile(file, sessionId);
            redirectAttributes.addFlashAttribute("success", "Файл успешно загружен");
            redirectAttributes.addFlashAttribute("fileUploaded", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке файла: " + e.getMessage());
            return "redirect:/?sessionId=" + sessionId;
        }

        return "redirect:/?sessionId=" + sessionId;
    }

    @PostMapping("/search")
    public String searchWord(@RequestParam String word,
                             @RequestParam String sessionId,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        // Проверяем существование файла перед поиском
        if (!wordSearchService.fileExists(sessionId)) {
            redirectAttributes.addFlashAttribute("error", "Сначала загрузите файл для поиска");
            return "redirect:/?sessionId=" + sessionId;
        }

        try {
            SearchResult result = wordSearchService.searchWord(word, sessionId);
            model.addAttribute("searchWord", word);
            model.addAttribute("count", result.getCount());
            model.addAttribute("frequency", result.getFrequency());
            model.addAttribute("totalWords", result.getTotalWords());
            model.addAttribute("sessionId", sessionId);
            return "result";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при поиске: " + e.getMessage());
            return "redirect:/?sessionId=" + sessionId;
        }
    }
}