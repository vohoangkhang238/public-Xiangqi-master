package com.sojourners.chess.util;

import com.sojourners.chess.board.ChessBoard.Point;

public class JieqiUtils {

    /**
     * So sánh hai trạng thái bàn cờ và suy luận ra nước đi UCI (bao gồm lật quân).
     * @param oldBoard Bàn cờ trước.
     * @param newBoard Bàn cờ sau.
     * @return UCI move string (e.g., "a1a2" hoặc "i0i0R" cho lật quân) hoặc null.
     */
    public static String compareBoards(char[][] oldBoard, char[][] newBoard) {
        Point fromPos = null;
        Point toPos = null;
        
        // Tìm vị trí quân cờ biến mất (fromPos) và vị trí quân cờ xuất hiện/thay đổi (toPos)
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 9; c++) {
                if (oldBoard[r][c] != newBoard[r][c]) {
                    // Nếu một quân cờ đã bị xóa (không phải ô trống), đó là vị trí 'from'
                    if (oldBoard[r][c] != ' ' && newBoard[r][c] == ' ') {
                        fromPos = new Point(c, r);
                    }
                    // Nếu một quân cờ xuất hiện/thay đổi (không phải ô trống), đó là vị trí 'to'
                    if (newBoard[r][c] != ' ') {
                        toPos = new Point(c, r);
                    }
                }
            }
        }

        // --- Trường hợp Lật Quân ---
        // Nước lật quân xảy ra khi: Vị trí 'to' là quân cờ đã lật, và Vị trí 'from' không tồn tại
        // HOẶC vị trí 'from' và 'to' là cùng một ô cờ.
        if (toPos != null && oldBoard[toPos.y][toPos.x] == 'X' && newBoard[toPos.y][toPos.x] != 'X') {
            char revealedPiece = newBoard[toPos.y][toPos.x];
            // Trả về UCI move dạng a1a1<Piece> cho lật quân (e.g., "a1a1R")
            return String.format("%c%d%c%d%c", 
                    (char)('a' + toPos.x), 9 - toPos.y,
                    (char)('a' + toPos.x), 9 - toPos.y,
                    revealedPiece); 
        }

        // --- Trường hợp Nước Đi Thường/Ăn Quân ---
        if (fromPos != null && toPos != null) {
            // Trả về UCI move dạng a1b2
            return String.format("%c%d%c%d", 
                    (char)('a' + fromPos.x), 9 - fromPos.y, 
                    (char)('a' + toPos.x), 9 - toPos.y);
        }

        return null;
    }
}