/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DynamicTreeStructures.interfaces;

/**
 *
 * @author Fiordor
 */
public interface TreeOp<K> {
    
    public void insert(K data);
    
    public K search(K data);
    
    public K delete(K data);
}
