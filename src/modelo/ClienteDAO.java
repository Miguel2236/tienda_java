
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteDAO implements CRUD { // implementamos la interfaz creada CRUD.java (equivale al extends de php
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    
    public Cliente buscarCliente(String dni)
    {
        Cliente cls = new Cliente();
        String sql = "SELECT idCliente,Dni,Nombres,Direccion,Estado FROM cliente WHERE Dni = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();
            while (rs.next())
            {
                cls.setId(rs.getInt(1));
                cls.setDni(rs.getString(2));
                cls.setNom(rs.getString(3));
                cls.setDir(rs.getString(4));
                cls.setEstado(rs.getString(5));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return cls;
    }

    @Override
    public List listar() {
        /**
         * Lista a los clientes
         */
        
        List<Cliente> lista = new ArrayList<>();
        String slq = "SELECT idCliente, Dni, Nombres, Direccion, Estado FROM cliente";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(slq);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt(1));
                c.setDni(rs.getString(2));
                c.setNom(rs.getString(3));
                c.setDir(rs.getString(4));
                c.setEstado(rs.getString(5));
                lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return lista;
    }

    @Override
    public int add(Object[] obj) {
        int r = 0;
        String sql = "INSERT INTO cliente (Dni,Nombres,Direccion,Estado) VALUES(?,?,?,?)";
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
        }
        
        return r;
    }

    @Override
    public int actualizar(Object[] obj) {
        int r = 0;
        String sql = "UPDATE cliente SET Dni = ?, Nombres = ?, Direccion = ?, Estado = ? WHERE idCliente = ?";
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
        }
        
        return r;
    }

    @Override
    public int eliminar(int id) {
        int r = 0;
        String sql = "DELETE FROM cliente WHERE idCliente = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return r;
    }
}
