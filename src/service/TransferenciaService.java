package service;

import clases.Cuenta;
import clases.Transferencia;
import dao.CuentaDAO;
import dao.TransferenciaDAO;
import java.sql.SQLException;

public class TransferenciaService {

    public String realizarTransferencia(Transferencia transferencia) throws SQLException {
        CuentaDAO cuentaDAO = new CuentaDAO();
        Cuenta cuentaOrigen = cuentaDAO.obtenerCuentaPorId(transferencia.getIdOrigen());
        Cuenta cuentaDestino = cuentaDAO.obtenerCuentaPorId(transferencia.getIdDestino());

        if (cuentaOrigen == null) {
            return "Error: La cuenta origen no existe.";
        }
        if (cuentaDestino == null) {
            return "Error: La cuenta destino no existe.";
        }
        if (cuentaOrigen.getSaldo() < transferencia.getMonto()) {
            return "Error: Fondos insuficientes en la cuenta origen.";
        }

        double saldoOrigenAntes = cuentaOrigen.getSaldo();
        double saldoDestinoAntes = cuentaDestino.getSaldo();

        // Actualización de saldos
        cuentaOrigen.retirar(transferencia.getMonto());
        cuentaDestino.setSaldo(saldoDestinoAntes + transferencia.getMonto());

        // Persistencia en base de datos
        cuentaDAO.actualizarCuenta(cuentaOrigen);
        cuentaDAO.actualizarCuenta(cuentaDestino);

        TransferenciaDAO transferenciaDAO = new TransferenciaDAO();
        transferenciaDAO.registrarTransferencia(transferencia);

        return "Transferencia realizada con éxito!\n\n" +
                "Cuenta Origen (antes): " + saldoOrigenAntes + " - (después): " + cuentaOrigen.getSaldo() + "\n" +
                "Cuenta Destino (antes): " + saldoDestinoAntes + " - (después): " + cuentaDestino.getSaldo();
    }
}
