package com.nlnt.philokalo_server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.hibernate.annotations.UpdateTimestamp;

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
@ToString(exclude = {"categorySet", "tagSet", "artworkCommentSet", "artworkLikeSet", "artworkImageSet", "artist"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "artwork")
@NamedQueries({
    @NamedQuery(name = "Artwork.findAll", query = "SELECT a FROM Artwork a"),
    @NamedQuery(name = "Artwork.findById", query = "SELECT a FROM Artwork a WHERE a.id = :id"),
    @NamedQuery(name = "Artwork.findByTitle", query = "SELECT a FROM Artwork a WHERE a.title = :title"),
    @NamedQuery(name = "Artwork.findByMedium", query = "SELECT a FROM Artwork a WHERE a.medium = :medium"),
    @NamedQuery(name = "Artwork.findByYearCreated", query = "SELECT a FROM Artwork a WHERE a.yearCreated = :yearCreated"),
    @NamedQuery(name = "Artwork.findByStatus", query = "SELECT a FROM Artwork a WHERE a.status = :status"),
    @NamedQuery(name = "Artwork.findByIsForSale", query = "SELECT a FROM Artwork a WHERE a.isForSale = :isForSale"),
    @NamedQuery(name = "Artwork.findByPrice", query = "SELECT a FROM Artwork a WHERE a.price = :price"),
    @NamedQuery(name = "Artwork.findByCurrency", query = "SELECT a FROM Artwork a WHERE a.currency = :currency"),
    @NamedQuery(name = "Artwork.findByViewCount", query = "SELECT a FROM Artwork a WHERE a.viewCount = :viewCount"),
    @NamedQuery(name = "Artwork.findByLikeCount", query = "SELECT a FROM Artwork a WHERE a.likeCount = :likeCount"),
    @NamedQuery(name = "Artwork.findByCreatedAt", query = "SELECT a FROM Artwork a WHERE a.createdAt = :createdAt"),
    @NamedQuery(name = "Artwork.findByUpdatedAt", query = "SELECT a FROM Artwork a WHERE a.updatedAt = :updatedAt")})
public class Artwork implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private String id;
    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Size(max = 100)
    @Column(name = "medium")
    private String medium;
    @Column(name = "year_created")
    @Temporal(TemporalType.DATE)
    private Date yearCreated;
    @Size(max = 9)
    @Column(name = "status")
    private String status;
    @Column(name = "is_for_sale")
    private Boolean isForSale;
    @Column(name = "price")
    private BigDecimal price;
    @Size(max = 3)
    @Column(name = "currency")
    private String currency;
    @Column(name = "view_count")
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
    @JoinTable(name = "artwork_category", joinColumns = {
        @JoinColumn(name = "artwork_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "category_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<Category> categorySet;
    @JoinTable(name = "artwork_tag", joinColumns = {
        @JoinColumn(name = "artwork_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "tag_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<Tag> tagSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artwork")
    private Set<ArtworkComment> artworkCommentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artwork")
    private Set<ArtworkLike> artworkLikeSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artwork")
    private Set<ArtworkImage> artworkImageSet;
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User artist;

}
