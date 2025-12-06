package com.sojourners.chess.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Quản lý kho quân úp (Dark Piece Library) cho Cờ Úp (Jieqi).
 */
public class JieqiPool {
    // Số lượng quân cờ tối đa (2 Xe, 2 Mã, 2 Tượng/Sĩ, 2 Sĩ/Tượng, 2 Pháo, 5 Tốt)
    private static final Map<Character, Integer> MAX_COUNTS = Map.of(
        'R', 2, 'N', 2, 'B', 2, 'A', 2, 'C', 2, 'P', 5, // Quân Đỏ (Hoa)
        'r', 2, 'n', 2, 'b', 2, 'a', 2, 'c', 2, 'p', 5  // Quân Đen (Thường)
    );
    
    private final Map<Character, Integer> pool; // Số lượng quân cờ còn lại trong kho úp

    public JieqiPool() {
        this.pool = new HashMap<>(MAX_COUNTS);
    }
    
    /**
     * Cập nhật Pool bằng cách đếm các quân cờ đã lật trên bàn.
     * @param boardState Mảng char[][] trạng thái bàn cờ hiện tại.
     */
    public void updatePoolFromBoard(char[][] boardState) {
        this.pool.clear();
        this.pool.putAll(MAX_COUNTS);

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 9; c++) {
                char piece = boardState[r][c];
                // Loại trừ quân úp 'X' và ô trống ' '
                if (piece != ' ' && piece != 'X') {
                    if (this.pool.containsKey(piece) && this.pool.get(piece) > 0) {
                        this.pool.put(piece, this.pool.get(piece) - 1);
                    }
                }
            }
        }
    }

    public Map<Character, Integer> getCurrentPool() {
        return pool;
    }
    
    public int totalPoolCount() {
        return pool.values().stream().mapToInt(Integer::intValue).sum();
    }
}