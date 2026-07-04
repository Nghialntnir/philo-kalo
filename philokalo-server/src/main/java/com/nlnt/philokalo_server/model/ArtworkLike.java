package com.nlnt.philokalo_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
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
@ToString(exclude = {"artwork", "user"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "artwork_like")
@NamedQueries({
    @NamedQuery(name = "ArtworkLike.findAll", query = "SELECT a FROM ArtworkLike a"),
    @NamedQuery(name = "ArtworkLike.findByUserId", query = "SELECT a FROM ArtworkLike a WHERE a.artworkLikePK.userId = :userId"),
    @NamedQuery(name = "ArtworkLike.findByArtworkId", query = "SELECT a FROM ArtworkLike a WHERE a.artworkLikePK.artworkId = :artworkId"),
    @NamedQuery(name = "ArtworkLike.findByCreatedAt", query = "SELECT a FROM ArtworkLike a WHERE a.createdAt = :createdAt")})
public class ArtworkLike implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArtworkLikePK artworkLikePK;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
    @JoinColumn(name = "artwork_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Artwork artwork;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
