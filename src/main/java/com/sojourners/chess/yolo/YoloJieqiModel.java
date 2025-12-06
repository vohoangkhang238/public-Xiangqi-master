package com.sojourners.chess.yolo;

import java.util.HashMap;
import java.util.Map;

/**
 * Xử lý mô hình ONNX cho Cờ Úp (Jieqi).
 */
public class YoloJieqiModel extends Yolo11Model { // Kế thừa từ Yolo11Model

    // Giả định CLASSES bao gồm cả quân úp (Concealed piece)
    private static final String[] CLASSES = new String[] {
            // 14 quân cờ thường
            "rK", "rA", "rB", "rN", "rR", "rC", "rP", 
            "bK", "bA", "bB", "bN", "bR", "bC", "bP", 
            // Quân úp
            "X" // Giả định tên lớp cho quân úp là "X"
    };
    
    private static final Map<String, Character> PIECE_MAP = new HashMap<>();
    static {
        // Ánh xạ 14 quân cờ thường
        PIECE_MAP.put("rK", 'K'); PIECE_MAP.put("rA", 'A'); PIECE_MAP.put("rB", 'B'); 
        PIECE_MAP.put("rN", 'N'); PIECE_MAP.put("rR", 'R'); PIECE_MAP.put("rC", 'C'); 
        PIECE_MAP.put("rP", 'P'); 
        
        PIECE_MAP.put("bK", 'k'); PIECE_MAP.put("bA", 'a'); PIECE_MAP.put("bB", 'b'); 
        PIECE_MAP.put("bN", 'n'); PIECE_MAP.put("bR", 'r'); PIECE_MAP.put("bC", 'c'); 
        PIECE_MAP.put("bP", 'p');
        
        // Quân úp: quan trọng là phải ánh xạ về ký tự đơn 'X' trong Java char[][]
        PIECE_MAP.put("X", 'X'); 
    }

    public YoloJieqiModel() {
        super("yolojieqi.onnx", CLASSES, PIECE_MAP); // Sử dụng tên file ONNX mới
    }
}