package Registraduria.seguridad.Repositorios;

import Registraduria.seguridad.Modelos.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface RepositorioRol extends MongoRepository<Rol,String> {
}
