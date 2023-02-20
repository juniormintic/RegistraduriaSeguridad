package Registraduria.seguridad.Repositorios;

import Registraduria.seguridad.Modelos.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioUsuario extends MongoRepository <Usuario, String> {

    //validacion de login
    @Query("{'email':?0}")
    public Usuario findByEmail(String email);
}
