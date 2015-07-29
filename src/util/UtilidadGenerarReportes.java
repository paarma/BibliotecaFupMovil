package util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import modelo.Libro;

/**
 * Created by pablo on 28/07/15.
 */
public class UtilidadGenerarReportes {

    static String TAG = "ExelLog";

    private List<Libro> listaLibros;

    /**
     * Indica cual es el archivo que se generará
     * 1 = ListadoLibros;
     */
    private int tipoArchivo;

    //Generar archivo excel
    public boolean saveExcelFile(Context context, String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        // nuevo libro de trabajo
        Workbook wb = new HSSFWorkbook();

        //Cell style for header row
        // Estilo de celda para encabezado
        CellStyle cs = wb.createCellStyle();

        cs.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        Font font = wb.createFont();//Create font
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);//Make font bold //Negrilla

        cs.setFont(font);//set it to bold

        CellStyle styleCabezera = wb.createCellStyle();
        styleCabezera.setFont(font); //Se setea el fond para la cabezera o titulo del reporte

        switch (tipoArchivo){
            case 1:
                generarContenidoListaLibros(wb, cs, styleCabezera);
                break;
            case 2:
                break;
        }

        // Create a path where we will place our List of objects on external storage
        // Crear una ruta en la que vamos a poner nuestra Lista de objetos en el almacenamiento externo
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;

    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    /**
     * Metodo encargado de generar el contenido del reporte para el caso de ListaLibros
     * @param wb
     * @param cs
     */
    public void generarContenidoListaLibros(Workbook wb, CellStyle cs, CellStyle styleCabezera){

        Cell c = null;

        //New Sheet
        // nueva hoja
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Libros"); // (nombre de la hoja)


        //Encabezado del reporte
        Row rowCabezera = sheet1.createRow(0);
        c = rowCabezera.createCell(0);
        c.setCellValue("FUNDACION UNIVERSITARIA DE POPAYAN");
        c.setCellStyle(styleCabezera);

        rowCabezera = sheet1.createRow(1);
        c = rowCabezera.createCell(0);
        c.setCellValue("Reporte: Listado de libros");

        // Fila vacia
        rowCabezera = sheet1.createRow(2);
        ///////////////////////////////////////Fin encabezado


        // Generate column headings
        // Generando titulo a las columnas
        Row row = sheet1.createRow(3);

        c = row.createCell(0);
        c.setCellValue("TITULO");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("VALOR");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("ADQUISICION");
        c.setCellStyle(cs);

//        c = row.createCell(3);
//        c.setCellValue("ESTADO");
//        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("ISBN");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("RADICADO");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("FECHA INGRESO");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("COD. TOPOGRAFICO");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("SERIE");
        c.setCellStyle(cs);

        c = row.createCell(8);
        c.setCellValue("SEDE");
        c.setCellStyle(cs);

        c = row.createCell(9);
        c.setCellValue("EDITORIAL");
        c.setCellStyle(cs);

        c = row.createCell(10);
        c.setCellValue("AREA");
        c.setCellStyle(cs);

        c = row.createCell(11);
        c.setCellValue("AÑO");
        c.setCellStyle(cs);

        c = row.createCell(12);
        c.setCellValue("TEMAS");
        c.setCellStyle(cs);

        c = row.createCell(13);
        c.setCellValue("PAGINAS");
        c.setCellStyle(cs);

//        c = row.createCell(15);
//        c.setCellValue("USUARIO REGISTRO");
//        c.setCellStyle(cs);

        c = row.createCell(14);
        c.setCellValue("CIUDAD");
        c.setCellStyle(cs);

        c = row.createCell(15);
        c.setCellValue("CANTIDAD");
        c.setCellStyle(cs);

        for (int i = 0; i < 17; i++){ // campos originales 18
            sheet1.setColumnWidth(i, (15 * 500));
        }

        for (int i = 0; i < listaLibros.size(); i++){

            /////////Incrementa en 4 para no sobreescribir la fila de encabezado (titulos)
            Row rowValue = sheet1.createRow(i+4);

            c = rowValue.createCell(0);
            c.setCellValue(listaLibros.get(i).getTitulo());

            c = rowValue.createCell(1);
            c.setCellValue(listaLibros.get(i).getValor());

            c = rowValue.createCell(2);
            c.setCellValue(listaLibros.get(i).getAdquisicion());

//            c = rowValue.createCell(3);
//            c.setCellValue(listaLibros.get(i).getEstado());

            c = rowValue.createCell(3);
            c.setCellValue(listaLibros.get(i).getIsbn());

            c = rowValue.createCell(4);
            c.setCellValue(listaLibros.get(i).getRadicado());

            c = rowValue.createCell(5);
            if(listaLibros.get(i).getFechaIngreso() != null) {
                c.setCellValue(Utilidades.formatoFechaYYYYMMDD.format(listaLibros.get(i).getFechaIngreso()));
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(6);
            c.setCellValue(listaLibros.get(i).getCodigoTopografico());

            c = rowValue.createCell(7);
            c.setCellValue(listaLibros.get(i).getSerie());

            c = rowValue.createCell(8);
            if(listaLibros.get(i).getSede() != null){
                c.setCellValue(listaLibros.get(i).getSede().getDescripcion());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(9);
            if(listaLibros.get(i).getEditorial() != null){
                c.setCellValue(listaLibros.get(i).getEditorial().getDescripcion());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(10);
            if(listaLibros.get(i).getArea() != null){
                c.setCellValue(listaLibros.get(i).getArea().getDescripcion());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(11);
            c.setCellValue(listaLibros.get(i).getAnio());

            c = rowValue.createCell(12);
            c.setCellValue(listaLibros.get(i).getTemas());

            c = rowValue.createCell(13);
            c.setCellValue(listaLibros.get(i).getPaginas());

//            c = rowValue.createCell(14);
//            c.setCellValue(listaLibros.get(i).getIdUsuario());

            c = rowValue.createCell(14);
            if(listaLibros.get(i).getCiudad() != null){
                c.setCellValue(listaLibros.get(i).getCiudad().getNombre());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(15);
            c.setCellValue(listaLibros.get(i).getCantidad());

        }

    }

    //Setters
    public void setListaLibros(List<Libro> listaLibros) {
        this.listaLibros = listaLibros;
    }

    public void setTipoArchivo(int tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }
}
