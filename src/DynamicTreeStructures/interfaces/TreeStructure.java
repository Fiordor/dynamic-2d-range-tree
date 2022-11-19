/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DynamicTreeStructures.interfaces;

import DynamicTreeStructures.structure.Node;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public interface TreeStructure<K extends Comparable<K>> {
    
    public Node<K> getRoot();
    
    public void insert(K data);
    
    public K search(K data);
    
    public K delete(K data);
}
