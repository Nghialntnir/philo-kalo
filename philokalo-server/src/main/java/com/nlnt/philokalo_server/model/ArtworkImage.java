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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@ToString(exclude = {"artwork"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "artwork_image")
@NamedQueries({
    @NamedQuery(name = "ArtworkImage.findAll", query = "SELECT a FROM ArtworkImage a"),
    @NamedQuery(name = "ArtworkImage.findById", query = "SELECT a FROM ArtworkImage a WHERE a.id = :id"),
    @NamedQuery(name = "ArtworkImage.findByIsPrimary", query = "SELECT a FROM ArtworkImage a WHERE a.isPrimary = :isPrimary"),
    @NamedQuery(name = "ArtworkImage.findByBlurHash", query = "SELECT a FROM ArtworkImage a WHERE a.blurHash = :blurHash"),
    @NamedQuery(name = "ArtworkImage.findByWidth", query = "SELECT a FROM ArtworkImage a WHERE a.width = :width"),
    @NamedQuery(name = "ArtworkImage.findByHeight", query = "SELECT a FROM ArtworkImage a WHERE a.height = :height"),
    @NamedQuery(name = "ArtworkImage.findByFileSizeKb", query = "SELECT a FROM ArtworkImage a WHERE a.fileSizeKb = :fileSizeKb"),
    @NamedQuery(name = "ArtworkImage.findByFormat", query = "SELECT a FROM ArtworkImage a WHERE a.format = :format"),
    @NamedQuery(name = "ArtworkImage.findBySortOrder", query = "SELECT a FROM ArtworkImage a WHERE a.sortOrder = :sortOrder"),
    @NamedQuery(name = "ArtworkImage.findByCreatedAt", query = "SELECT a FROM ArtworkImage a WHERE a.createdAt = :createdAt")})
public class ArtworkImage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "is_primary")
    private Boolean isPrimary;
    @NotBlank
    @Lob
    @Size(max = 65535)
    @Column(name = "original_url")
    private String originalUrl;
    @Lob
    @Size(max = 65535)
    @Column(name = "full_url")
    private String fullUrl;
    @Lob
    @Size(max = 65535)
    @Column(name = "medium_url")
    private String mediumUrl;
    @NotBlank
    @Lob
    @Size(max = 65535)
    @Column(name = "thumb_url")
    private String thumbUrl;
    @Size(max = 100)
    @Column(name = "blur_hash")
    private String blurHash;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Column(name = "file_size_kb")
    private Integer fileSizeKb;
    @Size(max = 10)
    @Column(name = "format")
    private String format;
    @Column(name = "sort_order")
    private Short sortOrder;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
    @JoinColumn(name = "artwork_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Artwork artwork;

}
