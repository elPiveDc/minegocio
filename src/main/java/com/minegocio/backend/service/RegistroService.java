package com.minegocio.backend.service;

import com.minegocio.backend.entity.BaseDatosFranquicia;
import com.minegocio.backend.entity.Franquicia;
import com.minegocio.backend.entity.ObjetoBdFranquicia;
import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.entity.BaseDatosFranquicia.EstadoBd;
import com.minegocio.backend.repository.BaseDatosFranquiciaRepository;
import com.minegocio.backend.repository.FranquiciaRepository;
import com.minegocio.backend.repository.ObjetoBdFranquiciaRepository;
import com.minegocio.backend.repository.UsuarioRepository;
import com.minegocio.backend.util.ConexionFranquiciaUtil;
import com.minegocio.backend.util.PasswordUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final FranquiciaRepository franquiciaRepository;
    private final BaseDatosFranquiciaRepository bdRepository;
    private final ObjetoBdFranquiciaRepository objetoRepository;
    private final ConexionFranquiciaUtil conexionUtil;

    public RegistroService(
            UsuarioRepository usuarioRepository,
            FranquiciaRepository franquiciaRepository,
            BaseDatosFranquiciaRepository bdRepository,
            ObjetoBdFranquiciaRepository objetoRepository,
            ConexionFranquiciaUtil conexionUtil) {

        this.usuarioRepository = usuarioRepository;
        this.franquiciaRepository = franquiciaRepository;
        this.bdRepository = bdRepository;
        this.objetoRepository = objetoRepository;
        this.conexionUtil = conexionUtil;
    }

    @Transactional
    public Usuario registrarUsuarioConFranquicia(
            String nombreUsuario,
            String correo,
            String password,
            String nombreFranquicia,
            String nombreBd) {

        // 1 Validar si ya existe el correo
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo ya está en uso. Intenta con otro.");
        }

        String contraseñaHash = PasswordUtil.hashPassword(password);

        // 2 Crear usuario central
        Usuario usuario = new Usuario();
        usuario.setNombre(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setPasswordHash(contraseñaHash);
        usuario = usuarioRepository.save(usuario);

        // 3 Crear franquicia
        Franquicia franquicia = new Franquicia();
        franquicia.setUsuario(usuario);
        franquicia.setNombreFranquicia(nombreFranquicia);
        franquicia = franquiciaRepository.save(franquicia);

        // 4 Crear base de datos real(el metodo:
        // conexionUtil.crearBaseDeDatos(nombreBd))
        // y
        // se registra BD en la tabla central
        BaseDatosFranquicia bd = new BaseDatosFranquicia();
        bd.setFranquicia(franquicia);
        bd.setNombreBd(nombreBd);
        bd.setEstado(EstadoBd.CONFIGURADA);

        // 5 se pasa el Nombre de la franquicia y la contraseña en base a su dimencion
        // para la creacion de las base de
        // datos, su suario y su asignacion de privilegios
        String contraseñaBD = PasswordUtil.generarPasswordSegura(nombreFranquicia.length());
        bd.setUrlConexion(conexionUtil.crearBaseDeDatos(nombreBd, nombreFranquicia, contraseñaBD));
        bd.setUsuarioConexion(nombreFranquicia);
        // se le pasa a la bdcentral el pasword de conexion
        bd.setPassConexionHash(contraseñaBD);
        bd = bdRepository.save(bd);

        // 6 Crear tabla de usuarios en la BD de la franquicia y a su vez crea el primer
        // usuario de franquicia
        String columnasJson = conexionUtil.crearTablaUsuarios(nombreBd, nombreUsuario, correo, contraseñaHash);

        // 7 Registrar objeto en la tabla central
        ObjetoBdFranquicia objeto = new ObjetoBdFranquicia();
        objeto.setBaseDatosFranquicia(bd);
        objeto.setNombreTabla("usuarios");
        objeto.setEsTablaUsuarios(true);
        objeto.setColumnas(columnasJson);
        objetoRepository.save(objeto);

        return usuario;
    }
}
