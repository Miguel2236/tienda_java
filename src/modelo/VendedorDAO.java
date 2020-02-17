
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class VendedorDAO implements CRUD{
    Conexion con = new Conexion();
    Connection acceso;
    PreparedStatement ps;
    ResultSet rs;
    
    public EntidadVendedor ValidarVendedor(String dni, String user)
    {
        /**
         * validar usuario para iniciar sesión
         */
        EntidadVendedor ev = new EntidadVendedor(); //instanciamos el objeto
        String sql = "SELECT * FROM vendedor WHERE Dni=? AND User_2=?";
        try {
            acceso = con.Conectar();
            ps = acceso.prepareStatement(sql);
            ps.setString(1, dni);
            ps.setString(2, user);
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                ev.setId(rs.getInt(1));
                ev.setDni(rs.getString(2));
                ev.setNom(rs.getString(3));
                ev.setTel(rs.getString(4));
                ev.setEstado(rs.getString(5));
                ev.setUser(rs.getString(6));
            }
        } catch (SQLException e) {
            System.out.println("Ocurrió un Error "+e);
        }
        return ev;
    }

    @Override
    public List listar() {
         //Listar los vendedores
         List<Vendedor> listar = new ArrayList<> ();
         String sql = "SELECT idVEndedor,Dni,Nombres,Telefono,User_2,Estado FROM vendedor";
         try {
             acceso = con.Conectar();
             ps = acceso.prepareStatement(sql);
             rs = ps.executeQuery();
             while (rs.next()) 
             {
                 Vendedor vnd = new Vendedor();
                 vnd.setId(rs.getInt(1));
                 vnd.setDni(rs.getString(2));
                 vnd.setNombre(rs.getString(3));
                 vnd.setTel(rs.getString(4));
                 vnd.setUser(rs.getString(5));
                 vnd.setEstado(rs.getString(6));
                 listar.add(vnd);
             }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return listar;
    }

    @Override
    public int add(Object[] obj) {
        // agregar un vendedor nuevo, se reciben los datos del formulario para ser guardados
        int r = 0;
        String sql = "INSERT INTO vendedor(Dni,Nombres,Telefono,User_2,Estado) VALUES(?,?,?,?,?)";
        try {
            acceso = con.Conectar();
            ps = acceso.prepareStatement(sql);
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
    public int actualizar(Object[] obj) {
        //editar un vendedor, se recibe los datos del formulario.
        int r = 0;
        String sql = "UPDATE vendedor SET Dni = ?, Nombres = ?, Telefono = ?, User_2 = ?, Estado = ? WHERE idVEndedor = ?";
        try {
            acceso = con.Conectar();
            ps = acceso.prepareStatement(sql);
            ps.setObject(1, obj[0]);
            ps.setObject(2, obj[1]);
            ps.setObject(3, obj[2]);
            ps.setObject(4, obj[3]);
            ps.setObject(5, obj[4]);
            ps.setObject(6, obj[5]);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return r;
    }

    @Override
    public int eliminar(int id) {
        //eliminar un vendedor usando el ID enviado.
        int r = 0;
        String sql = "DELETE FROM vendedor WHERE idVEndedor = ?";
        try {
            acceso = con.Conectar();
            ps = acceso.prepareStatement(sql);
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return r;
    }
}
