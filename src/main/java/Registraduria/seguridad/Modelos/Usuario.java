package Registraduria.seguridad.Modelos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Usuario {
    @Id
    private String _id;
    private String seudonimo;
    private String email;
    private  String contrasenia;
    //private  Rol rol;
    @DBRef
    private Rol rol;

    public Usuario(String seudonimo, String email, String contrasenia) {
        this.seudonimo = seudonimo;
        this.email = email;
        this.contrasenia = contrasenia;
    }
}
