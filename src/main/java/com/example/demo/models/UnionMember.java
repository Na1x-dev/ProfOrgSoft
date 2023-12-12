package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "union_members")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class UnionMember {
    @Id
    @Column(name = "union_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long unionMemberId;

    @NonNull
    @Column(name = "surname")
    String surname;

    @Column(name = "name")
    @NonNull
    String name;

    @Column(name = "patronymic")
    @NonNull
    String patronymic;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthdate")
    @NonNull
    Date birthdate;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false, referencedColumnName = "gender_id")
    @NonNull
    Gender gender;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false, referencedColumnName = "position_id")
    @NonNull
    Position position;

    @JsonIgnore
    @OneToMany(mappedBy = "unionMember", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    List<Application> applications;

    @JsonIgnore
    @OneToMany(mappedBy = "unionMember", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    List<PhoneNumber> phoneNumbers;

    @ManyToMany(mappedBy = "unionMembers")
    @NonNull
    @ToString.Exclude
    Set<Child> children;

    @ManyToMany
    @JoinTable(
            name = "public_org_union_members",
            joinColumns = @JoinColumn(name = "union_member_id"),
            inverseJoinColumns = @JoinColumn(name = "public_organization_id"))
    Set<PublicOrganization> publicOrganizations;

//    @ManyToMany(mappedBy = "unionMembers")
//    @NonNull
//    Set<Child> children;

    public UnionMember() {
        surname = "";
        name = "";
        patronymic = "";
        gender = new Gender();
        birthdate = new Date();
        position = new Position();
        applications = new ArrayList<>();
        phoneNumbers = new ArrayList<>();
        children = new HashSet<>();
        phoneNumbers.add(new PhoneNumber());
    }

    public String getIdAndName() {
        if (!surname.equals("") || !name.equals("") || !patronymic.equals(""))
            return unionMemberId + ". " + surname + " " + name.charAt(0) + "." + patronymic.charAt(0) + ".";
        return "";
    }

    public String getDateInNormalFormat() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(birthdate);
    }

    public String formatDateForChange() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(birthdate);
    }

    public String getNameAndInitials() {
        String name;
        String patronymic;

        if (this.name.isEmpty()) name = this.name;
        else name = this.name.substring(0, 1);
        if (this.patronymic.isEmpty()) patronymic = this.patronymic;
        else patronymic = this.patronymic.substring(0, 1);
        return this.surname + " " + name + ". " + patronymic + ".";
    }

    @Override
    public String toString() {
        return "UnionMember{" +
                "unionMemberId=" + unionMemberId +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", phone='" + phoneNumbers.get(0).phoneNumber + '\'' +
                '}';
    }
}
