package com.nlnt.philokalo_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author nghia
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"artwork", "parent", "user", "artworkCommentSet"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "artwork_comment")
@NamedQueries({
    @NamedQuery(name = "ArtworkComment.findAll", query = "SELECT a FROM ArtworkComment a"),
    @NamedQuery(name = "ArtworkComment.findById", query = "SELECT a FROM ArtworkComment a WHERE a.id = :id"),
    @NamedQuery(name = "ArtworkComment.findByIsHidden", query = "SELECT a FROM ArtworkComment a WHERE a.isHidden = :isHidden"),
    @NamedQuery(name = "ArtworkComment.findByCreatedAt", query = "SELECT a FROM ArtworkComment a WHERE a.createdAt = :createdAt")})
public class ArtworkComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private String id;
    @NotBlank
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content")
    private String content;
    @Column(name = "is_hidden")
    private Boolean isHidden;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
    @JoinColumn(name = "artwork_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Artwork artwork;
    @OneToMany(mappedBy = "parent")
    private Set<ArtworkComment> artworkCommentSet;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ArtworkComment parent;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
