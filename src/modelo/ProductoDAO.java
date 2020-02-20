
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO implements CRUD{
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public Producto buscarProducto(int idProducto)
    {
        /**
         * buscar un producto usando su ID, retorna el resultado de la consulta
         */
        Producto prod = new Producto();
        String sql = "SELECT idProducto,Nombres,Precio,Stock,Estado FROM producto WHERE idProducto = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();
            while (rs.next())
            {
                prod.setId(rs.getInt(1));
                prod.setNom(rs.getString(2));
                prod.setPrecio(rs.getDouble(3));
                prod.setStock(rs.getInt(4));
                prod.setEstado(rs.getString(5));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return prod;
    }

    @Override
    public List listar() {
        /**
         * listado de productos
         */
        List<Producto> lista = new ArrayList<> ();
        String sql = "SELECT idProducto,Nombres,Precio,Stock,Estado FROM producto";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                Producto pr = new Producto();
                pr.setId(rs.getInt(1));
                pr.setNom(rs.getString(2));
                pr.setPrecio(rs.getDouble(3));
                pr.setStock(rs.getInt(4));
                pr.setEstado(rs.getString(5));
                lista.add(pr);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return lista;
    }

    @Override
    public int add(Object[] obj) {
        /**
         * Agregar un proucto
         */
        int r = 0;
        String sql = "INSERT INTO producto(Nombres,Precio,Stock,Estado) VALUES(?,?,?,?)";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setObject(1, obj[0]);
            ps.setObject(2, obj[1]);
            ps.setObject(3, obj[2]);
            ps.setObject(4, obj[3]);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception h){
            JOptionPane.showMessageDialog(null, h);
        }
        return r;
    }

    @Override
    public int actualizar(Object[] obj) {
        /**
         * actualizar un registro
         */
        int r = 0;
        String sql = "UPDATE producto SET Nombres = ?, Precio = ?, Stock = ?, Estado = ? WHERE idProducto = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setObject(1, obj[0]);
            ps.setObject(2, obj[1]);
            ps.setObject(3, obj[2]);
            ps.setObject(4, obj[3]);
            ps.setObject(5, obj[4]);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception h){
            JOptionPane.showMessageDialog(null, h);
        }
        return r;
    }

    @Override
    public int eliminar(int id) {
        /**
         * eliminar un registro
         */
        int r = 0;
        String sql = "DELETE FROM producto WHERE idProducto = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception h){
            JOptionPane.showMessageDialog(null, h);
        }
        return r;
    }
    
}
