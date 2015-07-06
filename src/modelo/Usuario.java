package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 29/03/15.
 */
@SuppressWarnings("serial")
public class Usuario implements Serializable {

    private int idUsuario;
    private int cedula;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private int telefono;
    private String direccion;
    private String email;
    private String codigo;
    private String clave;
    private String rol;

    public Usuario() {
        this.idUsuario = 0;
        this.cedula = 0;
        this.primerNombre = "";
        this.segundoNombre = "";
        this.primerApellido = "";
        this.segundoApellido = "";
        this.telefono = 0;
        this.direccion = "";
        this.email = "";
        this.codigo = "";
        this.clave = "";
        this.rol = null;
    }

    public Usuario(int idUsuario, int cedula, String primerNombre, String segundoNombre,
                   String primerApellido, String segundoApellido, int telefono, String direccion,
                   String email, String codigo, String clave, String rol) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.codigo = codigo;
        this.clave = clave;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
