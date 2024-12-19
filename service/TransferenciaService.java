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

        if (cuentaOrigen != null && cuentaDestino != null && cuentaOrigen.getSaldo() >= transferencia.getMonto()) {
            double saldoOrigenAntes = cuentaOrigen.getSaldo();
            double saldoDestinoAntes = cuentaDestino.getSaldo();

            cuentaOrigen.setSaldo(saldoOrigenAntes - transferencia.getMonto());
            cuentaDestino.setSaldo(saldoDestinoAntes + transferencia.getMonto());

            cuentaDAO.actualizarCuenta(cuentaOrigen);
            cuentaDAO.actualizarCuenta(cuentaDestino);

            TransferenciaDAO transferenciaDAO = new TransferenciaDAO();
            transferenciaDAO.registrarTransferencia(transferencia);
            cuentaDAO.registrarMovimiento(cuentaOrigen.getId(), cuentaDestino.getUsuarioId(), transferencia.getMonto(), "Transferencia");

            return "Transferencia realizada con éxito!\n\n" +
                    "Cuenta Origen (antes): " + saldoOrigenAntes + " - (después): " + cuentaOrigen.getSaldo() + "\n" +
                    "Cuenta Destino (antes): " + saldoDestinoAntes + " - (después): " + cuentaDestino.getSaldo();
        } else {
            return "Transferencia fallida! Verifique los datos ingresados.";
        }
    }
}
