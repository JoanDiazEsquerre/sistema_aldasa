package com.model.aldasa.util;

import java.awt.Color;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class UtilXls {
	public static CellStyle styleCell(HSSFWorkbook libro, char tipo) {
		CellStyle cellStyleDefault = libro.createCellStyle();
		HSSFFont fontRow = libro.createFont();
		fontRow.setFontHeightInPoints((short) 9);
		fontRow.setFontName("Calibri");
		if (tipo == 'B') {// CANTIDAD NORMALES {
			cellStyleDefault.setBorderBottom(BorderStyle.THIN);
			cellStyleDefault.setBorderTop(BorderStyle.THIN);
			cellStyleDefault.setBorderRight(BorderStyle.THIN);
			cellStyleDefault.setBorderLeft(BorderStyle.THIN);
		}

		if (tipo == 'A') {
			fontRow.setFontHeightInPoints((short) 13);
			cellStyleDefault.setAlignment(HorizontalAlignment.CENTER);
			cellStyleDefault.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyleDefault.setWrapText(true);
			HSSFPalette palette2 = libro.getCustomPalette();
			
			Color color = new Color(0,0,255);
			cellStyleDefault.setBorderBottom(BorderStyle.THIN);
			cellStyleDefault.setBorderTop(BorderStyle.THIN);
			cellStyleDefault.setBorderRight(BorderStyle.THIN);
			cellStyleDefault.setBorderLeft(BorderStyle.THIN);
		}

	

		
		cellStyleDefault.setFont(fontRow);

		return cellStyleDefault;
	}
}
