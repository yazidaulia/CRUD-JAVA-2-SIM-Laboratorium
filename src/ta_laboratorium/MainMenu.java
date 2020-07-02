package ta_laboratorium;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author yazidaulia
 */
public class MainMenu extends javax.swing.JFrame {
    private Connection conn = new koneksi().connect();
    private DefaultTableModel model, model2, model3, model4;
    public String id_pas, nama, tanggal_lahir, alamat, pemeriksaan;
    
    public MainMenu() {
        initComponents();
        this.setLocationRelativeTo(null);
        table();
        autonumber();
    }

    private void table(){
        Object[] Baris = {"ID Pasien", "Nama", "Alamat", "Pemeriksaan", "Tanggal Lahir"};
        model = new DefaultTableModel(null,Baris);
        Object[] BarPemeriksaan = {"ID Pasien", "Sub Pemeriksaan", "Tanggal Periksa","Hasil Pemeriksaan"};
        model2 = new DefaultTableModel(null, BarPemeriksaan);
        tblPemeriksaan.setModel(model2);
        Object[] BarRiwayat = {"ID Nota", "Tanggal Periksa", "Nama Pasien", "Nama Pemeriksaan"};
        model3 = new DefaultTableModel(null, BarRiwayat);
        tblRiwayat.setModel(model3);
        Object[] BarAnalis = {"ID Analis", "Nama Analis","Alamat","Pendidikan Terakhir", "No. Telp"};
        model4 = new DefaultTableModel(null, BarAnalis);
        tblAnalis.setModel(model4);
        getData();
    }
    
    private void resetAll(){
        id_pasien.setText("");
        txtNamaPasien.setText("");
        txtAlamat.setText("");
        cbPemeriksaan.setSelectedItem("-- Pilih Pemeriksaan --");
        ftgl.setText("");
        
        Fidpasien.setText("");
        fSubTest.setText("");
        fHasil.setText("");
        
        nm_pasien.setText("");
        FTanggal.setText("");
        FAlamat.setText("");
        
        txtIDAnalis.setText("");
        txtNamaAnalis.setText("");
        txtAlamatAnalis.setText("");
        cbPendidikan.setSelectedItem("-- Pilih Salah Satu --");
        txtNoHP.setText("");
        txtpas.setText("");
    }
    
    private void getData(){
        //Untuk Liat Data Pasien
        try{
            model.getDataVector().removeAllElements();
            String caripasien=tCariPasien.getText();
            Statement st = conn.createStatement();
            String sql = "select * from pasien where nama_pasien like '%"+caripasien+"%' or alamat like '%"+caripasien+"%' or tanggal_lahir like '%"+caripasien+"%'";            
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Object[] obj = new Object[6];
                obj[0] = rs.getString("id_pasien");
                obj[1] = rs.getString("nama_pasien");
                obj[2] = rs.getString("alamat");
                obj[3] = rs.getString("nama_test");
                obj[4] = rs.getString("tanggal_lahir");
                
                model.addRow(obj);
            }
            tblPasien.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "error "+e);
        }
        
        //Untuk Tabel Pemeriksaan
        try{
            model2.getDataVector().removeAllElements();
            String caridata = txtcari.getText();
            String s = "select * from buat where id_pas like '%"+caridata+"%' or subtest like '%"+caridata+"%' or jtanggal_periksa like '%"+caridata+"%'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(s);
            while(rs.next()){
                Object[] obj = new Object[4];
                obj[0] = rs.getString("id_pas");
                obj[1] = rs.getString("subtest");
                obj[2] = rs.getString("jtanggal_periksa");
                obj[3] = rs.getString("hasil_pemeriksaan");
                
                model2.addRow(obj);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        //Untuk TABEL ANALIS
        try{
            model4.getDataVector().removeAllElements();
            String caridata = txtCariAnalis.getText();
            String s = "select * from analis where nama_analis like '%"+caridata+"%' or alamat_analis like '%"+caridata+"%'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(s);
            while(rs.next()){
                Object[] obj = new Object[5];
                obj[0] = rs.getString("id_analis");
                obj[1] = rs.getString("nama_analis");
                obj[2] = rs.getString("alamat_analis");
                obj[3] = rs.getString("pend_terakhir");
                obj[4] = rs.getString("no_hp");
                
                model4.addRow(obj);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
       
        //Untuk Tabel Nota
        try{
            model3.getDataVector().removeAllElements();
            String sql = "select nota.id_nota, nota.jtanggal_periksa, pasien.nama_pasien, pasien.nama_test from nota, pasien where nota.id_pas = pasien.id_pasien";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Object[] obj = new Object[4];
                obj[0] = rs.getString("id_nota");
                obj[1] = rs.getString("jtanggal_periksa");
                obj[2] = rs.getString("nama_pasien");
                obj[3] = rs.getString("nama_test");
                
                model3.addRow(obj);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Ada error nih "+e);
        }
    }
    
    /*public void getDataRiwayat(){
        //Untuk Tabel Riwayat
        try{
            model3.getDataVector().removeAllElements();
            String nmp = nm_pasien.getText();
            String tgl = FTanggal.getText();
            String almt = FAlamat.getText();
            String sql = "select pasien.nama_pasien, pasien.alamat, pasien.tanggal_lahir, buat.subtest, buat.jtanggal_periksa, buat.hasil_pemeriksaan from pasien, buat where pasien.id_pasien = buat.id_pas and pasien.nama_pasien like '%"+nmp+"%' and (pasien.alamat like '%"+almt+"%' or pasien.tanggal_lahir like '%"+tgl+"%')";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Object[] obj = new Object[6];
                obj[0] = rs.getString("nama_pasien");
                obj[1] = rs.getString("alamat");
                obj[2] = rs.getString("tanggal_lahir");
                obj[3] = rs.getString("subtest");
                obj[4] = rs.getString("jtanggal_periksa");
                obj[5] = rs.getString("hasil_pemeriksaan");
                
                model3.addRow(obj);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Ada error nih "+e);
        }
    }*/
    
    protected void autonumber(){
        //Autonumber id_pasien
        try{
            String sql = "SELECT id_pasien FROM pasien order by id_pasien desc LIMIT 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            id_pasien.setText("PS0001");
            while(rs.next()){
                String id_pas = rs.getString("id_pasien").substring(2);
                int AN = Integer.parseInt(id_pas) + 1;
                String Nol = "";
                
                if(AN<10){
                    Nol = "000";
                }else if(AN<100){
                    Nol = "00";
                }else if(AN<1000){
                    Nol = "0";
                }else if(AN<10000){
                    Nol = "";
                }
                id_pasien.setText("PS" + Nol + AN);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Auto number gagal "+e);
        }
        
        //Autonumber ID analis
        try{
            String sql = "SELECT id_analis FROM analis order by id_analis desc LIMIT 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            txtIDAnalis.setText("AN001");
            while(rs.next()){
                String idanalis = rs.getString("id_analis").substring(2);
                int AN = Integer.parseInt(idanalis) + 1;
                String Nol = "";
                
                if(AN<10){
                    Nol = "00";
                }else if(AN<100){
                    Nol = "0";
                }else if(AN<1000){
                    Nol = "";
                }
                txtIDAnalis.setText("AN" + Nol + AN);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Auto number gagal "+e);
        }
    }
    
    protected void cetakNota(){
        String reportSource = null, reportDesc = null;
        try{
            reportSource = System.getProperty("user.dir") + "/src/laporan/NotaPasien.jrxml";
            reportDesc = System.getProperty("user.dir") + "/src/laporan/NotaPasien.jasper";
            JasperReport jasRep = JasperCompileManager.compileReport(reportSource);
            JasperPrint jp = JasperFillManager.fillReport(jasRep, null, conn);
            JasperExportManager.exportReportToHtmlFile(jp, reportDesc);
            JasperViewer.viewReport(jp, false);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Masih belum terhubung "+e);
        }
    }
    
    public void itemTerpilihPasien(){
        popup Pp = new popup();
        Pp.pasien = this;
        nm_pasien.setText(nama);
        FTanggal.setText(tanggal_lahir);
        FAlamat.setText(alamat);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        Menu_Utama = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        Menu_Pasien = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNamaPasien = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        bSimpan = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        cbPemeriksaan = new javax.swing.JComboBox();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        id_pasien = new javax.swing.JTextField();
        ftgl = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        tCariPasien = new javax.swing.JTextField();
        bCariPasien = new javax.swing.JButton();
        bCetakNota = new javax.swing.JButton();
        Menu_Pemeriksaan = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPemeriksaan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtcari = new javax.swing.JTextField();
        bCari = new javax.swing.JButton();
        bTambah = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        Fidpasien = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        fSubTest = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fHasil = new javax.swing.JTextField();
        TUbah = new javax.swing.JButton();
        THapus = new javax.swing.JButton();
        TBatal = new javax.swing.JButton();
        Menu_Analis = new javax.swing.JPanel();
        Daftar_Analis = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtIDAnalis = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtNamaAnalis = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtAlamatAnalis = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        cbPendidikan = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        txtNoHP = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        bSimpanAnalis = new javax.swing.JButton();
        bUbahAnalis = new javax.swing.JButton();
        bBatalAnalis = new javax.swing.JButton();
        bHapusAnalis = new javax.swing.JButton();
        txtpas = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblAnalis = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        txtCariAnalis = new javax.swing.JTextField();
        Nota = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblRiwayat = new javax.swing.JTable();
        HapusData = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        nm_pasien = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        FTanggal = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        FAlamat = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        bSimpanRiwayat = new javax.swing.JButton();
        bBatalRiwayat = new javax.swing.JButton();
        bPilih = new javax.swing.JButton();
        BKeluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistem Informasi Manajemen Lab");
        setName("Sistem Informasi Manajemen Lab"); // NOI18N

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(800, 800));

        Menu_Utama.setBackground(new java.awt.Color(153, 255, 153));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel1.setText("RS. Tiara Bunda");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText(" Sistem Informasi Manajemen Laboratorium");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/th2.png"))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel24.setText("Jl. Kelapa Dua No. 100, Padurenan, Kec. Mustika Jaya, Kota Bekasi");

        javax.swing.GroupLayout Menu_UtamaLayout = new javax.swing.GroupLayout(Menu_Utama);
        Menu_Utama.setLayout(Menu_UtamaLayout);
        Menu_UtamaLayout.setHorizontalGroup(
            Menu_UtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UtamaLayout.createSequentialGroup()
                .addContainerGap(210, Short.MAX_VALUE)
                .addGroup(Menu_UtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addGroup(Menu_UtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UtamaLayout.createSequentialGroup()
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(376, 376, 376))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UtamaLayout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(192, 192, 192))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UtamaLayout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(338, 338, 338)))))
        );
        Menu_UtamaLayout.setVerticalGroup(
            Menu_UtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_UtamaLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addComponent(jLabel24)
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Menu Utama", new javax.swing.ImageIcon(getClass().getResource("/img/home.png")), Menu_Utama); // NOI18N

        Menu_Pasien.setBackground(new java.awt.Color(153, 255, 153));
        Menu_Pasien.setPreferredSize(new java.awt.Dimension(500, 900));

        jPanel3.setBackground(new java.awt.Color(153, 255, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Daftar Pasien", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 371));

        jLabel3.setText("Nama Pasien");

        jLabel4.setText("Alamat");

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane1.setViewportView(txtAlamat);

        jLabel5.setText("Tanggal Lahir");

        jLabel6.setText("Pemeriksaan");

        bSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gtk-save-as.png"))); // NOI18N
        bSimpan.setText("SIMPAN");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

        bBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Cancel.png"))); // NOI18N
        bBatal.setText("BATAL");
        bBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBatalActionPerformed(evt);
            }
        });

        cbPemeriksaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Pilih Pemeriksaan --", "H2TL", "Widal" }));

        bUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gtk-edit.png"))); // NOI18N
        bUbah.setText("UBAH");
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });

        bHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/document_delete.png"))); // NOI18N
        bHapus.setText("HAPUS");
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });

        jLabel8.setText("ID Pasien");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(id_pasien, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNamaPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                                            .addComponent(ftgl)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbPemeriksaan, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(bSimpan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bUbah)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bHapus))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(bBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(id_pasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNamaPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ftgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbPemeriksaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bUbah)
                    .addComponent(bHapus))
                .addGap(27, 27, 27)
                .addComponent(bBatal)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(153, 255, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tabel Pasien", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tblPasien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPasienMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPasien);

        jLabel7.setText("Cari Data Pasien");

        tCariPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tCariPasienKeyPressed(evt);
            }
        });

        bCariPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cari.png"))); // NOI18N
        bCariPasien.setText("CARI");
        bCariPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCariPasienActionPerformed(evt);
            }
        });

        bCetakNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cetak.png"))); // NOI18N
        bCetakNota.setText("CETAK");
        bCetakNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCetakNotaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tCariPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCariPasien)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bCetakNota)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tCariPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCariPasien))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(bCetakNota)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout Menu_PasienLayout = new javax.swing.GroupLayout(Menu_Pasien);
        Menu_Pasien.setLayout(Menu_PasienLayout);
        Menu_PasienLayout.setHorizontalGroup(
            Menu_PasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_PasienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        Menu_PasienLayout.setVerticalGroup(
            Menu_PasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_PasienLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(Menu_PasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pasien", new javax.swing.ImageIcon(getClass().getResource("/img/icon-pasien.png")), Menu_Pasien); // NOI18N

        Menu_Pemeriksaan.setBackground(new java.awt.Color(153, 255, 153));

        jPanel6.setBackground(new java.awt.Color(153, 255, 153));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tabel Pemeriksaan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tblPemeriksaan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPemeriksaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPemeriksaanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblPemeriksaan);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Cari Data Pemeriksaan");

        txtcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcariKeyPressed(evt);
            }
        });

        bCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cari.png"))); // NOI18N
        bCari.setText("CARI");
        bCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCariActionPerformed(evt);
            }
        });

        bTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download.jpg"))); // NOI18N
        bTambah.setText("Tambah Sub Pemeriksaan");
        bTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bCari)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTambah, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCari)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bTambah)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Edit Data Pemeriksaan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel10.setText("ID Pasien");

        jLabel11.setText("Sub Test");

        jLabel12.setText("Hasil");

        TUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gtk-edit.png"))); // NOI18N
        TUbah.setText("Ubah");
        TUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TUbahActionPerformed(evt);
            }
        });

        THapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/document_delete.png"))); // NOI18N
        THapus.setText("Hapus");
        THapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                THapusActionPerformed(evt);
            }
        });

        TBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Cancel.png"))); // NOI18N
        TBatal.setText("Batal");
        TBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(TBatal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(THapus))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Fidpasien, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(fSubTest)
                            .addComponent(fHasil)))
                    .addComponent(TUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(Fidpasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(fSubTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(fHasil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(TUbah)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(THapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TBatal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Menu_PemeriksaanLayout = new javax.swing.GroupLayout(Menu_Pemeriksaan);
        Menu_Pemeriksaan.setLayout(Menu_PemeriksaanLayout);
        Menu_PemeriksaanLayout.setHorizontalGroup(
            Menu_PemeriksaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_PemeriksaanLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Menu_PemeriksaanLayout.setVerticalGroup(
            Menu_PemeriksaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_PemeriksaanLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(Menu_PemeriksaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pemeriksaan", new javax.swing.ImageIcon(getClass().getResource("/img/mikroskop.png")), Menu_Pemeriksaan); // NOI18N

        Menu_Analis.setBackground(new java.awt.Color(153, 255, 153));

        Daftar_Analis.setBackground(new java.awt.Color(153, 255, 153));
        Daftar_Analis.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Daftar Analis", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        Daftar_Analis.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel16.setText("ID Analis");

        jLabel17.setText("Nama Analis");

        jLabel18.setText("Alamat");

        txtAlamatAnalis.setColumns(20);
        txtAlamatAnalis.setRows(5);
        jScrollPane6.setViewportView(txtAlamatAnalis);

        jLabel19.setText("Pendidikan Terakhir");

        cbPendidikan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Pilih Salah Satu --", "SMA Sederajat", "D3", "D4", "S1", "S2" }));

        jLabel20.setText("No. Telp / HP");

        jLabel21.setText("Password");

        bSimpanAnalis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-as.png"))); // NOI18N
        bSimpanAnalis.setText("SIMPAN");
        bSimpanAnalis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanAnalisActionPerformed(evt);
            }
        });

        bUbahAnalis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gtk-edit.png"))); // NOI18N
        bUbahAnalis.setText("UBAH");
        bUbahAnalis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahAnalisActionPerformed(evt);
            }
        });

        bBatalAnalis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Cancel.png"))); // NOI18N
        bBatalAnalis.setText("BATAL");
        bBatalAnalis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBatalAnalisActionPerformed(evt);
            }
        });

        bHapusAnalis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/document_delete.png"))); // NOI18N
        bHapusAnalis.setText("HAPUS");
        bHapusAnalis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusAnalisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Daftar_AnalisLayout = new javax.swing.GroupLayout(Daftar_Analis);
        Daftar_Analis.setLayout(Daftar_AnalisLayout);
        Daftar_AnalisLayout.setHorizontalGroup(
            Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Daftar_AnalisLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_AnalisLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(Daftar_AnalisLayout.createSequentialGroup()
                            .addGap(19, 19, 19)
                            .addComponent(txtIDAnalis, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Daftar_AnalisLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(txtNamaAnalis))))
                .addGap(57, 57, 57)
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbPendidikan, 0, 137, Short.MAX_VALUE)
                    .addComponent(txtNoHP)
                    .addComponent(txtpas))
                .addGap(42, 42, 42)
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bHapusAnalis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSimpanAnalis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bUbahAnalis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bBatalAnalis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        Daftar_AnalisLayout.setVerticalGroup(
            Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Daftar_AnalisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtIDAnalis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(cbPendidikan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSimpanAnalis))
                .addGap(18, 18, 18)
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaAnalis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel20)
                    .addComponent(txtNoHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bUbahAnalis))
                .addGap(18, 18, 18)
                .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Daftar_AnalisLayout.createSequentialGroup()
                        .addGroup(Daftar_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(bBatalAnalis)
                            .addComponent(txtpas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(bHapusAnalis)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tabel Analis", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tblAnalis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblAnalis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAnalisMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblAnalis);

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cari.png"))); // NOI18N
        jLabel22.setText("Cari Analis");

        txtCariAnalis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariAnalisKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCariAnalis, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtCariAnalis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Menu_AnalisLayout = new javax.swing.GroupLayout(Menu_Analis);
        Menu_Analis.setLayout(Menu_AnalisLayout);
        Menu_AnalisLayout.setHorizontalGroup(
            Menu_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_AnalisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Menu_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Menu_AnalisLayout.createSequentialGroup()
                        .addComponent(Daftar_Analis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 120, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Menu_AnalisLayout.setVerticalGroup(
            Menu_AnalisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_AnalisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Daftar_Analis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Analis", new javax.swing.ImageIcon(getClass().getResource("/img/lab-staff-icon.png")), Menu_Analis); // NOI18N

        Nota.setBackground(new java.awt.Color(153, 255, 153));

        jPanel5.setBackground(new java.awt.Color(153, 255, 153));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tabel Nota Pasien"));

        tblRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblRiwayat);

        HapusData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/document_delete.png"))); // NOI18N
        HapusData.setText("HAPUS");
        HapusData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(HapusData)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(HapusData)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(153, 255, 153));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Cetak Nota Pasien"));

        jLabel13.setText("Nama Pasien");

        jLabel14.setText("Tanggal Lahir");

        FAlamat.setColumns(20);
        FAlamat.setRows(5);
        jScrollPane5.setViewportView(FAlamat);

        jLabel15.setText("Alamat");

        bSimpanRiwayat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gtk-save-as.png"))); // NOI18N
        bSimpanRiwayat.setText("SIMPAN");
        bSimpanRiwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanRiwayatActionPerformed(evt);
            }
        });

        bBatalRiwayat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Cancel.png"))); // NOI18N
        bBatalRiwayat.setText("BATAL");
        bBatalRiwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBatalRiwayatActionPerformed(evt);
            }
        });

        bPilih.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/note.png"))); // NOI18N
        bPilih.setText("Pilih Data");
        bPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPilihActionPerformed(evt);
            }
        });

        BKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        BKeluar.setText("KELUAR");
        BKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BKeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nm_pasien)
                            .addComponent(FTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bPilih)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(bSimpanRiwayat)
                        .addGap(18, 18, 18)
                        .addComponent(bBatalRiwayat)
                        .addGap(18, 18, 18)
                        .addComponent(BKeluar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(nm_pasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPilih))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(FTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(38, 38, 38))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bSimpanRiwayat)
                    .addComponent(bBatalRiwayat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout NotaLayout = new javax.swing.GroupLayout(Nota);
        Nota.setLayout(NotaLayout);
        NotaLayout.setHorizontalGroup(
            NotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NotaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(NotaLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        NotaLayout.setVerticalGroup(
            NotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NotaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nota", new javax.swing.ImageIcon(getClass().getResource("/img/note.png")), Nota); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 875, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bSimpanRiwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanRiwayatActionPerformed
        //Data dialirkan ke nota
            String a = "select id_nota from nota order by id_nota desc LIMIT 1";
            try{
                Statement stat = conn.createStatement();
                ResultSet rs = stat.executeQuery(a);
                while(rs.next()){
                    String id = rs.getString("id_nota");
                    String sbs = id.substring(3);
                    int AN = Integer.parseInt(sbs) + 1;
                    String Nol = "";
                
                    if(AN<10){
                        Nol = "00";
                    }else if(AN<100){
                        Nol = "0";
                    }else if(AN<1000){
                        Nol = "";
                    }
                    String id_nota = "RST" + Nol + AN;
                    String s = "insert into nota(id_nota,jtanggal_periksa,id_pas,id_analis,nm_test) values('"+id_nota+"',current_timestamp(),'"+id_pas+"','"+TA_Laboratorium.getUserLogin()+"','"+pemeriksaan+"')";
                    try{
                        Statement stt = conn.createStatement();
                        stt.execute(s);
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(this, e);
                    }
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, e);
            }
            
        getData();
        cetakNota();
    }//GEN-LAST:event_bSimpanRiwayatActionPerformed

    private void TBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TBatalActionPerformed
        // Tombol Batal di Form Pemeriksaan
        resetAll();
        autonumber();
    }//GEN-LAST:event_TBatalActionPerformed

    private void THapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_THapusActionPerformed
        // Tombol Hapus Form Pemeriksaan
        int row = tblPemeriksaan.getSelectedRow();
        try{
            PreparedStatement ps = conn.prepareStatement("delete from buat where id_pas='"+Fidpasien.getText()+"'");
            ps.executeUpdate();
            model2.removeRow(row);
            getData();
            JOptionPane.showMessageDialog(null, "Berhasil menghapus data");
            resetAll();
            autonumber();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_THapusActionPerformed

    private void TUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TUbahActionPerformed
        // Tombol Ubah di Form Pemeriksaan
        String sql = "update buat set hasil_pemeriksaan=? where subtest='"+fSubTest.getText()+"' and id_pas='"+Fidpasien.getText()+"'";
        try{
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, fHasil.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "data berhasil diubah");
            getData();
            resetAll();
            autonumber();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "data gagal diubah "+e);
        }
    }//GEN-LAST:event_TUbahActionPerformed

    private void bTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTambahActionPerformed
        // Tombol Tambah di form pemeriksaan, berguna untuk menambah jumlah test
        new tmb_pemeriksaan().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_bTambahActionPerformed

    private void bCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariActionPerformed
        //tombol cari di form Pemeriksaan
        getData();
    }//GEN-LAST:event_bCariActionPerformed

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        // Field cari di form pemeriksaan
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            getData();
        }
    }//GEN-LAST:event_txtcariKeyPressed

    private void tblPemeriksaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPemeriksaanMouseClicked
        //Tabel di form Pemeriksaan
        int baris = tblPemeriksaan.getSelectedRow();
        String a = model2.getValueAt(baris, 0).toString();
        String b = model2.getValueAt(baris, 1).toString();
        String c = model2.getValueAt(baris, 2).toString();
        String d = model2.getValueAt(baris, 3).toString();

        Fidpasien.setText(a);
        fSubTest.setText(b);
        fHasil.setText(d);
    }//GEN-LAST:event_tblPemeriksaanMouseClicked

    private void bCariPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariPasienActionPerformed
        //Button cari di form pasien
        getData();
    }//GEN-LAST:event_bCariPasienActionPerformed

    private void tCariPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCariPasienKeyPressed
        //Field Cari di Form Pasien
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            getData();
        }
    }//GEN-LAST:event_tCariPasienKeyPressed

    private void tblPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPasienMouseClicked
        //Tabel di Form Pasien
        int baris = tblPasien.getSelectedRow();
        String a = model.getValueAt(baris, 0).toString();
        String b = model.getValueAt(baris, 1).toString();
        String c = model.getValueAt(baris, 2).toString();
        String d = model.getValueAt(baris, 3).toString();
        String e = model.getValueAt(baris, 4).toString();

        id_pasien.setText(a);
        txtNamaPasien.setText(b);
        txtAlamat.setText(c);
        cbPemeriksaan.setSelectedItem(d);
        ftgl.setText(e);
    }//GEN-LAST:event_tblPasienMouseClicked

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // Tombol Hapus Form Pasien
        int row = tblPasien.getSelectedRow();
        String selected = model.getValueAt(row,0).toString();
        try{
            PreparedStatement ps = conn.prepareStatement("delete from pasien where id_pasien='"+selected+"'");
            ps.executeUpdate();
            model.removeRow(row);
            getData();
            JOptionPane.showMessageDialog(null, "Berhasil menghapus data");
            resetAll();
            autonumber();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        //button ubah di form pasien
        int baris = tblPasien.getSelectedRow();
        String a = model.getValueAt(baris, 0).toString();
        String sql = "update pasien set nama_pasien=?, alamat=?, tanggal_lahir=?, nama_test=? where id_pasien='"+a+"'";
        try{
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, txtNamaPasien.getText());
            stat.setString(2, txtAlamat.getText());
            stat.setString(3, ftgl.getText());
            stat.setString(4, cbPemeriksaan.getSelectedItem().toString());

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "data berhasil diubah");
            getData();
            resetAll();
            autonumber();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "data gagal diubah "+e);
        }
    }//GEN-LAST:event_bUbahActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        //button batal di form pasien
        resetAll();
        autonumber();
    }//GEN-LAST:event_bBatalActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // Tombol Simpan di form pasien
        try{
            String np, almt, d, tgl, idp;
            idp = id_pasien.getText();
            np = txtNamaPasien.getText();
            almt = txtAlamat.getText();
            d = cbPemeriksaan.getSelectedItem().toString();
            tgl = ftgl.getText();

            String sql = "insert into pasien values (?,?,?,?,?)";
            try{
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, idp);
                stat.setString(2, np);
                stat.setString(3, almt);
                stat.setString(4, d);
                stat.setString(5, tgl);

                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "data berhasil disimpan");
                getData();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "data gagal disimpan "+e);
            }
            
            //Data dialirkan ke Hasil Pemeriksaan
            String st = "select sub_test from pemeriksaan where nama_pemeriksaan = '"+cbPemeriksaan.getSelectedItem()+"'";
            Statement state = conn.createStatement();
            ResultSet res = state.executeQuery(st);
            try{
                while(res.next()){
                    Object[] obj = new Object[1];
                    obj[0] = res.getString("sub_test");;
                    String hp = "insert into buat(id_pas,subtest,jtanggal_periksa,hasil_pemeriksaan) values('"+id_pasien.getText()+"','"+obj[0]+"',current_timestamp(),'')";
                    Statement stmt = conn.createStatement();
                    stmt.execute(hp);
                }
                getData();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
            resetAll();
            autonumber();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void tblAnalisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAnalisMouseClicked
        // Tabel di Form Analis
        int baris = tblAnalis.getSelectedRow();
        String a = model4.getValueAt(baris, 0).toString();
        String b = model4.getValueAt(baris, 1).toString();
        String c = model4.getValueAt(baris, 2).toString();
        String d = model4.getValueAt(baris, 3).toString();
        String e = model4.getValueAt(baris, 4).toString();

        txtIDAnalis.setText(a);
        txtNamaAnalis.setText(b);
        txtAlamatAnalis.setText(c);
        cbPendidikan.setSelectedItem(d);
        txtNoHP.setText(e);
    }//GEN-LAST:event_tblAnalisMouseClicked

    private void txtCariAnalisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariAnalisKeyTyped
        //Field text cari analis di Form Analis
        getData();
    }//GEN-LAST:event_txtCariAnalisKeyTyped

    private void bBatalAnalisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalAnalisActionPerformed
        // Tombol Batal di Form Analis
        resetAll();
        autonumber();
    }//GEN-LAST:event_bBatalAnalisActionPerformed

    private void bBatalRiwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalRiwayatActionPerformed
        // Tombol Batal di Form Riwayat
        resetAll();
        autonumber();
    }//GEN-LAST:event_bBatalRiwayatActionPerformed

    private void bSimpanAnalisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanAnalisActionPerformed
        // Tombol Simpan di Form Analis
        String id_analis = txtIDAnalis.getText();
        String nm_analis = txtNamaAnalis.getText();
        String almtAnalis = txtAlamatAnalis.getText();
        String pend = cbPendidikan.getSelectedItem().toString();
        String telp = txtNoHP.getText();
        String pass = txtpas.getText();
        String sql = "insert into analis values (?,?,?,?,?,?)";
            try{
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, id_analis);
                stat.setString(2, nm_analis);
                stat.setString(3, almtAnalis);
                stat.setString(4, pass);
                stat.setString(5, pend);
                stat.setString(6, telp);

                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "data berhasil disimpan");
                getData();
                resetAll();
                autonumber();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "data gagal disimpan "+e);
            }
    }//GEN-LAST:event_bSimpanAnalisActionPerformed

    private void bUbahAnalisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahAnalisActionPerformed
        // Tombol Ubah di Form Analis
        String id_analis = txtIDAnalis.getText();
        String nm_analis = txtNamaAnalis.getText();
        String almtAnalis = txtAlamatAnalis.getText();
        String pend = cbPendidikan.getSelectedItem().toString();
        String telp = txtNoHP.getText();
        String pass = txtpas.getText();
        if(txtpas.getText().equals("")){
            String sql = "update analis set nama_analis=?, alamat_analis=?, pend_terakhir=?, no_hp=? where id_analis='"+id_analis+"'";
            try{
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, nm_analis);
                stat.setString(2, almtAnalis);
                stat.setString(3, pend);
                stat.setString(4, telp);
                
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "data berhasil diubah");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "data gagal diubah "+e);
            }
        }else{
            String sql = "update analis set nama_analis=?, alamat_analis=?, password=?, pend_terakhir=?, no_hp=? where id_analis='"+id_analis+"'";
            try{
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, nm_analis);
                stat.setString(2, almtAnalis);
                stat.setString(3, pass);
                stat.setString(4, pend);
                stat.setString(5, telp);
                
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "data berhasil diubah");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "data gagal diubah "+e);
            }
        }
        getData();
        resetAll();
        autonumber();
    }//GEN-LAST:event_bUbahAnalisActionPerformed

    private void bHapusAnalisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusAnalisActionPerformed
        // Tombol Hapus di form Analis
        int row = tblAnalis.getSelectedRow();
        String selected = model4.getValueAt(row,0).toString();
        try{
            PreparedStatement ps = conn.prepareStatement("delete from analis where id_analis='"+selected+"'");
            ps.executeUpdate();
            model4.removeRow(row);
            getData();
            JOptionPane.showMessageDialog(null, "Berhasil menghapus data");
            resetAll();
            autonumber();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_bHapusAnalisActionPerformed

    private void bCetakNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCetakNotaActionPerformed
        // Tombol cetak Laporan Pemeriksaan Pasien di Form Pasien
        String reportSource = null, reportDesc = null;
        try{
            reportSource = System.getProperty("user.dir") + "/src/laporan/DataPasien.jrxml";
            reportDesc = System.getProperty("user.dir") + "/src/laporan/DataPasien.jasper";
            JasperReport jasRep = JasperCompileManager.compileReport(reportSource);
            JasperPrint jp = JasperFillManager.fillReport(jasRep, null, conn);
            JasperExportManager.exportReportToHtmlFile(jp, reportDesc);
            JasperViewer.viewReport(jp, false);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Masih belum terhubung "+e);
        }
    }//GEN-LAST:event_bCetakNotaActionPerformed

    private void bPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPilihActionPerformed
        // Tombol Pilih di Menu Riwayat
        popup Pp = new popup();
        Pp.pasien = this;
        Pp.setVisible(true);
        Pp.setResizable(false);
    }//GEN-LAST:event_bPilihActionPerformed

    private void HapusDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusDataActionPerformed
        //Hapus di Menu Riwayat
        int index = tblRiwayat.getSelectedRow();
        String a = model3.getValueAt(index, 0).toString();
        try{
            PreparedStatement ps = conn.prepareStatement("delete from nota where id_nota='"+a+"'");
            ps.executeUpdate();
            model3.removeRow(index);
            JOptionPane.showMessageDialog(null, "Berhasil menghapus data");
            resetAll();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        tblRiwayat.setModel(model3);
    }//GEN-LAST:event_HapusDataActionPerformed

    private void BKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BKeluarActionPerformed
        // Tombol Keluar di Nota
        new Loginn().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BKeluarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BKeluar;
    private javax.swing.JPanel Daftar_Analis;
    private javax.swing.JTextArea FAlamat;
    private javax.swing.JTextField FTanggal;
    private javax.swing.JTextField Fidpasien;
    private javax.swing.JButton HapusData;
    private javax.swing.JPanel Menu_Analis;
    private javax.swing.JPanel Menu_Pasien;
    private javax.swing.JPanel Menu_Pemeriksaan;
    private javax.swing.JPanel Menu_Utama;
    private javax.swing.JPanel Nota;
    private javax.swing.JButton TBatal;
    private javax.swing.JButton THapus;
    private javax.swing.JButton TUbah;
    private javax.swing.JButton bBatal;
    private javax.swing.JButton bBatalAnalis;
    private javax.swing.JButton bBatalRiwayat;
    private javax.swing.JButton bCari;
    private javax.swing.JButton bCariPasien;
    private javax.swing.JButton bCetakNota;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bHapusAnalis;
    private javax.swing.JButton bPilih;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bSimpanAnalis;
    private javax.swing.JButton bSimpanRiwayat;
    private javax.swing.JButton bTambah;
    private javax.swing.JButton bUbah;
    private javax.swing.JButton bUbahAnalis;
    private javax.swing.JComboBox cbPemeriksaan;
    private javax.swing.JComboBox cbPendidikan;
    private javax.swing.JTextField fHasil;
    private javax.swing.JTextField fSubTest;
    private javax.swing.JTextField ftgl;
    private javax.swing.JTextField id_pasien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField nm_pasien;
    private javax.swing.JTextField tCariPasien;
    private javax.swing.JTable tblAnalis;
    private javax.swing.JTable tblPasien;
    private javax.swing.JTable tblPemeriksaan;
    private javax.swing.JTable tblRiwayat;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextArea txtAlamatAnalis;
    private javax.swing.JTextField txtCariAnalis;
    private javax.swing.JTextField txtIDAnalis;
    private javax.swing.JTextField txtNamaAnalis;
    private javax.swing.JTextField txtNamaPasien;
    private javax.swing.JTextField txtNoHP;
    private javax.swing.JTextField txtcari;
    private javax.swing.JPasswordField txtpas;
    // End of variables declaration//GEN-END:variables
}
