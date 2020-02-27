
package modelo;

public class ReporteVentas {
    int idVentas;
    int idCliente;
    String dni;
    String nombreCliente;
    String dirCliente;
    String claveVenta;
    String fecha;
    double monto;

    public ReporteVentas() {
    }

    public ReporteVentas(int idVentas, int idCliente, String dni, String nombreCliente, String dirCliente, String claveVenta, String fecha, double monto) {
        this.idVentas = idVentas;
        this.idCliente = idCliente;
        this.dni = dni;
        this.nombreCliente = nombreCliente;
        this.dirCliente = dirCliente;
        this.claveVenta = claveVenta;
        this.fecha = fecha;
        this.monto = monto;
    }

    public int getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(int idVentas) {
        this.idVentas = idVentas;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDirCliente() {
        return dirCliente;
    }

    public void setDirCliente(String dirCliente) {
        this.dirCliente = dirCliente;
    }

    public String getClaveVenta() {
        return claveVenta;
    }

    public void setClaveVenta(String claveVenta) {
        this.claveVenta = claveVenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
    
}
