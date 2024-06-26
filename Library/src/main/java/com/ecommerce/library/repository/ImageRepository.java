package com.ecommerce.library.repository;

import com.ecommerce.library.model.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query(value = "select * from images where product_id = :id", nativeQuery = true)
    List<Image> findImageBy(@Param("id")long id);
    @Modifying
    @Query(value = "delete from images where product_id = :id",nativeQuery = true)
    void deleteImagesByProductId(@Param("id") long id);

    @Query("SELECT COUNT(i) > 0 FROM Image i WHERE i.name = :imageName AND i.product.id = :productId")
    boolean existsByNameAndProductId(@Param("imageName") String imageName, @Param("productId") Long productId);


    @Modifying
    @Query(value = "delete from images where name = :name",nativeQuery = true)
    void deleteByName(@Param("name") String name);

}
