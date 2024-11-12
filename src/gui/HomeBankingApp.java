package gui;


import dao.DatabaseConnection;
import org.h2.tools.Server;
import service.CuentaService;
import service.ResumenService;
import service.TarjetaService;
import service.TransferenciaService;
import service.UsuarioService;

import javax.swing.*;
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

            JFrame loginFrame = new JFrame("Login");
            loginFrame.setSize(300, 200);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel loginPanel = new JPanel();
            loginFrame.add(loginPanel);
            placeLoginComponents(loginPanel, loginFrame);
            loginFrame.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS Usuarios (ID INT PRIMARY KEY AUTO_INCREMENT, Nombre VARCHAR(255), Saldo DOUBLE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Cuentas (ID INT PRIMARY KEY AUTO_INCREMENT, UsuarioID INT, Tipo VARCHAR(255), Saldo DOUBLE, TasaInteres DOUBLE DEFAULT 20.0)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Tarjetas (ID INT PRIMARY KEY AUTO_INCREMENT, UsuarioID INT, Numero VARCHAR(255), Limite DOUBLE, Saldo DOUBLE, Tipo VARCHAR(255), TasaInteres DOUBLE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Transferencias (ID INT PRIMARY KEY AUTO_INCREMENT, IdOrigen INT, IdDestino INT, Monto DOUBLE)");
        stmt.execute("CREATE TABLE IF NOT EXISTS MovimientosTarjeta (ID INT PRIMARY KEY AUTO_INCREMENT, TarjetaID INT NOT NULL, Monto DOUBLE NOT NULL, Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        stmt.execute("CREATE TABLE IF NOT EXISTS MovimientosCuenta (ID INT PRIMARY KEY AUTO_INCREMENT, CuentaID INT NOT NULL, Tipo ENUM('Transferencia', 'Tarjeta', 'Cuenta') NOT NULL, Monto DOUBLE NOT NULL, Detalle VARCHAR(500), Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        stmt.execute("CREATE TABLE IF NOT EXISTS AuditLogs (ID INT PRIMARY KEY AUTO_INCREMENT, UsuarioID INT, Action VARCHAR(50), Details VARCHAR(255), Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }

    private static void placeLoginComponents(JPanel panel, JFrame loginFrame) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Usuario");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Ingresar");
        loginButton.setBounds(10, 80, 150, 25);
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
        JFrame mainFrame = new JFrame("Home Banking");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JDesktopPane desktopPane = new JDesktopPane();
        mainFrame.add(desktopPane);

        JMenuBar menuBar = new JMenuBar();

        JMenu mainMenu = new JMenu("Opciones");


        JMenuItem userManagementItem = new JMenuItem("Gestión de Usuarios");
        JMenuItem accountManagementItem = new JMenuItem("Gestión de Cuentas");
        JMenuItem cardManagementItem = new JMenuItem("Gestión de Tarjetas");
        JMenuItem transferItem = new JMenuItem("Transferencias");
        JMenuItem resumenItem = new JMenuItem("Gestión de Resúmenes");


        userManagementItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionUsuariosFrame = new JInternalFrame("Gestión de Usuarios", true, true, true, true);
                gestionUsuariosFrame.setSize(350, 300);
                desktopPane.add(gestionUsuariosFrame);
                gestionUsuariosFrame.setVisible(true);
                UsuarioGUI.placeGestionUsuariosComponents(gestionUsuariosFrame);
            }
        });

        accountManagementItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionCuentasFrame = new JInternalFrame("Gestión de Cuentas", true, true, true, true);
                gestionCuentasFrame.setSize(350, 300);
                desktopPane.add(gestionCuentasFrame);
                gestionCuentasFrame.setVisible(true);
                CuentaGUI.placeGestionCuentasComponents(gestionCuentasFrame);
            }
        });

        cardManagementItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionTarjetasFrame = new JInternalFrame("Gestión de Tarjetas", true, true, true, true);
                gestionTarjetasFrame.setSize(350, 300);
                desktopPane.add(gestionTarjetasFrame);
                gestionTarjetasFrame.setVisible(true);
                TarjetaGUI.placeGestionTarjetasComponents(gestionTarjetasFrame);
            }
        });

        transferItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame transferenciasFrame = new JInternalFrame("Transferencias", true, true, true, true);
                transferenciasFrame.setSize(350, 300);
                desktopPane.add(transferenciasFrame);
                transferenciasFrame.setVisible(true);
                TransferenciaGUI.placeTransferenciasComponents(transferenciasFrame);
            }
        });

        resumenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame gestionResumenFrame = new JInternalFrame("Gestión de Resúmenes", true, true, true, true);
                gestionResumenFrame.setSize(350, 300);
                desktopPane.add(gestionResumenFrame);
                gestionResumenFrame.setVisible(true);
                ResumenGUI.placeGestionResumenComponents(gestionResumenFrame);
            }
        });

        // Add menu items to the menu
        mainMenu.add(userManagementItem);
        mainMenu.add(accountManagementItem);
        mainMenu.add(cardManagementItem);
        mainMenu.add(transferItem);
        mainMenu.add(resumenItem);

        // Add the main menu to the menu bar
        menuBar.add(mainMenu);

        // Set the menu bar for the JFrame
        mainFrame.setJMenuBar(menuBar);

        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                    if (server != null) {
                        server.stop();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        mainFrame.setVisible(true);
    }

    private static void openWindow(String title) {
        JFrame window = new JFrame(title);
        window.setSize(300, 200);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Bienvenido a " + title, SwingConstants.CENTER);
        window.add(label);

        window.setVisible(true);
    }
}
