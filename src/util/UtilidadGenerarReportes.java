package util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

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
import modelo.Solicitud;
import modelo.Usuario;

/**
 * Created by pablo on 28/07/15.
 */
public class UtilidadGenerarReportes {

    static String TAG = "ExelLog";

    private List<Libro> listaLibros;
    private List<Solicitud> listaSolicitudes;
    private List<Usuario> listaUsuarios;
    private String rutaReporte = "";

    /**
     * Indica cual es el archivo que se generará
     * 1 = ListadoLibros;
     */
    private int tipoArchivo;

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();
    TareasGenerales tareasGenerales = new TareasGenerales();

    //Generar archivo excel
    public boolean saveExcelFile(Context context, String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;

        try {

            Activity activity = (Activity) context;
            TareaWsObtenerDatos datos = new TareaWsObtenerDatos();
            datos.setActivity(activity);
            datos.setFileName(fileName);
            datos.execute();
            success = true;

        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
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
        c.setCellValue("ACTIVO");
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
            c.setCellValue(listaLibros.get(i).getDisponibilidad());

        }

    }

    /**
     * Metodo encargado de generar el contenido del reporte para el caso de reservas
     * @param wb
     * @param cs
     */
    public void generarContenidoReservas(Workbook wb, CellStyle cs, CellStyle styleCabezera) {

        Cell c = null;

        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Reservas"); // (nombre de la hoja)

        //Encabezado del reporte
        Row rowCabezera = sheet1.createRow(0);
        c = rowCabezera.createCell(0);
        c.setCellValue("FUNDACION UNIVERSITARIA DE POPAYAN");
        c.setCellStyle(styleCabezera);

        rowCabezera = sheet1.createRow(1);
        c = rowCabezera.createCell(0);
        c.setCellValue("Reporte: Listado de reservas");

        // Fila vacia
        rowCabezera = sheet1.createRow(2);
        ///////////////////////////////////////Fin encabezado

        // Generate column headings
        Row row = sheet1.createRow(3);

        c = row.createCell(0);
        c.setCellValue("LIBRO");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("NOMBRES USUARIO");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("APELLIDOS USUARIO");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("COD. USUARIO");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("FECHA SOLICITUD");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("FECHA RESERVA");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("FECHA ENTREGA");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("ESTADO");
        c.setCellStyle(cs);

        for (int i = 0; i < 9; i++){
            sheet1.setColumnWidth(i, (15 * 500));
        }

        for (int i = 0; i < listaSolicitudes.size(); i++) {

            /////////Incrementa en 4 para no sobreescribir la fila de encabezado (titulos)
            Row rowValue = sheet1.createRow(i + 4);

            c = rowValue.createCell(0);
            if(listaSolicitudes.get(i).getLibro() != null){
                c.setCellValue(listaSolicitudes.get(i).getLibro().getTitulo());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(1);
            if(listaSolicitudes.get(i).getUsuario() != null){
                c.setCellValue(listaSolicitudes.get(i).getUsuario().getPrimerNombre()+" "+
                        listaSolicitudes.get(i).getUsuario().getSegundoNombre());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(2);
            if(listaSolicitudes.get(i).getUsuario() != null){
                c.setCellValue(listaSolicitudes.get(i).getUsuario().getPrimerApellido()+" "+
                        listaSolicitudes.get(i).getUsuario().getSegundoApellido());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(3);
            if(listaSolicitudes.get(i).getUsuario() != null){
                c.setCellValue(listaSolicitudes.get(i).getUsuario().getCodigo());
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(4);
            if(listaSolicitudes.get(i).getFechaSolicitud() != null) {
                c.setCellValue(Utilidades.formatoFechaYYYYMMDD.format(listaSolicitudes.get(i).getFechaSolicitud()));
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(5);
            if(listaSolicitudes.get(i).getFechaReserva() != null) {
                c.setCellValue(Utilidades.formatoFechaYYYYMMDD.format(listaSolicitudes.get(i).getFechaReserva()));
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(6);
            if(listaSolicitudes.get(i).getFechaEntrega() != null) {
                c.setCellValue(Utilidades.formatoFechaYYYYMMDD.format(listaSolicitudes.get(i).getFechaEntrega()));
            }else{
                c.setCellValue("");
            }

            c = rowValue.createCell(7);
            c.setCellValue(listaSolicitudes.get(i).getEstado());

        }

    }

    /**
     * Metodo encargado de generar el contenido del reporte para el caso de reservas
     * @param wb
     * @param cs
     */
    public void generarContenidoListaUsuarios(Workbook wb, CellStyle cs, CellStyle styleCabezera) {

        Cell c = null;

        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Usuarios"); // (nombre de la hoja)

        //Encabezado del reporte
        Row rowCabezera = sheet1.createRow(0);
        c = rowCabezera.createCell(0);
        c.setCellValue("FUNDACION UNIVERSITARIA DE POPAYAN");
        c.setCellStyle(styleCabezera);

        rowCabezera = sheet1.createRow(1);
        c = rowCabezera.createCell(0);
        c.setCellValue("Reporte: Listado de usuarios");

        // Fila vacia
        rowCabezera = sheet1.createRow(2);
        ///////////////////////////////////////Fin encabezado

        // Generate column headings
        Row row = sheet1.createRow(3);

        c = row.createCell(0);
        c.setCellValue("No. IDENTIFICACION");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("NOMBRES");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("APELLIDOS");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("TELEFONO");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("DIRECCION");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("EMAIL");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("CODIGO");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("ROL");
        c.setCellStyle(cs);

        for (int i = 0; i < 9; i++){
            sheet1.setColumnWidth(i, (15 * 500));
        }

        for (int i = 0; i < listaUsuarios.size(); i++) {

            /////////Incrementa en 4 para no sobreescribir la fila de encabezado (titulos)
            Row rowValue = sheet1.createRow(i + 4);

            c = rowValue.createCell(0);
            c.setCellValue(listaUsuarios.get(i).getCedula());

            c = rowValue.createCell(1);
            c.setCellValue(listaUsuarios.get(i).getPrimerNombre()+" "+
                    listaUsuarios.get(i).getSegundoNombre());

            c = rowValue.createCell(2);
                c.setCellValue(listaUsuarios.get(i).getPrimerApellido()+" "+
                        listaUsuarios.get(i).getSegundoApellido());

            c = rowValue.createCell(3);
            c.setCellValue(listaUsuarios.get(i).getTelefono());

            c = rowValue.createCell(4);
            c.setCellValue(listaUsuarios.get(i).getDireccion());

            c = rowValue.createCell(5);
            c.setCellValue(listaUsuarios.get(i).getEmail());

            c = rowValue.createCell(6);
            c.setCellValue(listaUsuarios.get(i).getCodigo());

            c = rowValue.createCell(7);
            c.setCellValue(listaUsuarios.get(i).getRol());
        }

    }

    /**
     * ProgressBar (circular)
     * @param activity
     */
    public void lanzarProgressBar(final Activity activity){
        final ProgressDialog dialogo = ProgressDialog.show(activity, "Espere", "......");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                    dialogo.dismiss();

                    //Se lanza el mensaje de alerta. (Controlador para el manejo de hilos)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showAlert(activity, "Ruta reporte: "+rutaReporte);
                        }
                    });

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * AlertDialog para notificacion de mensajes
     * @param activity
     * @param message
     */
    public static void showAlert(Activity activity, String message) {

        TextView title = new TextView(activity);
        title.setText("Notificación");
        title.setPadding(10, 10, 10, 10);
        //title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // builder.setTitle("Title");
        builder.setCustomTitle(title);
        // builder.setIcon(R.drawable.alert_36);

        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Tarea encargada de listar los libros de la biblioteca ya sea el listado general
     * o un listado segun parametros de un libro a buscar
     */
    private class TareaWsObtenerDatos extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;
        ProgressDialog dialogo = null;
        File file = null;
        FileOutputStream os = null;

        Activity activity = null;
        String fileName = "reporteFup.xls";

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogo = ProgressDialog.show(activity, "Espere", "......");
                    }
                });


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



                // Create a path where we will place our List of objects on external storage
                // Crear una ruta en la que vamos a poner nuestra Lista de objetos en el almacenamiento externo
                file = new File(activity.getExternalFilesDir(null), fileName);

                rutaReporte = file.toString();

                switch (tipoArchivo){
                    case 1:
                        //Reporte listadoLibros
                        listaLibros = tareasGenerales.buscarLibros(variablesGlobales.getLibroBuscar());
                        generarContenidoListaLibros(wb, cs, styleCabezera);
                        Log.i("Reporte",">>>>>>>>>>> Tamaño lista libros reporte: "+listaLibros.size());
                        break;
                    case 2:
                        //Reporte reservas
                        listaSolicitudes = tareasGenerales.buscarSolicitudes(variablesGlobales.getSolicitudBuscar());
                        generarContenidoReservas(wb, cs, styleCabezera);
                        break;
                    case 3:
                        //reporte usuarios
                        listaUsuarios = tareasGenerales.buscarUsuarios(variablesGlobales.getUsuarioBuscar());
                        generarContenidoListaUsuarios(wb, cs, styleCabezera);
                        break;
                }

                //Se escriben los datos en el reporte
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file" + file);

            } catch (IOException e) {
                resultadoTarea = false;
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                resultadoTarea = false;
                Log.w("FileUtils", "Failed to save file", e);
                Log.e("Reporte ", "xxx Error TareaWsObtenerDatos: " + e.getMessage());
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception ex) {
                }
            }

            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogo.dismiss(); //Se cierra el dialog (se desecha)
                            showAlert(activity, "Ruta reporte: "+rutaReporte);
                        }
                    });
                }catch (Exception e){
                    Log.e("Reporte","XXX Error TareaWsObtenerDatos: "+e.getMessage());
                }
            }
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }


    public void setTipoArchivo(int tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }
}
