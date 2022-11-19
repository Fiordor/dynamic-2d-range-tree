/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DynamicTreeStructures.interfaces;

import DynamicTreeStructures.imagegenerator.ImageLabel;
import DynamicTreeStructures.imagegenerator.ImageLine;
import java.awt.Font;

/**
 *
 * @author Fiordor
 */
public interface TreeImage {
    
    public Font getFont();
    
    public ImageLine[] getLines();
    
    public ImageLabel[] getLabels();
    
    public int getWidth();
    
    public int getHeight();
}
