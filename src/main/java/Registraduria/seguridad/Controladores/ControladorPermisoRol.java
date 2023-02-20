package Registraduria.seguridad.Controladores;

import Registraduria.seguridad.Modelos.Permiso;
import Registraduria.seguridad.Modelos.PermisoRol;
import Registraduria.seguridad.Modelos.Rol;
import Registraduria.seguridad.Repositorios.RepositorioPermiso;
import Registraduria.seguridad.Repositorios.RepositorioPermisoRol;
import Registraduria.seguridad.Repositorios.RepositorioRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permiso-rol")
public class ControladorPermisoRol {
    @Autowired
    private RepositorioPermisoRol RepositorioPermisosRol;
    @Autowired
    private RepositorioRol RepositorioRol;
    @Autowired
    private RepositorioPermiso RepositorioPermiso;

    //Asignación rol y permiso
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("rol/{id_rol}/permiso/{id_permiso}")
    public PermisoRol crearPermisoRol(@PathVariable String id_rol, @PathVariable String id_permiso) {
        PermisoRol nuevo = new PermisoRol();
        Rol rol = RepositorioRol.findById(id_rol).orElseThrow(RuntimeException::new);
        Permiso permiso = RepositorioPermiso.findById(id_permiso).orElseThrow(RuntimeException::new);
        if (rol != null && permiso != null) {
            nuevo.setPermiso(permiso);
            nuevo.setRol(rol);
            return RepositorioPermisosRol.save(nuevo);
        } else {
            return null;
        }
    }

    @GetMapping
    public List<PermisoRol> listaPermisosRol(){
        return RepositorioPermisosRol.findAll();
    }

    @GetMapping("{id}")
    public PermisoRol show(@PathVariable String id) {
        PermisoRol permisoRolActual = RepositorioPermisosRol
                .findById(id)
                .orElse(null);
        return permisoRolActual;
    }
    //modificacion permiso rol
    @PutMapping("{id}/rol/{id_rol}/permiso/{id_permiso}")
    public PermisoRol update(@PathVariable String id, @PathVariable String id_rol, @PathVariable String id_permiso) {
        PermisoRol permisoRolActual = RepositorioPermisosRol
                .findById(id)
                .orElse(null);
        Rol elRol = RepositorioRol.findById(id_rol).get();
        Permiso elPermiso = RepositorioPermiso.findById(id_permiso).get();
        if (permisoRolActual != null && elPermiso != null && elRol != null) {
            permisoRolActual.setPermiso(elPermiso);
            permisoRolActual.setRol(elRol);
            return RepositorioPermisosRol.save(permisoRolActual);
        } else {
            return null;
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        PermisoRol permisoRolActual = RepositorioPermisosRol
                .findById(id)
                .orElse(null);
        if (permisoRolActual != null) {
            RepositorioPermisosRol.delete(permisoRolActual);
        }
    }

    @GetMapping("validar-permiso/rol/{idRol}")
    public PermisoRol validarPermisosDelRol(@PathVariable String idRol, @RequestBody Permiso infoPermiso, HttpServletResponse response) throws IOException {

        //Buscar en base de datos el rol y permiso
        Rol rolActual = RepositorioRol.findById(idRol).orElse(null);
        Permiso permisoActual = RepositorioPermiso.getPermiso(infoPermiso.getUrl(), infoPermiso.getMetodo());

        //Validar si existe el rol y el permiso en base de datos
        if (rolActual != null && permisoActual != null) {

            String idRolActual = rolActual.get_id();
            String idPermisoActual = permisoActual.get_id();

            //Buscar en la tabla PermisosRol si el rol tiene asociado el permiso.
            PermisoRol permisosRolActual = RepositorioPermisosRol.findByRolAndPermissions(idRolActual, idPermisoActual);

            if (permisosRolActual != null) {
                return permisosRolActual;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

    }
}
