package models;

import com.avaje.ebean.Model;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 18/08/2016.
 */
@Entity
@Table(name = "competencia")
public class Competencia extends Model {

    public static Finder<Long,Competencia> FINDER = new Finder<>(Competencia.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    public Long id;

    @Column(unique=true)
    public String nombre;

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

    public Competencia(String nombre) {
        this.nombre = nombre;
        created = new Date(System.currentTimeMillis());
        updated = new Date(System.currentTimeMillis());
    }
}
