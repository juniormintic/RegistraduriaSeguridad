package Registraduria.seguridad.Controladores;


import Registraduria.seguridad.Modelos.Rol;
import Registraduria.seguridad.Modelos.Usuario;
import Registraduria.seguridad.Repositorios.RepositorioRol;
import Registraduria.seguridad.Repositorios.RepositorioUsuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

//para los console log.info o log.error etc
@Slf4j
//para permitir llamadas http desde cualquier lado o la seguridad no permite
@CrossOrigin
// para indicar que sera de tipo rest
@RestController
// para indicar la ruta url
@RequestMapping("/usuario")
public class ControladorUsuario {

    @Autowired
    RepositorioUsuario repositorioUsuario;
    @Autowired
    RepositorioRol repositorioRol;

    @GetMapping
    public List<Usuario> listarUsuarios(){
        return repositorioUsuario.findAll();
    }

    @GetMapping("{idUsuario}")
    public  Usuario buscarUsuario(@PathVariable String idUsuario){
        return  repositorioUsuario.findById(idUsuario).orElse(null);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario infoUsuario){
        String password=convertirSHA256(infoUsuario.getContrasenia());
        infoUsuario.setContrasenia(password);
        return repositorioUsuario.save(infoUsuario);
    }
    @PutMapping("{idUsuario}")
    public Usuario actuaizarUsuario(@PathVariable String idUsuario, @RequestBody Usuario infoUsuario ){
        Usuario usuarioActual =repositorioUsuario.findById(idUsuario).orElse(null);
        if(usuarioActual != null){
            usuarioActual.setSeudonimo(infoUsuario.getSeudonimo());
            usuarioActual.setEmail(infoUsuario.getEmail());
            usuarioActual.setContrasenia(convertirSHA256(infoUsuario.getContrasenia()));
            return repositorioUsuario.save(usuarioActual);
        }else {
            return null;
        }
    }

    //Relación (1 a n) entre rol y usuario
    @PutMapping("{id}/rol/{id_rol}")
    public Usuario asignarRolAUsuario(@PathVariable String id,@PathVariable String id_rol){
        Usuario usuarioActual=repositorioUsuario
                .findById(id)
                .orElse(null);
        Rol rolActual=repositorioRol
                .findById(id_rol)
                .orElse(null);

        if (usuarioActual !=null && rolActual !=null){
            usuarioActual.setRol(rolActual);
            return repositorioUsuario.save(usuarioActual);
        }else {
            return null;
        }
    }

    @DeleteMapping("{idUsuario}")
    public void eliminarUsuario(@PathVariable String idUsuario){
        repositorioUsuario.deleteById(idUsuario);
    }

    @PostMapping("validar-usuario")
    public Usuario validarUsuario(@RequestBody Usuario infoUsuario, HttpServletResponse response) throws IOException {
        log.info("Validando el usuario, request body: {}", infoUsuario);

        //Busco el usuario en base de datos dado el email
        Usuario usuarioActual = repositorioUsuario.findByEmail(infoUsuario.getEmail());
        if (usuarioActual != null) {
            //Comparar las contraseñas que llegan desde postman y la que está en BD
            String contrasenaUsuario = convertirSHA256(infoUsuario.getContrasenia());
            String contrasenaBaseDatos = usuarioActual.getContrasenia();

            if (contrasenaUsuario.equals(contrasenaBaseDatos)) {
                usuarioActual.setContrasenia("");
                return usuarioActual;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }

    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
