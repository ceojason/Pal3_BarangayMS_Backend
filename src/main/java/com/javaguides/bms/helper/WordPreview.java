package com.javaguides.bms.helper;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class WordPreview {

    public static String generatePreview(String templatePath, Map<String, String> props) {
        try (InputStream is = WordPreview.class.getClassLoader().getResourceAsStream(templatePath)) {
            assert is != null;
            try (XWPFDocument doc = new XWPFDocument(is)) {

                StringBuilder html = new StringBuilder();
                html.append("<div style='padding: 20px;'>");

                for (XWPFParagraph p : doc.getParagraphs()) {
                    String text = p.getText();
                    if (text != null && !text.isEmpty()) {
                        for (Map.Entry<String, String> entry : props.entrySet()) {
                            text = text.replace(entry.getKey(), entry.getValue());
                        }
                        html.append("<p>").append(text).append("</p>");
                    }
                }

                html.append("</div>");
                return html.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "<p style='color:red'>Failed to load document template: " + e.getMessage() + "</p>";
        }
    }
}