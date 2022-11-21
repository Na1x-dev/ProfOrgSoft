package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "public_organizations")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class PublicOrganization {
    @Id
    @Column(name = "public_organization_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long publicOrganizationId;

    @Column(name = "public_organization_title")
    @NonNull
    String publicOrganizationTitle;

    public PublicOrganization() {
    }
}
