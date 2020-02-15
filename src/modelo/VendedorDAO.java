
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class VendedorDAO implements CRUD{
    PreparedStatement ps;
    ResultSet rs;
    
    Conexion con = new Conexion();
    Connection acceso;
    
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
                 vnd.setDni(rs.getString(1));
                 vnd.setNombre(rs.getString(2));
                 vnd.setTel(rs.getString(3));
                 vnd.setUser(rs.getString(4));
                 vnd.setEstado(rs.getString(5));
                 listar.add(vnd);
             }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listar;
    }

    @Override
    public int add(Object[] o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int actualizar(Object[] o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminar(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
