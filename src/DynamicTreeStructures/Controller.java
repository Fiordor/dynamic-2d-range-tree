package DynamicTreeStructures;

import DynamicTreeStructures.imagegenerator.Image;
import DynamicTreeStructures.imagegenerator.ImageRedBlackTree;
import DynamicTreeStructures.imagegenerator.ImageTree;
import DynamicTreeStructures.interfaces.TreeImage;
import DynamicTreeStructures.interfaces.TreeStructure;
import DynamicTreeStructures.structure.RedBlackTree;
import DynamicTreeStructures.structure.RootedBinaryTree;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Fiordor
 */
public class Controller {

    public final static int BALANCED_BINARY_TREE = 0;
    public final static int COMPLETE_BINARY_TREE = 1;
    public final static int DEGENERATE_BINARY_TREE = 2;
    public final static int PERFECT_BINARY_TREE = 3;
    public final static int ROOTED_BINARY_TREE = 4;
    public final static int RED_BLACK_TREE = 5;

    private int type;

    private TreeStructure<Integer> tree;
    private RedBlackTree<Integer> redBlackTree;

    private DefaultListModel<String> list;

    private Canvas canvas;
    private BufferedImage image;

    private int canvasX;
    private int canvasY;
    private double zoom;

    public Controller(JPanel panel) {
        this(panel, RED_BLACK_TREE);
    }

    public Controller(JPanel panel, int type) {

        setTreeType(type);
        this.list = new DefaultListModel<>();
        this.canvas = new Canvas();
        this.zoom = 1.0;

        canvas.setBackground(Color.WHITE);

        canvas.setBounds(0, 0, panel.getWidth(), panel.getHeight());
        panel.add(canvas);
    }

    public void setTreeType(int type) {
        this.type = type;

        this.redBlackTree = null;
        this.tree = null;

        switch (type) {
            case BALANCED_BINARY_TREE:
                break;
            case COMPLETE_BINARY_TREE:
                break;
            case DEGENERATE_BINARY_TREE:
                break;
            case PERFECT_BINARY_TREE:
                break;
            case ROOTED_BINARY_TREE:
                this.tree = new RootedBinaryTree<>();
                break;
            case RED_BLACK_TREE:
                this.redBlackTree = new RedBlackTree<>();
                break;
        }
    }

    public DefaultListModel<String> getList() {
        return list;
    }

    public void setSize(int w, int h) {
        canvas.setSize(w, h);
    }

    public void add(String input) {
        if (input.length() == 0) {
            return;
        }
        int value = Integer.parseInt(input);

        if (redBlackTree == null) {
            tree.insert(value);
        } else {
            redBlackTree.insert(value);
        }

        list.addElement(input);

        print();
    }

    public void clear() {
        this.canvas.clear();
        this.canvas.repaint();

        this.redBlackTree = new RedBlackTree<>();
        this.list.clear();
        this.zoom = 1.0;
    }

    public void search(String input) {
        if (input.length() == 0) {
            return;
        }
    }

    public void delete(String input) {
        if (input.length() == 0) {
            return;
        }
    }

    public void generate(String input) {
        if (input.length() == 0) {
            return;
        }
        int k = Integer.parseInt(input);

        Random r = new Random();
        for (int i = 0; i < k; i++) {
            int random = r.nextInt(1000);
            while (list.contains(String.valueOf(random))) {
                random = r.nextInt(1000);
            }
            list.addElement(String.valueOf(random));
            if (redBlackTree == null) {
                tree.insert(random);
            } else {
                redBlackTree.insert(random);
            }
        }

        print();
    }

    public void movePressed(int x, int y) {
        canvasX = x - canvas.getPoint()[0];
        canvasY = y - canvas.getPoint()[1];
    }

    public void moveDragged(int x, int y) {
        canvas.setPoint(x - canvasX, y - canvasY);
        canvas.repaint();
    }

    public void moveReleased() {

    }

    public void zoom(int zoom) {

        this.zoom = this.zoom - (zoom / 10.0);

        int w = (int) Math.ceil(this.image.getWidth() * (this.zoom > 0 ? this.zoom : 1));
        int h = (int) Math.ceil(this.image.getHeight() * (this.zoom > 0 ? this.zoom : 1));
        BufferedImage scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = AffineTransform.getScaleInstance(this.zoom, this.zoom);
        AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaledImage = ato.filter(image, scaledImage);

        canvas.setImage(scaledImage);
        canvas.repaint();
    }

    private void print() {
        long start = System.currentTimeMillis();
        TreeImage treeImage = redBlackTree == null ? 
                new ImageTree<>(tree, 16, 16) :
                new ImageRedBlackTree<>(redBlackTree, 16, 16);
        long stop = System.currentTimeMillis();
        System.out.println("Create image: " + (stop - start) + " ms");
        
        start = System.currentTimeMillis();
        this.image = new Image(treeImage).create();
        stop = System.currentTimeMillis();
        
        System.out.println("print image: " + (stop - start) + " ms");

        canvas.setImage(this.image);
        canvas.repaint();
    }
}
