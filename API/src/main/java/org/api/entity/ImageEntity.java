package org.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "post_images", schema = "allergysafediet_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(name = "image_data", columnDefinition="bytea" ,nullable = false)
    private byte[] imageData;

    public ImageEntity(PostEntity post, byte[] imageData) {
        this.post = post;
        this.imageData = imageData;
    }
}
