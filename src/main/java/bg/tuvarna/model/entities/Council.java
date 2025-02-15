package bg.tuvarna.model.entities;

import bg.tuvarna.enums.CouncilRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "council")
public class Council {
    @Id
    @Column(name = "profile_id", nullable = false)
    private Long id;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<CouncilRole> roles;
    private int fromYear;
    private int toYear;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    @JsonIgnore
    private Profile profile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public List<CouncilRole> getRoles() {
        return roles;
    }

    public void setRoles(List<CouncilRole> roles) {
        this.roles = roles;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }
}
