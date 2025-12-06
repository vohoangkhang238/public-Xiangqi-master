package com.sojourners.chess.util;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MoveAnimator {

    /**
     * Tạo hiệu ứng trượt quân cờ
     * @param rootPane : Pane chứa bàn cờ (thường là canvasPane)
     * @param pieceImage : Hình ảnh quân cờ cần di chuyển
     * @param startX : Tọa độ X ban đầu (pixel)
     * @param startY : Tọa độ Y ban đầu (pixel)
     * @param endX : Tọa độ X đích (pixel)
     * @param endY : Tọa độ Y đích (pixel)
     * @param pieceSize : Kích thước quân cờ (width/height)
     * @param onFinished : Hàm sẽ chạy sau khi di chuyển xong (thường là vẽ lại bàn cờ)
     */
    public static void animateMove(Pane rootPane, Image pieceImage, 
                                   double startX, double startY, 
                                   double endX, double endY, 
                                   double pieceSize,
                                   Runnable onFinished) {
        
        // 1. Tạo một quân cờ giả (ImageView) đè lên bàn cờ
        ImageView fakePiece = new ImageView(pieceImage);
        fakePiece.setFitWidth(pieceSize);
        fakePiece.setFitHeight(pieceSize);
        fakePiece.setX(startX); // Đặt tại vị trí cũ
        fakePiece.setY(startY);
        
        // Thêm vào giao diện
        Platform.runLater(() -> {
            rootPane.getChildren().add(fakePiece);

            // 2. Tạo hiệu ứng di chuyển (Slide)
            TranslateTransition tt = new TranslateTransition(Duration.millis(250), fakePiece); // 250ms là tốc độ chuẩn
            tt.setToX(endX - startX);
            tt.setToY(endY - startY);
            
            // 3. Khi chạy xong
            tt.setOnFinished(event -> {
                // Xóa quân giả đi
                rootPane.getChildren().remove(fakePiece);
                // Gọi hàm cập nhật bàn cờ thật (để đảm bảo đồng bộ)
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            
            tt.play();
        });
    }
}