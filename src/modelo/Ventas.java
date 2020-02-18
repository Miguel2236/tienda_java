
package modelo;

public class Ventas {
    int idVenta;
    int idCliente;
    int idVendedor;
    String serie;
    String fecha;
    double monto;
    String estado; 

    public Ventas() {
    }

    public Ventas(int idVenta, int idCliente, int idVendedor, String serie, String fecha, double monto, String estado) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.serie = serie;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
