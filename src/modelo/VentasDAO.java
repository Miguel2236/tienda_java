
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class VentasDAO {
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    int r = 0;
    public String getLastIDVenta() 
    {
        /**
         * obtener el ultimo ID generado en la ultima venta
         */
        String idVenta = "";
        String sql = "SELECT MAX(idVentas) FROM ventas";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                idVenta = rs.getString(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un Error al obtener el ID venta "+e);
        }
        return idVenta;
    }
    
    public String generarNoSerie()
    {
        /**
         * generar el número de serie
         */
        String serie = "";
        String sql = "SELECT MAX(NumeroVentas) FROM ventas";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                serie = rs.getString(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return serie;
    }
    
    // funciones para guardar datos en al tabla ventas y ventasDetalle
    
    public int guardarVenta(Ventas v)
    {
        Ventas ventas = new Ventas();
        String sql = "INSERT INTO ventas(Cliente_idCliente,Vendedor_idVendedor,NumeroVentas,FechaVenta,Monto,Estado) VALUES(?,?,?,?,?,?)";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, v.getIdCliente());
            ps.setInt(2, v.getIdVendedor());
            ps.setString(3, v.getSerie());
            ps.setString(4, v.getFecha());
            ps.setDouble(5, v.getMonto());
            ps.setString(6, v.getEstado());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un Error "+e);
        }
        return r;
    }
    
    public int guardarDetalleVentas(DetalleVenta d)
    {
        String sql = "INSERT INTO detalle_ventas(Ventas_idVentas,Producto_idProducto,Cantidad,PrecioVenta) VALUES(?,?,?,?)";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, d.getIdVentas());
            ps.setInt(2, d.getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPreVenta());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return r;
    }
    
    public List obtenerVentas()
    {
        List<ReporteVentas> reporte = new ArrayList<>();
        String sql = "SELECT vnt.idVentas,cls.idCliente,cls.Dni as claveCliente,cls.Nombres as cliente,cls.Direccion,vnt.NumeroVentas as claveVenta,vnt.FechaVenta,vnt.Monto FROM ventas as vnt INNER JOIN cliente as cls ON vnt.Cliente_idCliente = cls.idCliente";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                ReporteVentas rep = new ReporteVentas();
                rep.setIdVentas(rs.getInt(1));
                rep.setIdCliente(rs.getInt(2));
                rep.setDni(rs.getString(3));
                rep.setNombreCliente(rs.getString(4));
                rep.setDirCliente(rs.getString(5));
                rep.setClaveVenta(rs.getString(6));
                rep.setFecha(rs.getString(7));
                rep.setMonto(rs.getDouble(8));
                reporte.add(rep);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return reporte;
    }
}
