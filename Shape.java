import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

abstract class Shape {
    protected int x, y;
    protected int depth; // 深度屬性

    public Shape(int x, int y, int depth) {
        this.x = x;
        this.y = y;
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public abstract void draw(Graphics g, boolean isSelected);

    public abstract boolean contains(int mouseX, int mouseY);

    public abstract List<Point> getConnectionPorts();

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    // 新增抽象方法 getBounds()
    public abstract Rectangle getBounds();

    private boolean isSelected = false;

    private List<Link> links = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public Point getClosestPort(int mouseX, int mouseY) {
        List<Point> ports = getConnectionPorts();
        Point closestPort = null;
        double minDistance = Double.MAX_VALUE;
    
        for (Point port : ports) {
            double distance = port.distance(mouseX, mouseY);
            if (distance < minDistance) {
                minDistance = distance;
                closestPort = port;
            }
        }
    
        return closestPort;
    }

    // 標籤相關屬性
    protected String labelText = "";
    protected Color labelColor = Color.YELLOW;
    protected int fontSize = 12;
    protected boolean isRectLabel = true;  // true 為矩形，false 為橢圓形

    // 取得與設置標籤屬性的方法
    public String getLabelText() { return labelText; }
    public void setLabelText(String labelText) { this.labelText = labelText; }
    public Color getLabelColor() { return labelColor; }
    public void setLabelColor(Color labelColor) { this.labelColor = labelColor; }
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    public boolean isRectLabel() { return isRectLabel; }
    public void setRectLabel(boolean isRectLabel) { this.isRectLabel = isRectLabel; }

    // 新增抽象方法用於獲取形狀尺寸
    public abstract int getWidth();
    public abstract int getHeight();

    // 繪製標籤的方法
    protected void drawLabel(Graphics g) {
        if (labelText == null || labelText.isEmpty()) {
            return;  // 如果沒有標籤文字，不繪製
        }

        Graphics2D g2d = (Graphics2D) g.create();
        FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.PLAIN, fontSize));
        int textWidth = metrics.stringWidth(labelText);
        int textHeight = metrics.getHeight();
        
        int labelWidth = textWidth + 20;  // 標籤寬度 = 文字寬度 + 邊距
        int labelHeight = textHeight + 10;  // 標籤高度 = 文字高度 + 邊距
        
        int centerX = x + getWidth() / 2;  // 形狀中心 X
        int centerY = y + getHeight() / 2;  // 形狀中心 Y
        
        int labelX = centerX - labelWidth / 2;
        int labelY = centerY - labelHeight / 2;
        
        // 設置標籤背景顏色
        g2d.setColor(labelColor);
        
        // 根據設定繪製矩形或橢圓形標籤
        if (isRectLabel) {
            g2d.fillRect(labelX, labelY, labelWidth, labelHeight);
        } else {
            g2d.fillOval(labelX, labelY, labelWidth, labelHeight);
        }
        
        // 設置文字顏色和字體
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        
        // 計算文字位置使其居中
        int textX = labelX + (labelWidth - textWidth) / 2;
        int textY = labelY + (labelHeight - textHeight) / 2 + metrics.getAscent();
        
        // 繪製文字
        g2d.drawString(labelText, textX, textY);
        
        g2d.dispose();
    }  
}
