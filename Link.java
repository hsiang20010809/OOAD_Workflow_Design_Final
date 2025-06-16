import java.awt.*;

/**
 * 定義連線的類型枚舉
 */
enum LinkType {
    ASSOCIATION, // 箭頭
    GENERALIZATION, // 大箭頭
    COMPOSITION // 菱形箭頭
}  
// Link 類別用於表示圖形之間的連線
class Link {
    private Shape startShape;// 連線的起始圖形
    private Point startPort;// 連線的起始連接點
    private Shape endShape;// 連線的終點圖形
    private Point endPort;// 連線的終點連接點
    private LinkType type; // 連線的類型（關聯、繼承、組合）

    // 建構子：初始化連線的所有屬性
    public Link(Shape startShape, Point startPort, Shape endShape, Point endPort, LinkType type) {
        this.startShape = startShape;
        this.startPort = startPort;
        this.endShape = endShape;
        this.endPort = endPort;
        this.type = type;
    }

    // 繪製連線及其箭頭
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);// 設定繪製顏色為黑色
        g.drawLine(startPort.x, startPort.y, endPort.x, endPort.y);// 繪製連線主體（直線）

        // 根據連線類型繪製不同樣式的箭頭
        switch (type) {
            case ASSOCIATION: // 關聯：一般箭頭
                drawArrow(g, endPort.x, endPort.y, startPort.x, startPort.y);
                break;
            case GENERALIZATION: // 繼承：三角形箭頭
                drawTriangleArrow(g, endPort.x, endPort.y, startPort.x, startPort.y);
                break;
            case COMPOSITION:    // 組合：菱形箭頭
                drawDiamondArrow(g, endPort.x, endPort.y, startPort.x, startPort.y);
                break;
        }
    }

    // 更新連線的連接點位置
    public void updatePorts() {
        // 更新起始連接點
        if (startShape != null) {
            // 無論是組合圖形還是一般圖形，都取得最近的連接點
            if (startShape instanceof Composite) {
                startPort = startShape.getClosestPort(startPort.x, startPort.y);
            } else {
                startPort = startShape.getClosestPort(startPort.x, startPort.y);
            }
        }
    
        // 更新終點連接點
        if (endShape != null) {
            // 無論是組合圖形還是一般圖形，都取得最近的連接點
            if (endShape instanceof Composite) {
                endPort = endShape.getClosestPort(endPort.x, endPort.y);
            } else {
                endPort = endShape.getClosestPort(endPort.x, endPort.y);
            }
        }
    }

    // 設定起始圖形
    public void setStartShape(Shape startShape) {
        this.startShape = startShape;
    }

    // 設定終點圖形
    public void setEndShape(Shape endShape) {
        this.endShape = endShape;
    }

    // 取得起始圖形
    public Shape getStartShape() {
        return startShape;
    }

    // 取得終點圖形
    public Shape getEndShape() {
        return endShape;
    }
    
    // 取得起始連接點
    public Point getStartPort() {
        return startPort;
    }

    // 取得終點連接點
    public Point getEndPort() {
        return endPort;
    }
    
    /**
     * 繪製關聯關係的箭頭（一般箭頭）
     * @param g Graphics 物件用於繪圖
     * @param x1, y1 箭頭的頂點座標
     * @param x2, y2 用於計算箭頭方向的參考點座標
     */
    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {//x1, y1為EndPort.xy
        // 設定箭頭大小
        int arrowSize = 10;

        // 計算箭頭的角度（使用反正切函數）
        // y1-y2 和 x1-x2 分別是箭頭方向的垂直和水平分量
        double angle = Math.atan2(y1 - y2, x1 - x2);

        // 計算箭頭左側線段的終點
        // angle + Math.PI/6 表示向左偏移30度
        int x3 = (int) (x1 - arrowSize * Math.cos(angle + Math.PI / 6));
        int y3 = (int) (y1 - arrowSize * Math.sin(angle + Math.PI / 6));

        // 計算箭頭右側線段的終點
        // angle - Math.PI/6 表示向右偏移30度
        int x4 = (int) (x1 - arrowSize * Math.cos(angle - Math.PI / 6));
        int y4 = (int) (y1 - arrowSize * Math.sin(angle - Math.PI / 6));

        // 繪製箭頭的兩條線段
        g.drawLine(x1, y1, x3, y3); // 左側線段
        g.drawLine(x1, y1, x4, y4); // 右側線段
    }

    /**
     * 繪製繼承關係的箭頭（空心三角形）
     * @param g Graphics 物件用於繪圖
     * @param x1, y1 箭頭的頂點座標
     * @param x2, y2 用於計算箭頭方向的參考點座標
     */
    private void drawTriangleArrow(Graphics g, int x1, int y1, int x2, int y2) {//x1, y1為EndPort.xy
        int arrowSize = 10;
        double angle = Math.atan2(y1 - y2, x1 - x2);// 計算箭頭方向的角度

        // 計算三角形的兩個底角座標
        int x3 = (int) (x1 - arrowSize * Math.cos(angle + Math.PI / 6));
        int y3 = (int) (y1 - arrowSize * Math.sin(angle + Math.PI / 6));
        int x4 = (int) (x1 - arrowSize * Math.cos(angle - Math.PI / 6));
        int y4 = (int) (y1 - arrowSize * Math.sin(angle - Math.PI / 6));

        // 創建多邊形的頂點陣列
        int[] xPoints = {x1, x3, x4}; // X座標陣列
        int[] yPoints = {y1, y3, y4}; // Y座標陣列

        // 繪製空心三角形
        g.drawPolygon(xPoints, yPoints, 3); // 3表示三個頂點
    }

    /**
     * 繪製組合關係的箭頭（菱形）
     * @param g Graphics 物件用於繪圖
     * @param x1, y1 箭頭的頂點座標
     * @param x2, y2 用於計算箭頭方向的參考點座標
     */
    private void drawDiamondArrow(Graphics g, int x1, int y1, int x2, int y2) {//x1, y1為EndPort.xy
        int arrowSize = 10;
        double angle = Math.atan2(y1 - y2, x1 - x2);// 計算箭頭方向的角度

        // 計算菱形的兩個側邊點
        int x3 = (int) (x1 - arrowSize * Math.cos(angle + Math.PI / 6));
        int y3 = (int) (y1 - arrowSize * Math.sin(angle + Math.PI / 6));
        int x4 = (int) (x1 - arrowSize * Math.cos(angle - Math.PI / 6));
        int y4 = (int) (y1 - arrowSize * Math.sin(angle - Math.PI / 6));

        // 計算菱形的底部頂點
        int x5 = (int) (x3 - arrowSize * Math.cos(angle));
        int y5 = (int) (y3 - arrowSize * Math.sin(angle));

        // 創建菱形的頂點陣列
        int[] xPoints = {x1, x3, x5, x4};// X座標陣列
        int[] yPoints = {y1, y3, y5, y4};// Y座標陣列

        // 繪製菱形
        g.drawPolygon(xPoints, yPoints, 4);// 4表示四個頂點
    }
}
