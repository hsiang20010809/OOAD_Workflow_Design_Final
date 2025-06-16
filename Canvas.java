// 導入必要的 Java AWT 和 Swing 套件
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Canvas 類別，繼承自 JPanel，用於處理圖形的繪製和互動
 */
class Canvas extends JPanel {
    private List<Shape> shapes = new ArrayList<>();// 儲存所有圖形的列表
    private List<Link> links = new ArrayList<>();// 儲存所有連線的列表
    private Shape startShape = null;// 記錄連線起始的圖形
    private Point startPort = null;// 記錄連線的起始連接點
    private Shape selectedShape = null;// 目前選取的圖形
    private LinkType currentLinkType = LinkType.ASSOCIATION;// 目前的連線類型，預設為關聯關係
    private Point selectionStartPoint = null;// 記錄框選的起始點
    private Rectangle selectionRect = null;// 框選時的矩形範圍
    private boolean isDragging = false; // 判斷是否正在拖曳圖形
    private Point dragStartPoint = null; // 記錄拖曳的起始點座標

    /**
     * 定義畫布的操作模式
     * RECT: 繪製矩形模式
     * OVAL: 繪製橢圓模式
     * SELECT: 選取模式
     * LINK: 連線模式
     * NONE: 無動作模式
     */
    enum Mode {
        RECT, OVAL, SELECT, LINK, NONE
    }
    
    private Mode currentMode = Mode.NONE; // 預設為 NONE 模式

    /**
     * 新增圖形到畫布
     * @param shape 要新增的圖形
     */
    public void addShape(Shape shape) {
        clearSelection();// 清除目前的選取狀態
        shapes.add(shape);// 加入新圖形
        shapes.sort((s1, s2) -> Integer.compare(s2.getDepth(), s1.getDepth()));// 根據圖形深度排序(降序)，確保正確的繪製順序
        repaint();// 重新繪製畫布
    }

    /**
     * 將選取的多個圖形組合成一個複合圖形
     */
    public void groupSelectedShapes() {
        // 收集所有被選取的圖形
        List<Shape> selectedShapes = new ArrayList<>();
        for (Shape shape : shapes) {
            if (shape.isSelected()) {
                selectedShapes.add(shape);
            }
        }
        // 當選取超過一個圖形時才進行組合
        if (selectedShapes.size() > 1) {
            Composite composite = new Composite();
            // 將選取的圖形加入複合圖形中
            for (Shape shape : selectedShapes) {
                composite.addShape(shape);
            }
            shapes.removeAll(selectedShapes); // 從畫布移除原始圖形
            shapes.add(composite); // 加入組合後的複合圖形
            clearSelection();// 清除選取狀態
            composite.setSelected(true); // 選取新的複合圖形
            repaint();// 重新繪製畫布
        }
    }

    /**
     * 解散選取的複合圖形
     */
    public void ungroupSelectedComposite() {
        if (selectedShape instanceof Composite) {
            Composite composite = (Composite) selectedShape;
            List<Shape> childShapes = composite.getChildShapes();
            
            // 處理與 Composite 相關的連線
            List<Link> compositeLinks = new ArrayList<>();
            for (Link link : links) {
                if (link.getStartShape() == composite || link.getEndShape() == composite) {
                    compositeLinks.add(link);
                }
            }
            
            // 重新建立連線關聯
            for (Link link : compositeLinks) {
                // 從 composite 移除連線關聯
                composite.removeLink(link);
                
                if (link.getStartShape() == composite) {
                    // 找到最接近連線起點的子圖形
                    Shape closestChild = findClosestChildShape(childShapes, link.getStartPort());
                    if (closestChild != null) {
                        link.setStartShape(closestChild);
                        closestChild.addLink(link);
                    }
                }
                
                if (link.getEndShape() == composite) {
                    // 找到最接近連線終點的子圖形
                    Shape closestChild = findClosestChildShape(childShapes, link.getEndPort());
                    if (closestChild != null) {
                        link.setEndShape(closestChild);
                        closestChild.addLink(link);
                    }
                }
                
                // 更新連線的端點
                link.updatePorts();
            }
            
            // 移除 composite，加入子圖形
            shapes.remove(composite);
            shapes.addAll(childShapes);
            clearSelection();
            repaint();
        }
    }

    // 輔助方法：找到最接近指定點的子圖形
    private Shape findClosestChildShape(List<Shape> childShapes, Point point) {
        Shape closest = null;  
        double minDistance = Double.MAX_VALUE;
        
        for (Shape child : childShapes) {
            // 計算到圖形中心的距離
            Point center = child.getCenter();
            // 計算歐幾里得距離
            // pow平方，sqrt開根號
            double distance = Math.sqrt(Math.pow(center.x - point.x, 2) + Math.pow(center.y - point.y, 2));
            
            if (distance < minDistance) {
                minDistance = distance;
                closest = child;
            }
        }
        
        return closest;
    }


    /**
     * 覆寫 JPanel 的 paintComponent 方法，負責繪製所有圖形元素
     * @param g Graphics 物件，用於繪圖
     */
    @Override
    protected void paintComponent(Graphics g) {
        // 調用父類別的 paintComponent 方法，確保正確的繪製行為
        super.paintComponent(g);

        // 繪製所有圖形
        for (Shape shape : shapes) {
            // 呼叫每個圖形的 draw 方法，傳入是否被選取的狀態
            shape.draw(g, shape.isSelected());
        }
        // 繪製所有連線
        for (Link link : links) {
            // 繪製連線本身
            link.draw(g);
        }
        // 繪製框選矩形
        if (selectionRect != null) {
            g.setColor(Color.BLUE);
            // 將 Graphics 轉換為 Graphics2D 以使用進階繪圖功能
            // setStroke:設定虛線樣式：
            // - 線條寬度：1.0f
            // - 線條端點樣式：BasicStroke.CAP_BUTT
            // - 線條連接處樣式：BasicStroke.JOIN_MITER
            // - 10.0f:限制尖角的長度
            // - new float[]{5.0f}: 虛線模式（表示虛線段的長度）
            // - 0.0f: 虛線偏移量
            ((Graphics2D) g).setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
            g.drawRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);// 繪製框選矩形
        }
    }

    /**
     * 遞迴更新與指定圖形相關的所有連線
     * @param shape 要檢查的圖形
     */
    private void updateLinksForShape(Shape shape) {
        // 更新直接與該圖形相關的連線
        for (Link link : links) {
            if (link.getStartShape() == shape || link.getEndShape() == shape) {
                link.updatePorts();
            } 
        }
        
        // 如果是組合物件，遞迴處理其子圖形
        if (shape instanceof Composite) {
            Composite composite = (Composite) shape;
            for (Shape child : composite.getChildShapes()) {
                updateLinksForShape(child); // 遞迴呼叫
            }
        }
    }

    /**
     * Canvas 類別的建構函式
     * 初始化畫布並設定滑鼠事件監聽器
     * 處理所有的使用者互動，包括繪圖、選取、拖曳和連線等操作
     */
    public Canvas() {
        // 新增滑鼠事件監聽器，處理按下和放開事件
        addMouseListener(new MouseAdapter() {
            /**
             * 處理滑鼠按下事件
             * @param e MouseEvent 包含滑鼠事件的相關資訊
             */
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickPoint = e.getPoint();// 獲取點擊座標
                isDragging = false; // 初始化為非拖曳狀態

                // 根據當前模式執行不同操作
                if (currentMode == Mode.RECT) {
                    // 矩形模式：在點擊位置創建新的矩形
                    Rect rect = new Rect(clickPoint.x, clickPoint.y);
                    addShape(rect);// 將新建的矩形加入到圖形集合中
                } else if (currentMode == Mode.OVAL) {
                    // 橢圓模式：在點擊位置創建新的橢圓
                    Oval oval = new Oval(clickPoint.x, clickPoint.y);
                    addShape(oval);// 將新建的橢圓加入到圖形集合中
                } else if (currentMode == Mode.SELECT) {
                    // 選取模式
                    selectedShape = null; // 重置選中的圖形
                    boolean shapeClicked = false;// 標記是否點擊到圖形

                    // 從上層往下層檢查是否點擊到圖形
                    for (int i = shapes.size() - 1; i >= 0; i--) {
                        Shape shape = shapes.get(i);
                        // 檢查點擊位置是否在圖形內
                        if (shape.contains(clickPoint.x, clickPoint.y)) {
                            shapeClicked = true;

                            // 清除其他物件的選取狀態
                            clearSelection();

                            // 設定當前物件為選取狀態
                            selectedShape = shape;
                            shape.setSelected(true);

                            // 記錄拖曳的起始點，用於後續計算移動距離
                            dragStartPoint = clickPoint;

                            break;// 找到第一個被點擊的圖形後就停止搜尋
                        }
                    }
                    // 如果沒有點擊到任何物件，啟動框選模式
                    if (!shapeClicked) {
                        clearSelection();// 清除所有選取狀態
                        // 記錄框選的起始點
                        selectionStartPoint = clickPoint;
                        // 創建框選矩形，初始大小為 0
                        selectionRect = new Rectangle(clickPoint.x, clickPoint.y, 0, 0);
                    }
                // 連線模式：尋找起始連線的圖形和連接點
                } else if (currentMode == Mode.LINK) {
                    for (Shape shape : shapes) {
                        if (shape.contains(clickPoint.x, clickPoint.y)) {
                            startShape = shape;// 設定連線的起始圖形
                            startPort = shape.getClosestPort(clickPoint.x, clickPoint.y);// 獲取最近的連接點作為連線的起點
                            break;
                        }
                    }
                }
                repaint();// 重新繪製畫面
            }

            /**
             * 處理滑鼠放開事件
             * @param e MouseEvent 包含滑鼠事件的相關資訊
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentMode == Mode.SELECT) {
                    if (selectionRect != null) {
                        // 框選模式：選取框內的所有物件
                        for (Shape shape : shapes) {
                            // 檢查每個圖形是否與選取框相交
                            if (selectionRect.intersects(shape.getBounds())) {
                                shape.setSelected(true);// 設定在框選範圍內的圖形為選中狀態
                            } else {
                                shape.setSelected(false);// 清除框選範圍外的圖形的選中狀態
                            }
                        }
                        selectionRect = null;// 清除選取框
                        selectionStartPoint = null;// 清除框選起始點
                    } else if (isDragging && selectedShape != null) {
                        // 拖曳模式：更新所有與被拖曳圖形相關的連線
                        // 拖曳結束時，使用遞迴方法更新連線
                        updateLinksForShape(selectedShape);
                    }
                } else if (currentMode == Mode.LINK && startShape != null) {
                    // 連線模式：完成連線的建立
                    for (Shape shape : shapes) {
                        // 檢查滑鼠放開位置是否在某個圖形上，且不是起始圖形
                        if (shape.contains(e.getX(), e.getY()) && shape != startShape) {
                            Point endPort = shape.getClosestPort(e.getX(), e.getY());// 獲取目標圖形最近的連接點
                            Link link = new Link(startShape, startPort, shape, endPort, currentLinkType);// 創建新的連線
                            links.add(link); //加入到連線集合中
                            //將 連線也加入到相關圖形的連線列表
                            startShape.addLink(link);
                            shape.addLink(link);
                            break;
                        }
                    }
                    // 清除連線的暫存資料
                    startShape = null;
                    startPort = null;
                }
                repaint();// 重新繪製畫面
                isDragging = false; // 重置拖曳狀態
            }
        });

        // 新增滑鼠移動事件監聽器，處理拖曳操作
        addMouseMotionListener(new MouseMotionAdapter() {
            /**
             * 處理滑鼠拖曳事件
             * @param e MouseEvent 包含滑鼠事件的相關資訊
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                isDragging = true; // 設定為拖曳狀態
                if (currentMode == Mode.SELECT && selectedShape != null && dragStartPoint != null) {
                    // 計算圖形需要移動的距離
                    int dx = e.getX() - dragStartPoint.x;
                    int dy = e.getY() - dragStartPoint.y;
                    // 移動選中的物件
                    selectedShape.move(dx, dy);
                    // 更新拖曳起始點
                    dragStartPoint = e.getPoint();
                    // 使用遞迴方法更新所有相關連線
                    updateLinksForShape(selectedShape);
                } else if (currentMode == Mode.SELECT && selectionRect != null && selectionStartPoint != null) {//框選模式
                    // 更新框選矩形的大小
                    // 計算矩形的左上角座標（取起始點和當前點的較小值）
                    int x = Math.min(selectionStartPoint.x, e.getX());
                    int y = Math.min(selectionStartPoint.y, e.getY());
                    // 計算矩形的寬度和高度（使用起始點和當前點的差值的絕對值）
                    int width = Math.abs(selectionStartPoint.x - e.getX());
                    int height = Math.abs(selectionStartPoint.y - e.getY());
                    // 設定選取框的新邊界
                    selectionRect.setBounds(x, y, width, height);
                }
                repaint();// 重新繪製畫面
            }
        });
    }

    /**
     * 清除所有圖形的選取狀態
     * 重置所有與選取和連線相關的暫存變數
     */
    private void clearSelection() {
        // 遍歷所有圖形，將其選取狀態設為 false
        for (Shape shape : shapes) {
            shape.setSelected(false);
        }
        // 重置所有相關的參考變數
        selectedShape = null;// 清除當前選中的圖形
        startShape = null;// 清除連線的起始圖形
        startPort = null;// 清除連線的起始連接點
    }
    /**
     * 設定當前的操作模式
     * 切換模式時會清除所有選取狀態並重繪畫面
     * @param mode 要設定的新操作模式（如：SELECT、RECT、OVAL、LINK 等）
     */
    public void setMode(Mode mode) {
        this.currentMode = mode; // 更新當前模式
        clearSelection();// 清除所有選取狀態
        repaint();// 重新繪製畫面
    }
    /**
     * 取得當前的操作模式 
     * @return 當前的操作模式
     */
    public Mode getMode() {
        return currentMode;
    }
    /**
     * 設定當前的連線類型
     * 用於決定新建立的連線的樣式
     * @param type 要設定的連線類型
     */
    public void setCurrentLinkType(LinkType type) {
        this.currentLinkType = type;
    }
    /**
     * 取得當前的連線類型
     * @return 當前的連線類型
     */
    public LinkType getCurrentLinkType() {
        return currentLinkType;
    }
    /**
     * 取得目前選中的圖形
     * @return 當前選中的圖形物件，如果沒有選中任何圖形則返回 null
     */
    public Shape getSelectedShape() {
        return selectedShape;
    }
}
