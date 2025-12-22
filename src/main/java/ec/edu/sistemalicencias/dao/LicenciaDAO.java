package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.entities.Licencia;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;
import ec.edu.sistemalicencias.model.interfaces.Persistible;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Licencia.
 * Gestiona la persistencia de las licencias de conducir.
 */
public class LicenciaDAO implements Persistible<Licencia> {

    private final DatabaseConfig dbConfig;

    public LicenciaDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    @Override
    public Long guardar(Licencia licencia) throws BaseDatosException {
        if (licencia.getId() == null) {
            return insertar(licencia);
        } else {
            actualizar(licencia);
            return licencia.getId();
        }
    }

    private Long insertar(Licencia licencia) throws BaseDatosException {
        String sql = "INSERT INTO licencias (numero_licencia, conductor_id, tipo_licencia, " +
                "fecha_emision, fecha_vencimiento, activa, prueba_psicometrica_id, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, licencia.getNumeroLicencia());
            stmt.setLong(2, licencia.getConductorId());
            stmt.setString(3, licencia.getTipoLicencia());
            stmt.setDate(4, Date.valueOf(licencia.getFechaEmision()));
            stmt.setDate(5, Date.valueOf(licencia.getFechaVencimiento()));
            stmt.setBoolean(6, licencia.isActiva());

            // Manejo seguro del ID de la prueba (NULL o Long)
            if (licencia.getPruebaPsicometricaId() != null) {
                stmt.setLong(7, licencia.getPruebaPsicometricaId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }

            stmt.setString(8, licencia.getObservaciones());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new BaseDatosException("No se pudo insertar la licencia (0 filas afectadas).");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new BaseDatosException("No se pudo obtener el ID generado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new BaseDatosException("Error SQL al insertar licencia: " + e.getMessage(), e);
        }
    }

    private void actualizar(Licencia licencia) throws BaseDatosException {
        String sql = "UPDATE licencias SET numero_licencia = ?, conductor_id = ?, " +
                "tipo_licencia = ?, fecha_emision = ?, fecha_vencimiento = ?, " +
                "activa = ?, prueba_psicometrica_id = ?, observaciones = ? WHERE id = ?";

        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, licencia.getNumeroLicencia());
            stmt.setLong(2, licencia.getConductorId());
            stmt.setString(3, licencia.getTipoLicencia());
            stmt.setDate(4, Date.valueOf(licencia.getFechaEmision()));
            stmt.setDate(5, Date.valueOf(licencia.getFechaVencimiento()));
            stmt.setBoolean(6, licencia.isActiva());

            if (licencia.getPruebaPsicometricaId() != null) {
                stmt.setLong(7, licencia.getPruebaPsicometricaId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }

            stmt.setString(8, licencia.getObservaciones());
            stmt.setLong(9, licencia.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new BaseDatosException("No se encontró la licencia con ID: " + licencia.getId());
            }

        } catch (SQLException e) {
            throw new BaseDatosException("Error al actualizar licencia: " + e.getMessage(), e);
        }
    }

    @Override
    public Licencia buscarPorId(Long id) throws BaseDatosException {
        String sql = "SELECT * FROM licencias WHERE id = ?";
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearResultSet(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al buscar licencia por ID", e);
        }
    }

    public Licencia buscarPorNumero(String numeroLicencia) throws BaseDatosException {
        String sql = "SELECT * FROM licencias WHERE numero_licencia = ?";
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearResultSet(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al buscar licencia por número", e);
        }
    }

    public List<Licencia> buscarPorConductor(Long conductorId) throws BaseDatosException {
        String sql = "SELECT * FROM licencias WHERE conductor_id = ? ORDER BY fecha_emision DESC";
        List<Licencia> licencias = new ArrayList<>();
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, conductorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) licencias.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al buscar licencias por conductor", e);
        }
        return licencias;
    }

    public List<Licencia> obtenerTodas() throws BaseDatosException {
        String sql = "SELECT * FROM licencias ORDER BY fecha_emision DESC";
        List<Licencia> licencias = new ArrayList<>();
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) licencias.add(mapearResultSet(rs));
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener licencias", e);
        }
        return licencias;
    }

    public List<Licencia> obtenerLicenciasVigentes() throws BaseDatosException {
        String sql = "SELECT * FROM licencias WHERE activa = TRUE AND fecha_vencimiento > CURRENT_DATE ORDER BY fecha_vencimiento";
        List<Licencia> licencias = new ArrayList<>();
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) licencias.add(mapearResultSet(rs));
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener licencias vigentes", e);
        }
        return licencias;
    }

    @Override
    public boolean eliminar(Long id) throws BaseDatosException {
        String sql = "DELETE FROM licencias WHERE id = ?";
        try (Connection conn = dbConfig.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BaseDatosException("Error al eliminar licencia", e);
        }
    }

    private Licencia mapearResultSet(ResultSet rs) throws SQLException {
        Licencia licencia = new Licencia();
        licencia.setId(rs.getLong("id"));
        licencia.setNumeroLicencia(rs.getString("numero_licencia"));
        licencia.setConductorId(rs.getLong("conductor_id"));
        licencia.setTipoLicencia(rs.getString("tipo_licencia"));
        licencia.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
        licencia.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
        licencia.setActiva(rs.getBoolean("activa"));
        licencia.setObservaciones(rs.getString("observaciones"));

        long pruebaId = rs.getLong("prueba_psicometrica_id");
        if (!rs.wasNull()) {
            licencia.setPruebaPsicometricaId(pruebaId);
        }
        return licencia;
    }
}