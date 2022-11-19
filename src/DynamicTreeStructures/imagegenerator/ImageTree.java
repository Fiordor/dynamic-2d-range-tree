/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DynamicTreeStructures.imagegenerator;

import DynamicTreeStructures.interfaces.TreeImage;
import DynamicTreeStructures.interfaces.TreeStructure;
import DynamicTreeStructures.structure.Node;
import DynamicTreeStructures.structure.RedBlackTree;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fiordor
 */
public class ImageTree<K extends Comparable<K>> implements TreeImage {

    private ImageNode[][] nodeMatrix;
    private int width;
    private int height;

    private Font font;

    private BufferedImage calcBoundsImg;
    private Graphics2D calcBoundsG2D;
    private FontMetrics calcBoundsFontMetrics;

    public ImageTree(TreeStructure<K> tree) {
        this(tree, 4, 16, new Font(Font.MONOSPACED, Font.PLAIN, 16));
    }

    public ImageTree(TreeStructure<K> tree, int gapWidth, int gapHeight) {
        this(tree, gapWidth, gapHeight, new Font(Font.MONOSPACED, Font.PLAIN, 16));
    }

    public ImageTree(TreeStructure<K> tree, int gapWidth, int gapHeight, Font font) {

        this.font = font;

        this.calcBoundsImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        this.calcBoundsG2D = this.calcBoundsImg.createGraphics();
        this.calcBoundsFontMetrics = this.calcBoundsG2D.getFontMetrics(this.font);

        List< List<ImageNode>> rawMatrix = new ArrayList<>();

        if (tree != null) {
            next(null, tree.getRoot(), 0, rawMatrix);

            nodeMatrix = new ImageNode[rawMatrix.size()][];
            for (int i = 0; i < nodeMatrix.length; i++) {
                nodeMatrix[i] = new ImageNode[rawMatrix.get(i).size()];
                for (int j = 0; j < nodeMatrix[i].length; j++) {
                    nodeMatrix[i][j] = rawMatrix.get(i).get(j);
                }
            }

            assign(gapWidth, gapHeight);
        } else {
            nodeMatrix = null;
        }
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public ImageLine[] getLines() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ImageLabel[] getLabels() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Asigna la posición y las dimensiones de los nodos. Primero se obtiene el
     * máximo ancho que ocupa la representación gráfica del árbol de la fila con
     * más datos. Para cada fila del árbol primero se saca las dimensiones del
     * nodo, con las dimensiones se calcula la posición. Siempre se tiene en
     * cuenta los márgenes indicados. Al final se asigna el alto de la imangen.
     *
     * @param gapWidth espacio entre los datos de una misma fila.
     * @param gapHeight espacio entre la columnas.
     */
    private void assign(int gapWidth, int gapHeight) {

        width = 0;
        height = 0;

        int maxLabelHeight = 0;
        int maxLabelWidth = 0;

        for (int i = 0; i < nodeMatrix.length; i++) {
            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];

                int[] bounds = stringBounds(node.value);
                node.w = bounds[0];
                node.h = bounds[1];

                if (node.w > maxLabelWidth) {
                    maxLabelWidth = node.w;
                }
                if (node.h > maxLabelHeight) {
                    maxLabelHeight = node.h;
                }
            }
        }

        int nodesCant = (int) Math.pow(2, nodeMatrix.length - 1);
        width = (nodesCant * maxLabelWidth) + ((nodesCant * gapWidth) - gapWidth);
        int top = maxLabelHeight;
        nodeMatrix[0][0].x = (width / 2) - (nodeMatrix[0][0].w / 2);
        nodeMatrix[0][0].y = top;
        top += gapHeight;

        for (int i = 1; i < nodeMatrix.length; i++) {

            int cellWidth = (int) (width / Math.pow(2, i));
            int left = 0;
            top += maxLabelHeight;

            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];

                boolean right = node.value.compareTo(node.parent.value) > 0;
                int parentX = node.parent.x;

                while (left + cellWidth < parentX) {
                    left += cellWidth;
                }
                if (right) {
                    left += cellWidth;
                }

                node.x = left + (cellWidth / 2) - (node.w / 2);
                node.y = top;

            }
            top += gapHeight;
        }
        top -= gapHeight;

        height = top;
    }

    /**
     * Añade elementos a un array bidimensional dinámico. Si la fila no existe,
     * se crea. Se añade el nodo al array y si no es la raíz se le pone al nodo
     * la referencia de su padre. Luego se continua con los nodos hijos del
     * nodo.
     *
     * @param parent nodo padre parseado al ImageNode || null para el nodo root.
     * @param child nodo que se inserta en el array bidimensional.
     * @param deep profundidad del árbol.
     * @param matrix array bidimensional donde se añaden los nuevos nodos.
     */
    private void next(ImageNode parent, Node<K> child, int deep, List<List<ImageNode>> matrix) {

        if (deep >= matrix.size()) {
            matrix.add(new ArrayList<>());
        }

        ImageNode node = parse(child);
        matrix.get(deep).add(node);

        if (parent != null) {
            node.parent = parent;
        }

        if (child.getLeft() != null) {
            next(node, child.getLeft(), deep + 1, matrix);
        }

        if (child.getRight() != null) {
            next(node, child.getRight(), deep + 1, matrix);
        }
    }

    /**
     * Calculate bounds needs a string into image.
     *
     * @param text label to print into the image
     * @return int[2] where 0 is width and 1 is height
     */
    private int[] stringBounds(String text) {

        Rectangle2D bounds = this.calcBoundsFontMetrics.getStringBounds(text, this.calcBoundsG2D);
        double rawWidth = bounds.getWidth();
        double rawHeight = bounds.getHeight();
        int w = rawWidth % 1 == 0 ? (int) rawWidth : (int) rawWidth + 1;
        int h = rawHeight % 1 == 0 ? (int) rawHeight : (int) rawHeight + 1;
        return new int[]{w, h};
    }

    private ImageNode parse(Node<K> node) {
        return new ImageNode(node.getData().toString());
    }
}
