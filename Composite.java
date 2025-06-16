// 引入必要的 Java AWT 和集合類別
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Composite 類別實現組合模式，可以包含多個 Shape 物件
class Composite extends Shape {
    // 儲存所有子圖形的列表
    private List<Shape> childShapes = new ArrayList<>();

    // 建構子：初始化一個空的組合物件
    public Composite() {
        super(0, 0, 0); // 呼叫父類別建構子，但組合物件本身不需要位置和深度值
    }

    // 新增一個圖形到組合中
    public void addShape(Shape shape) {
        childShapes.add(shape);
    }

    // 從組合中移除指定的圖形
    public void removeShape(Shape shape) {
        childShapes.remove(shape);
    }

    // 取得所有子圖形的列表
    public List<Shape> getChildShapes() {
        return childShapes;
    }

    // 覆寫選取狀態設定方法
    @Override
    public void setSelected(boolean selected) {
        // 先設定組合本身的選取狀態
        super.setSelected(selected);
        
        // 將選取狀態同步到所有子圖形
        // 當群組的選取狀態改變時，確保組合中的所有圖形都具有相同的選取狀態
        for (Shape shape : childShapes) {
            shape.setSelected(selected);
        }
    }

    // 覆寫繪製方法
    @Override
    public void draw(Graphics g, boolean isSelected) {
        // 先設定組合的選取狀態
        setSelected(isSelected);
        
        // 依序繪製每個子圖形
        // 更新所有子物件的選取狀態，然後繪製
        for (Shape shape : childShapes) {
            shape.setSelected(isSelected);  // 更新子物件的選取狀態
            shape.draw(g, isSelected); // 繪製子圖形
        }
    }

    // 檢查滑鼠座標是否在組合內的任何圖形範圍內
    @Override
    public boolean contains(int mouseX, int mouseY) {
        for (Shape shape : childShapes) {
            // 如果滑鼠座標在任何一個子圖形內
            if (shape.contains(mouseX, mouseY)) {
                return true;
            }
        }
        return false; // 如果都不在任何子圖形內，回傳 false
    }

    // 取得組合中所有圖形的連接點
    @Override
    public List<Point> getConnectionPorts() {
        // 建立一個新的連接點列表
        List<Point> connectionPorts = new ArrayList<>();
        // 遍歷所有子圖形，收集它們的連接點
        for (Shape shape : childShapes) {
            connectionPorts.addAll(shape.getConnectionPorts());
        }
        return connectionPorts; // 回傳所有連接點的集合
    }

    // 計算組合的邊界範圍
    @Override
    public Rectangle getBounds() {
        // 如果組合是空的
        if (childShapes.isEmpty()) {
            return new Rectangle(0, 0, 0, 0); // 如果沒有子物件，返回空矩形
        }

        // 以第一個子圖形的邊界作為初始值
        Rectangle bounds = childShapes.get(0).getBounds();

        // 遍歷剩餘的子圖形，合併它們的邊界
        for (int i = 1; i < childShapes.size(); i++) {
            // 使用 union 方法合併邊界
            bounds = bounds.union(childShapes.get(i).getBounds());
        }

        return bounds; // 回傳最終的邊界範圍
    }

    @Override
    public void move(int dx, int dy) {
        // 遍歷所有子圖形
        for (Shape shape : childShapes) {
            // 移動每個子圖形
            shape.move(dx, dy);

            // 更新每個子圖形相關的所有連線
            for (Link link : shape.getLinks()) { 
                link.updatePorts(); // 更新連線的端點位置
            }
        }
    }

    // 取得組合的寬度
    @Override
    public int getWidth() {
        Rectangle bounds = getBounds();
        return bounds.width; // 回傳邊界的寬度
    }

    // 取得組合的高度
    @Override
    public int getHeight() {
        Rectangle bounds = getBounds();
        return bounds.height; // 回傳邊界的高度
    }
}
