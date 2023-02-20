package Registraduria.seguridad.Repositorios;

import Registraduria.seguridad.Modelos.PermisoRol;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioPermisoRol extends MongoRepository <PermisoRol, String> {
    @Query("{'rol.$id': ObjectId(?0),'permiso.$id': ObjectId(?1)}")
    public PermisoRol findByRolAndPermissions(String idRol,String idPermiso);
}
