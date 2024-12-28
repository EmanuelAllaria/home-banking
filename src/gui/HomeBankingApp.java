package gui;

import dao.DatabaseConnection;
import org.h2.tools.Server;
import service.CuentaService;
import service.ResumenService;
import service.TarjetaService;
import service.TransferenciaService;
import service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HomeBankingApp {

    private static Server server;
    private static UsuarioService usuarioService;
    private static CuentaService cuentaService;
    private static TarjetaService tarjetaService;
    private static TransferenciaService transferenciaService;
    private static ResumenService resumenService;

    public static void main(String[] args) {
        try {
            // Cambia el aspecto visual a Nimbus para un diseño moderno
            UIManager.setLookAndFeel(new NimbusLookAndFeel());

            // Inicia el servidor de H2 y la conexión a la base de datos
            server = Server.createTcpServer().start();
            Connection connection = DatabaseConnection.getInstance().getConnection();

            // Inicializa los servicios sin necesidad de pasar la conexión
            usuarioService = new UsuarioService();
            cuentaService = new CuentaService();
            tarjetaService = new TarjetaService();
            transferenciaService = new TransferenciaService();
            resumenService = new ResumenService();

            createTables(connection);  // Asegúrate de que las tablas existen

            JFrame loginFrame = new JFrame("HomeBanking - Login");
            loginFrame.setSize(400, 250);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setLocationRelativeTo(null); // Centra la ventana
            JPanel loginPanel = new JPanel();
            loginPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            loginPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Diseño más limpio
            loginFrame.add(loginPanel);
            placeLoginComponents(loginPanel, loginFrame);
            loginFrame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS Usuarios (ID INT PRIMARY KEY AUTO_INCREMENT, Nombre VARCHAR(255), Saldo DOUBLE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Cuentas (ID INT PRIMARY KEY AUTO_INCREMENT, UsuarioID INT NOT NULL, Tipo VARCHAR(255), Saldo DOUBLE, TasaInteres DOUBLE DEFAULT 20.0, FOREIGN KEY (UsuarioID) REFERENCES Usuarios(ID) ON DELETE CASCADE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Tarjetas (ID INT PRIMARY KEY AUTO_INCREMENT, UsuarioID INT NOT NULL, Numero VARCHAR(255), Limite DOUBLE, Saldo DOUBLE, SaldoAPagar DOUBLE, Tipo VARCHAR(255), TasaInteres DOUBLE, FOREIGN KEY (UsuarioID) REFERENCES Usuarios(ID) ON DELETE CASCADE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Transferencias (ID INT PRIMARY KEY AUTO_INCREMENT, IdOrigen INT NOT NULL, IdDestino INT NOT NULL, Monto DOUBLE, FOREIGN KEY (IdOrigen) REFERENCES Cuentas(ID) ON DELETE CASCADE, FOREIGN KEY (IdDestino) REFERENCES Cuentas(ID) ON DELETE CASCADE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS MovimientosTarjeta (ID INT PRIMARY KEY AUTO_INCREMENT, TarjetaID INT NOT NULL, Monto DOUBLE NOT NULL, Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (TarjetaID) REFERENCES Tarjetas(ID) ON DELETE CASCADE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS MovimientosCuenta (ID INT PRIMARY KEY AUTO_INCREMENT, CuentaID INT NOT NULL, Tipo ENUM('Transferencia', 'Tarjeta', 'Cuenta') NOT NULL, Monto DOUBLE NOT NULL, Detalle VARCHAR(500), Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (CuentaID) REFERENCES Cuentas(ID) ON DELETE CASCADE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS AuditLogs (ID INT PRIMARY KEY AUTO_INCREMENT, UsuarioID INT, Action VARCHAR(50), Details VARCHAR(255), Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (UsuarioID) REFERENCES Usuarios(ID) ON DELETE SET NULL)");
    }

    private static void placeLoginComponents(JPanel panel, JFrame loginFrame) {
        JLabel titleLabel = new JLabel("Bienvenido a HomeBanking", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setPreferredSize(new Dimension(100, 25));
        JTextField userText = new JTextField(20);
        userPanel.add(userLabel);
        userPanel.add(userText);
        panel.add(userPanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setPreferredSize(new Dimension(100, 25));
        JPasswordField passwordText = new JPasswordField(20);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordText);
        panel.add(passwordPanel);

        JButton loginButton = new JButton("Ingresar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 153, 51));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String password = new String(passwordText.getPassword());
                if (user.equals("admin") && password.equals("1234")) {
                    loginFrame.dispose();
                    showMainFrame();
                } else {
                    JOptionPane.showMessageDialog(panel, "Usuario o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void showMainFrame() {
        JFrame mainFrame = new JFrame("HomeBanking");
        mainFrame.setSize(1000, 700);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(240, 248, 255)); // Color de fondo
        mainFrame.add(desktopPane);

        JMenuBar menuBar = new JMenuBar();

        JMenu mainMenu = new JMenu("Opciones");
        mainMenu.setFont(new Font("Arial", Font.BOLD, 16));

        JMenuItem userManagementItem = new JMenuItem("Gestión de Usuarios");
        JMenuItem accountManagementItem = new JMenuItem("Gestión de Cuentas");
        JMenuItem cardManagementItem = new JMenuItem("Gestión de Tarjetas");
        JMenuItem transferItem = new JMenuItem("Transferencias");
        JMenuItem resumenItem = new JMenuItem("Gestión de Resúmenes");

        userManagementItem.setFont(new Font("Arial", Font.PLAIN, 14));
        accountManagementItem.setFont(new Font("Arial", Font.PLAIN, 14));
        cardManagementItem.setFont(new Font("Arial", Font.PLAIN, 14));
        transferItem.setFont(new Font("Arial", Font.PLAIN, 14));
        resumenItem.setFont(new Font("Arial", Font.PLAIN, 14));

        userManagementItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionUsuariosFrame = new JInternalFrame("Gestión de Usuarios", true, true, true, true);
                gestionUsuariosFrame.setSize(600, 400);
                desktopPane.add(gestionUsuariosFrame);
                gestionUsuariosFrame.setVisible(true);
                UsuarioGUI.placeGestionUsuariosComponents(gestionUsuariosFrame);
            }
        });

        accountManagementItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionCuentasFrame = new JInternalFrame("Gestión de Cuentas", true, true, true, true);
                gestionCuentasFrame.setSize(600, 400);
                desktopPane.add(gestionCuentasFrame);
                gestionCuentasFrame.setVisible(true);
                CuentaGUI.placeGestionCuentasComponents(gestionCuentasFrame);
            }
        });

        cardManagementItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionTarjetasFrame = new JInternalFrame("Gestión de Tarjetas", true, true, true, true);
                gestionTarjetasFrame.setSize(600, 400);
                desktopPane.add(gestionTarjetasFrame);
                gestionTarjetasFrame.setVisible(true);
                TarjetaGUI.placeGestionTarjetasComponents(gestionTarjetasFrame);
            }
        });

        transferItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame transferenciasFrame = new JInternalFrame("Transferencias", true, true, true, true);
                transferenciasFrame.setSize(600, 400);
                desktopPane.add(transferenciasFrame);
                transferenciasFrame.setVisible(true);
                TransferenciaGUI.placeTransferenciasComponents(transferenciasFrame);
            }
        });

        resumenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionResumenFrame = new JInternalFrame("Gestión de Resúmenes", true, true, true, true);
                gestionResumenFrame.setSize(600, 400);
                desktopPane.add(gestionResumenFrame);
                gestionResumenFrame.setVisible(true);
                ResumenGUI.placeGestionResumenComponents(gestionResumenFrame);
            }
        });

        mainMenu.add(userManagementItem);
        mainMenu.add(accountManagementItem);
        mainMenu.add(cardManagementItem);
        mainMenu.add(transferItem);
        mainMenu.add(resumenItem);

        menuBar.add(mainMenu);
        mainFrame.setJMenuBar(menuBar);

        mainFrame.setVisible(true);
    }
}