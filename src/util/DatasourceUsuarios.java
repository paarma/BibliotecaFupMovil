package util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;

/**
 * Created by pablo on 23/09/15.
 * Clase encargada de contener la estructura de paginacion para el listado de usuarios
 */
public class DatasourceUsuarios
{
    //Singleton pattern
    private static DatasourceUsuarios datasourceUsuarios = null;
    private List<Usuario> data = null;
    private int SIZE = 0;

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    public static DatasourceUsuarios getInstance()
    {
        if (datasourceUsuarios == null)
        {
            datasourceUsuarios = new DatasourceUsuarios();
        }
        return datasourceUsuarios;
    }

    private DatasourceUsuarios(){
        data = new ArrayList<Usuario>();
    }

    public List<Usuario> getData() {
        return data;
    }

    public void setData(List<Usuario> data) {
        this.data = data;
    }

    public int getSize()
    {
        return SIZE;
    }

    public void setSIZE(int SIZE) {
        this.SIZE = SIZE;
    }

    /**
     * Returns the elements in a <b>NEW</b> list.
     */
    public List<Usuario> getData(int offset, int limit)
    {
        List<Usuario> newList = new ArrayList<Usuario>(limit);

        try {
            newList = tareasGenerales.buscarUsuariosPaginados(variablesGlobales.getUsuarioBuscar(), offset, limit);
            Log.i("DSUsuarios", ">>>>>>>>>>> Tamaño lista usuarios DataSoruce: " + newList.size());
        }catch (Exception e){
            Log.e("DSUsuarios ", "xxx Error getData(): " + e.getMessage());
        }

        return newList;
    }

}
