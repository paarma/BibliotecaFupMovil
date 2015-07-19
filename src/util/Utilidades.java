package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pablo on 27/05/15.
 */
public class Utilidades {

    /**
     * Variable encargada de contener obtener el formato fecha "yyyy-MM-dd"
     */
    public static SimpleDateFormat formatoFechaYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Variables String para manejar ESTADOS de la reserva
     */
    public static String estadoEnProceso = "EN PROCESO";
    public static String estadoPrestado = "PRESTADO";
    public static String estadoCancelado = "CANCELADO";
    public static String estadoFinalizado = "FINALIZADO";
    public static String estadoEnMora = "EN MORA";

    /**
     * Variable entera para indicar el numero de dias por los que se puede prestar un libro.
     */
    public final static int diasTotalesPrestamo = 2;

    /**
     * Variable entera para indicar la cantidad minima de copias de un libro que podrian quedar en
     * la biblioteca luego de los precesos de reserva.
     * De esta manera se indica si un libro esta o no disponible.
     */
    public final static int cantidadMininaLibroPrestar = 0;

    /**
     * Metodo encargado de retornar una fecha de un datepicker
     */
     public static Date getDateFromDatePicker(DatePicker datePicker){

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

        return calendar.getTime();
    }

    /**
     * Metodo encargado de sumar o restar dias a una determinada fecha
     * @param fecha Fecha que sera modificada
     * @param dias Dias que se sumaran a la fecha indicada, en caso de ser negativo estos se restaran
     * @return
     */
    public static Date sumarRestarDiasAFecha(Date fecha, int dias){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);

        return calendar.getTime();
    }

    public static long diasDiferenciaEntreFechas(Date fecha1, Date fecha2){
        long diferencia = 0;

        diferencia = fecha2.getTime() - fecha1.getTime();
        return  diferencia/(3600000*24);
    }

    /**
     * Metodo que recorre un spinner para buscar una cadena contenida en el.
     * Utilidad especificada para seleccionar por defecto un valor determinado  del spinner
     * @param spinner
     * @param myString
     * @return Devuelve el indice correspondiende a la cadena
     */
    public static int getIndexSpinner(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            Log.i("ValorDefectoSpinner",">>>>>>>>>>>>>>>>>>> spiner: "+spinner+" >>>>> valor: "+spinner.getItemAtPosition(i));

            if(spinner.getItemAtPosition(i) != null) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                    index = i;
                }
            }

        }
        return index;
    }

    /**
     * Metodo encargado de validar si el sistema está conectado a una determinada red. internet.
     * @param ctx
     * @return
     */
    public static boolean verificarConexionInternet(Context ctx){

        boolean conectado = false;

        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for(int i = 0; i < redes.length; i++){
            //Se verifica si hay conexion:
            if(redes[i].getState() == NetworkInfo.State.CONNECTED){
                conectado = true;
            }
        }

        return conectado;
    }

}
