package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 29/03/15.
 */
@SuppressWarnings("serial")
public class Usuario implements Serializable {

    private int idUsuario;
    private Long cedula;
    private String nombre;
    private String apellido;
    private Long telefono;
    private String direccion;
    private String email;
    private String codigo;
    private String clave;
    private String rol;

    public Usuario() {
        this.idUsuario = 0;
        this.cedula = null;
        this.nombre = "";
        this.apellido = "";
        this.telefono = null;
        this.direccion = "";
        this.email = "";
        this.codigo = "";
        this.clave = "";
        this.rol = null;
    }

    public Usuario(int idUsuario, Long cedula, String nombre, String apellido, Long telefono, String direccion, String email, String codigo, String clave, String rol) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
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

    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
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
