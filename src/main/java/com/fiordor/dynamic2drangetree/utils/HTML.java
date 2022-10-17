package com.fiordor.dynamic2drangetree.utils;

import com.fiordor.dynamic2drangetree.structure.Point;

public class HTML {

    public static String parsePoint(Point p) {

        String x = p.getX().toString();
        String y = p.getY().toString();

        return new StringBuilder()
            .append("<div class=\"row-point\">\n")
            .append("<div class=\"point-l\">").append(x).append("</div>\n")
            .append("<div class=\"point-r\">").append(y).append("</div>\n")
            .append("</div>").toString();
    }
    
}
