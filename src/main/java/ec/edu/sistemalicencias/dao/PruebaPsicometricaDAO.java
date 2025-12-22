package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.entities.PruebaPsicometrica;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PruebaPsicometricaDAO {

    private final DatabaseConfig dbConfig;

    public PruebaPsicometricaDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    private static final String INSERT_SQL = "INSERT INTO pruebas_psicometricas " +
            "(conductor_id, fecha_prueba, puntaje_visual, puntaje_auditivo, puntaje_motor, puntaje_psicologico, puntaje_reaccion, observaciones) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID = "SELECT * FROM pruebas_psicometricas WHERE id = ?";

    private static final String SELECT_LAST_APPROVED = "SELECT * FROM pruebas_psicometricas WHERE conductor_id = ? ORDER BY id DESC";

    private static final String SELECT_BY_CONDUCTOR = "SELECT * FROM pruebas_psicometricas WHERE conductor_id = ?";

    public Long guardar(PruebaPsicometrica prueba) throws BaseDatosException {
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, prueba.getConductorId());
            // Usar Timestamp suele ser mejor para guardar fecha Y hora exacta
            stmt.setTimestamp(2, Timestamp.valueOf(prueba.getFechaRealizacion()));
            stmt.setDouble(3, prueba.getNotaPercepcion());
            stmt.setDouble(4, prueba.getNotaAtencion());
            stmt.setDouble(5, prueba.getNotaCoordinacion());
            stmt.setDouble(6, prueba.getNotaPsicologica());
            stmt.setDouble(7, prueba.getNotaReaccion()); // Columna real en BD
            stmt.setString(8, prueba.getObservaciones());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) throw new BaseDatosException("No se pudo guardar la prueba, no se crearon filas.");

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
                else throw new BaseDatosException("No se obtuvo el ID generado.");
            }

        } catch (SQLException e) {
            throw new BaseDatosException("Error CRÍTICO al guardar prueba psicométrica: " + e.getMessage(), e);
        }
    }

    public PruebaPsicometrica buscarPorId(Long id) throws BaseDatosException {
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPrueba(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al buscar prueba por ID: " + e.getMessage(), e);
        }
    }

    public PruebaPsicometrica obtenerUltimaPruebaAprobada(Long conductorId) throws BaseDatosException {
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_LAST_APPROVED)) {

            stmt.setLong(1, conductorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PruebaPsicometrica p = mapearPrueba(rs);
                    if (p.estaAprobado()) {
                        return p;
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            throw new BaseDatosException("Error al buscar historial de pruebas: " + e.getMessage(), e);
        }
    }

    public List<PruebaPsicometrica> buscarPorConductor(Long conductorId) throws BaseDatosException {
        List<PruebaPsicometrica> lista = new ArrayList<>();
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CONDUCTOR)) {

            stmt.setLong(1, conductorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPrueba(rs));
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al listar pruebas del conductor", e);
        }
        return lista;
    }

    private PruebaPsicometrica mapearPrueba(ResultSet rs) throws SQLException {
        PruebaPsicometrica p = new PruebaPsicometrica();
        p.setId(rs.getLong("id"));
        p.setConductorId(rs.getLong("conductor_id"));

        Timestamp ts = rs.getTimestamp("fecha_prueba");
        if (ts != null) {
            p.setFechaRealizacion(ts.toLocalDateTime());
        } else {
            p.setFechaRealizacion(java.time.LocalDateTime.now());
        }

        p.setNotaPercepcion(rs.getDouble("puntaje_visual"));
        p.setNotaAtencion(rs.getDouble("puntaje_auditivo"));
        p.setNotaCoordinacion(rs.getDouble("puntaje_motor"));
        p.setNotaPsicologica(rs.getDouble("puntaje_psicologico"));

        p.setNotaReaccion(rs.getDouble("puntaje_reaccion"));
        p.setObservaciones(rs.getString("observaciones"));

        return p;
    }
}