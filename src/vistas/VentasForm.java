
package vistas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.ClienteDAO;
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.Ventas;
import modelo.DetalleVenta;
import modelo.VentasDAO;

public class VentasForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form VentasForm
     */
    ClienteDAO clsdao = new ClienteDAO();
    VentasDAO ventasdao = new VentasDAO();
    Ventas ventas = new Ventas();
    Producto p = new Producto();
    Cliente cls = new Cliente();
    DetalleVenta detalleventa = new DetalleVenta();
    ProductoDAO prodao = new ProductoDAO();
    
    DefaultTableModel modelo = new DefaultTableModel();
    
    int idProducto;
    double precioProducto;
    int cantidadProducto;
    int item = 0;
    double totalGlobal = 0;
    
    int idCliente = 0;
    int idVendedor = 0;
    
    public VentasForm() {
        // Constructor para inicializar variables y funciones
        initComponents();
        spnCantidad.setValue(1);
        txtFecha.setText(getDate());
        generarSerie();
    }
    
    void buscarCliente()
    {
        /**
         * funcion para buscar un cliente usando su DNI
         */
        
        int r;
        String codCliente = txtCodCliente.getText();
        if (txtCodCliente.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Escriba el código del cliente a buscar");
        }
        else
        {
            cls = clsdao.buscarCliente(codCliente);
            if (cls.getDni() != null)
            {
                txtCliente.setText(cls.getNom());
                txtCodProd.requestFocus();
            }
            else
            {
                r = JOptionPane.showConfirmDialog(this, "Cliente NO encontrado, ¿Registrar uno nuevo?");
                if (r == 0) 
                {
                    ClienteForm clsFrm = new ClienteForm();
                    Principal.VentanaPrincipal.add(clsFrm);  // esto se ahce cuando las propiedades de la ventana principal es public y static
                    clsFrm.setVisible(true);
                }
            }
        }
    }
    
    void buscarProductoID()
    {
        int r;
        String codProd = txtCodProd.getText();
        
        if (codProd.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Escriba el código del producto a buscar");
            txtCodProd.requestFocus();
        }
        else
        {
            Producto pro =  prodao.buscarProducto(Integer.parseInt(codProd));
            if (pro.getNom() != null)
            {
                txtProducto.setText(pro.getNom());
                txtPrecio.setText(""+pro.getPrecio()); // al ser imcompatibles los tipos se deja un espcio entre comillas
                txtStock.setText(""+pro.getStock());
            }
            else
            {
                r = JOptionPane.showConfirmDialog(this, "Producto no encotnrado,¿Desea registrarlo?");
                if (r == 0)
                {
                    ProductosForm prdfrm = new ProductosForm();
                    Principal.VentanaPrincipal.add(prdfrm);
                    prdfrm.setVisible(true);
                }
            }
        }
    }
    
    void argegarProducto()
    {
        /**
         * agregar un produto a la tabla de venta para su calculo
         */
        double total;
        modelo = (DefaultTableModel)tblVenta.getModel();
        item = item + 1;
        idProducto = Integer.parseInt(txtCodProd.getText());
        String nombreProducto = txtProducto.getText();
        precioProducto = Double.parseDouble(txtPrecio.getText());
        int stockProducto = Integer.parseInt(txtStock.getText());
        cantidadProducto = Integer.parseInt(spnCantidad.getValue().toString());
        
        if (cantidadProducto > 0)
        {
            total = cantidadProducto*precioProducto;
            ArrayList lista = new ArrayList();
            if (stockProducto > 0)
            {
                lista.add(item);
                lista.add(idProducto);
                lista.add(nombreProducto);
                lista.add(cantidadProducto);
                lista.add(precioProducto);
                lista.add(total);

                Object[] obj = new Object[6];
                obj[0] = lista.get(0);
                obj[1] = lista.get(1);
                obj[2] = lista.get(2);
                obj[3] = lista.get(3);
                obj[4] = lista.get(4);
                obj[5] = lista.get(5);

                modelo.addRow(obj);
                tblVenta.setModel(modelo);

                calcularTotalGlobal();
                limpiarFormularioVenta();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Es stock de "+nombreProducto+" es 0");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "La cantidad a vender no puede ser 0");
        }
    }
    
    void calcularTotalGlobal()
    {
        /**
         * se calcula el total a pagar de la venta
         */
        
        for (int i = 0; i < tblVenta.getRowCount(); i++)
        {
            cantidadProducto = Integer.parseInt(tblVenta.getValueAt(i, 3).toString());
            precioProducto = Double.parseDouble(tblVenta.getValueAt(i, 4).toString());
            totalGlobal = totalGlobal+(cantidadProducto*precioProducto);
        }
        txtTotal.setText(""+totalGlobal);
    }
    
    String getDate()
    {
        /**
         * obtener fecha actual en formato dd/mm/YYYY
         */
        String fecha;
        DateFormat dateformat = new SimpleDateFormat("YYYY-MM-dd");
        Date date = new Date();
        fecha = dateformat.format(date);
        return fecha;
    }
    
    void generarSerie()
    {
        /**
         * obtener el numero de serie anterior y pegarlo al imput de serie
         */
        
        String serie = ventasdao.generarNoSerie();
        if (serie == null)
        {
            serie = "00001";
            txtSerie.setText(serie);
        }
        else
        {
            int increment = Integer.parseInt(serie);
            increment = increment + 1;
            txtSerie.setText("0000"+increment);
        }
    }
    
    void guardarVenta()
    {
        /**
         * guardar la venta
         */
        String serie = txtSerie.getText();
        idCliente = cls.getId();
        idVendedor = 1;
        String fecha = getDate();
        double monto = totalGlobal;
        
        ventas.setIdCliente(idCliente);
        ventas.setIdVendedor(idVendedor);
        ventas.setSerie(serie);
        ventas.setFecha(fecha);
        ventas.setMonto(monto);
        ventas.setEstado("1");
        
        ventasdao.guardarVenta(ventas);
   }
    
    void guardarDetalle()
    {
        /**
         * guardar el detalle de las ventas
         */
        DetalleVenta dventa = new DetalleVenta();
        int idVenta = Integer.parseInt(ventasdao.getLastIDVenta());
        for (int i = 0; i < tblVenta.getRowCount(); i++)
        {
            int idProd = Integer.parseInt(tblVenta.getValueAt(i, 1).toString());
            int cantidad = Integer.parseInt(tblVenta.getValueAt(i, 3).toString());
            double precioUnitario = Double.parseDouble(tblVenta.getValueAt(i, 4).toString());
            
            dventa.setIdVentas(idVenta);
            dventa.setIdProducto(idProd);
            dventa.setCantidad(cantidad);
            dventa.setPreVenta(precioUnitario);
            
            int stock = prodao.obtenerStock(idProd);
            int resta = stock - cantidad;
            prodao.updateStock(resta, idProd);
            
            ventasdao.guardarDetalleVentas(dventa);
        }
        
        limpiarFormularioVenta();
        limpiartablaVenta();
        generarSerie();
        JOptionPane.showMessageDialog(this, "Venta guardada Con éxito");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCodCliente = new javax.swing.JTextField();
        txtCodProd = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        spnCantidad = new javax.swing.JSpinner();
        btnBuscarClt = new javax.swing.JButton();
        btnBuscarPro = new javax.swing.JButton();
        btnAgregarPro = new javax.swing.JButton();
        txtFecha = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtVendedor = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVenta = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnVenta = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setTitle("Ventas");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel1.setText("Sistema de Ventas");

        jLabel2.setText("Ventas del Friki Feliz");

        jLabel3.setText("Telefono: 32545745");

        jLabel4.setText("No. Serie:");

        txtSerie.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addComponent(jLabel4)
                        .addGap(37, 37, 37)
                        .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(260, 260, 260)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setText("COD. Cliente");

        jLabel6.setText("COD. Producto");

        jLabel7.setText("Precio");

        txtPrecio.setEditable(false);

        jLabel8.setText("Cantidad");

        btnBuscarClt.setText("Buscar");
        btnBuscarClt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCltActionPerformed(evt);
            }
        });

        btnBuscarPro.setText("Buscar");
        btnBuscarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProActionPerformed(evt);
            }
        });

        btnAgregarPro.setText("Agregar");
        btnAgregarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProActionPerformed(evt);
            }
        });

        txtFecha.setEditable(false);

        jLabel9.setText("Cliente");

        jLabel10.setText("Producto");

        jLabel11.setText("Stock");

        jLabel12.setText("Vendedor");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCodCliente)
                    .addComponent(txtCodProd)
                    .addComponent(txtPrecio)
                    .addComponent(spnCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBuscarClt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBuscarPro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregarPro, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(txtFecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVendedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarClt)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarPro)
                    .addComponent(jLabel10)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarPro)
                    .addComponent(jLabel11)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Número", "Código", "Producto", "Cantidad", "Precio Uni.", "Total"
            }
        ));
        jScrollPane1.setViewportView(tblVenta);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setText("Total a Pagar");

        txtTotal.setEditable(false);

        btnVenta.setText("Generar Venta");
        btnVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(192, Short.MAX_VALUE)
                .addComponent(btnVenta)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVenta)
                    .addComponent(btnCancelar)
                    .addComponent(jLabel13)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarCltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCltActionPerformed
        // buscar un cliente usando su DNI
        buscarCliente();
    }//GEN-LAST:event_btnBuscarCltActionPerformed

    private void btnBuscarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProActionPerformed
        // buscar un producto usando su ID
        buscarProductoID();
    }//GEN-LAST:event_btnBuscarProActionPerformed

    private void btnAgregarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProActionPerformed
        // Agregar ub producto para hacer la venta
        argegarProducto();
    }//GEN-LAST:event_btnAgregarProActionPerformed

    private void btnVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaActionPerformed
        // generar venta
        guardarVenta();
        guardarDetalle();
    }//GEN-LAST:event_btnVentaActionPerformed

    void limpiarFormularioVenta()
    {
        // limpiar los imputs de venta
        txtCliente.setText("");
        txtCliente.setText("");
        txtCodProd.setText("");
        txtProducto.setText("");
        txtStock.setText("");
        txtPrecio.setText("");
        spnCantidad.setValue(1);
    }
    
    void limpiartablaVenta()
    {
        //limpiar la tabla de ventas
        for (int i = 0; i < tblVenta.getRowCount(); i++)
        {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarPro;
    private javax.swing.JButton btnBuscarClt;
    private javax.swing.JButton btnBuscarPro;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTable tblVenta;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodCliente;
    private javax.swing.JTextField txtCodProd;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtSerie;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables
}
