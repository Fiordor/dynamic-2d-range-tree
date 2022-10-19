package com.fiordor.dynamic2drangetree.utils;

import java.util.ArrayList;
import java.util.List;

import com.fiordor.dynamic2drangetree.redblacktree.Node;
import com.fiordor.dynamic2drangetree.redblacktree.RedBlackTree;

public class ImageRedBlackTree<K extends Comparable<K>> {

    private ImageNode[][] nodeMatrix;
    private int width;
    private int height;

    public ImageRedBlackTree(RedBlackTree<K> tree) {
        this(tree, 16, 16);
    }

    public ImageRedBlackTree(RedBlackTree<K> tree, int gapWidth, int gapHeight) {

        List<List<ImageNode>> rawMatrix = new ArrayList<>();

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

    public ImageLine[] getLines() {
        
        List<ImageLine> dynamicLines = new ArrayList<>();

        for (int i = 0; i < nodeMatrix.length; i++) {
            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];
                ImageNode parent = node.parent;
                if (parent != null) {
                    int x1 = parent.x + (parent.width / 2);
                    int y1 = parent.y - (parent.height / 2);
                    int x2 = node.x + (node.width / 2);
                    int y2 = node.y - (node.height / 2);

                    dynamicLines.add(new ImageLine(x1, y1, x2, y2));
                }
            }
        }

        ImageLine[] lines = new ImageLine[dynamicLines.size()];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = dynamicLines.get(i);
        }

        return lines;
    }

    public ImageLabel[] getLabels() {

        List<ImageLabel> dynamicLabels = new ArrayList<>();

        for (int i = 0; i < nodeMatrix.length; i++) {
            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];
                dynamicLabels.add(new ImageLabel(node.data, node.x, node.y, node.width, node.height));
            }
        }

        ImageLabel[] labels = new ImageLabel[dynamicLabels.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = dynamicLabels.get(i);
        }

        return labels;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    /**
     * Asigna la posición y las dimensiones de los nodos. Primero se obtiene el
     * máximo ancho que opcupa la representación gráfica del árbol. Para cada
     * fila del árbol primero se saca las dimensiones del nodo, con las dimensiones
     * se calcula la posición. Siempre se tiene en cuenta los márgenes indicados.
     * Al final se asigna el alto de la imangen.
     * 
     * @param gapWidth espacio entre los datos de una misma fila.
     * @param gapHeight espacio entre la columnas.
     */
    private void assign(int gapWidth, int gapHeight) {

        width = 0;
        height = 0;

        for (int i = 0; i < nodeMatrix.length; i++) {
            int rowWidth = getRowWidth(nodeMatrix[i]);
            if (nodeMatrix[i].length > 0) {
                rowWidth += (nodeMatrix[i].length * gapWidth ) - gapWidth;
            }
            
            if (rowWidth >= width) { width = rowWidth; }
        }

        int top = 0;
        for (int i = 0; i < nodeMatrix.length; i++) {

            int left = 0;
            int maxHeight = 0, maxWidth = 0;

            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];

                node.width = Image.stringWidth(node.data);
                node.height = Image.stringHeight(node.data);

                maxWidth += node.width;
            }

            maxWidth += (nodeMatrix[i].length * gapWidth) - gapWidth;
            left = width - maxWidth;
            left = left <= 0 ? 0 : left / 2;
            
            for (int j = 0; j < nodeMatrix[i].length; j++) {
                ImageNode node = nodeMatrix[i][j];

                node.x = left;
                node.y = top + node.height;

                left += node.width + gapWidth;
                if (node.height >= maxHeight) { maxHeight = node.height; }

            }
            top += maxHeight + gapHeight;
        }
        top -= gapHeight;

        height = top;
    }

    /**
     * Calcula los píxeles que ocupa la suma de strings de una fila en bruto,
     * sin márgenes
     * 
     * @param row array de los ImageNode que lee el data
     * @return suma de la longitud en píxeles de todos los strings
     */
    private int getRowWidth(ImageNode[] row) {

        int width = 0;
        for (int i = 0; i < row.length; i++) {
            width += Image.stringWidth(row[i].data);
        }
        return width;
    }

    /**
     * Añade elementos a un array bidimensional dinámico. Si la fila no existe,
     * se crea. Se añade el nodo al array y si no es la raíz se le pone al nodo
     * la referencia de su padre. Luego se continua con los nodos hijos del nodo.
     * 
     * @param parent nodo padre parseado al ImageNode || null para el nodo root.
     * @param child nodo que se inserta en el array bidimensional.
     * @param deep profundidad del árbol.
     * @param matrix array bidimensional donde se añaden los nuevos nodos.
     */
    private void next(ImageNode parent, Node<K> child, int deep, List<List<ImageNode>> matrix) {

        if (deep >= matrix.size()) { matrix.add(new ArrayList<>()); }

        ImageNode node = parse(child);
        matrix.get(deep).add(node);

        if (parent != null) { node.parent = parent; }

        if (child.getLeft() != null) {
            next(node, child.getLeft(), deep + 1, matrix);
        }

        if (child.getRight() != null) {
            next(node, child.getRight(), deep + 1, matrix);
        }
    }

    private ImageNode parse(Node<K> node) {
        return new ImageNode(node.getData().toString(), node.isBlack());
    }
}
