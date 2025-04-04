import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Composite extends Shape {
    private List<Shape> childShapes = new ArrayList<>();

    public Composite() {
        super(0, 0, 0); // Composite 不需要 x, y 和 depth
    }

    public void addShape(Shape shape) {
        childShapes.add(shape);
    }

    public void removeShape(Shape shape) {
        childShapes.remove(shape);
    }

    public List<Shape> getChildShapes() {
        return childShapes;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        
        // 當群組的選取狀態改變時，同步更新所有子物件
        for (Shape shape : childShapes) {
            shape.setSelected(selected);
        }
    }

    @Override
    public void draw(Graphics g, boolean isSelected) {
        // 先設定自己的選取狀態
        setSelected(isSelected);
        
        // 更新所有子物件的選取狀態，然後繪製
        for (Shape shape : childShapes) {
            shape.setSelected(isSelected);  // 更新子物件的選取狀態
            shape.draw(g, isSelected);
        }
    }

    @Override
    public boolean contains(int mouseX, int mouseY) {
        for (Shape shape : childShapes) {
            if (shape.contains(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Point> getConnectionPorts() {
        List<Point> connectionPorts = new ArrayList<>();
        for (Shape shape : childShapes) {
            connectionPorts.addAll(shape.getConnectionPorts());
        }
        return connectionPorts;
    }

    @Override
    public Rectangle getBounds() {
        if (childShapes.isEmpty()) {
            return new Rectangle(0, 0, 0, 0); // 如果沒有子物件，返回空矩形
        }

        // 初始化邊界為第一個子物件的邊界
        Rectangle bounds = childShapes.get(0).getBounds();

        // 合併所有子物件的邊界
        for (int i = 1; i < childShapes.size(); i++) {
            bounds = bounds.union(childShapes.get(i).getBounds());
        }

        return bounds;
    }

    @Override
    public void move(int dx, int dy) {
        // 移動所有子物件
        for (Shape shape : childShapes) {
            shape.move(dx, dy);

            // 更新與子物件相關的連線端點
            for (Link link : shape.getLinks()) { // 假設 Shape 有 getLinks 方法
                link.updatePorts();
            }
        }
    }

    @Override
    public int getWidth() {
        Rectangle bounds = getBounds();
        return bounds.width;
    }

    @Override
    public int getHeight() {
        Rectangle bounds = getBounds();
        return bounds.height;
    }

}
