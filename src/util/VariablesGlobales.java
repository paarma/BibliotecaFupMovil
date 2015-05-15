package util;

/**
 * Created by pablo on 15/05/15.
 * Clase utilizada para gestionar variables globales
 */
public class VariablesGlobales {

    private static VariablesGlobales instance;

    // Global variable
    /**
     * Variable que indica el item especofico seleccionado desde el menu
     */
    private int opcionMenu;

    // Restrict the constructor from being instantiated
    private VariablesGlobales(){}

    //Getters and Setters
    public void setOpcionMenu(int d){
        this.opcionMenu=d;
    }
    public int getOpcionMenu(){
        return this.opcionMenu;
    }

    public static synchronized VariablesGlobales getInstance(){
        if(instance==null){
            instance=new VariablesGlobales();
        }
        return instance;
    }
}
