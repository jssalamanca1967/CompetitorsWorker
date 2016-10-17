package models;

import com.avaje.ebean.Model;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 18/08/2016.
 */
@Entity
@Table(name = "competidor")
public class Competidor extends Model {

    public static Finder<Long,Competidor> FINDER = new Finder<>(Competidor.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    public Long id;

    @Column(unique=true)
    public String nombre;

    public String email;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Inscripcion> inscripciones;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date created;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date updated;

    @PrePersist
    protected void onCreate() {
        created = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date(System.currentTimeMillis());
    }

    public Competidor(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        inscripciones = new ArrayList<>();
    }
}
