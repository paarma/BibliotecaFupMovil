package util;

import android.widget.DatePicker;

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

    /**
     * Variable entera para indicar el numero de dias por los que se puede prestar un libro.
     */
    public final static int diasTotalesPrestamo = 2;

    //Metodo encargado de retornar una fecha de un datepicker
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

    /**
     * Metodo encargado de retornar de manera String el estado de una reserva en cada proceso.
     * @param estado de la reserva :
     *  - EN_PROCESO: indica que se ha realizado una solicitud de reserva.
     *  - PRESTADO: indica que el libro ya se le ha prestado al usuario.
     *  - FINALIZADO: indica que el libro ya ha sido regresado a la biblioteca por el usuario.
     *  - CANCELADO: indica que se realizó una solicitud de reserva, pero el libro no fue llevado por el usuario en la fecha de reserva.
     * @return
     */
/*    public static String estadoSolicitudReservaUsuario(String estado){
        String respuestaEstado = "";

        if(estado.equals(estadoEnProceso)){
            respuestaEstado = "EN PROCESO";
        }
        return  respuestaEstado;
    }*/
}
