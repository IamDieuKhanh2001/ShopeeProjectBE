
package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUser;
import com.example.fsoft_shopee_nhom02.dto.CommentDTO;
import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.dto.ListOutputResult;
import com.example.fsoft_shopee_nhom02.mapper.CommentMapper;
import com.example.fsoft_shopee_nhom02.mapper.ProductMapper;
import com.example.fsoft_shopee_nhom02.model.*;
import com.example.fsoft_shopee_nhom02.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

//region--------------------Functions for Product Crud APIs---------------------------


    // Use for Upload image controller
    public ProductDTO saveProductImage(long id, MultipartFile imageProduct, MultipartFile image1, MultipartFile image2,
    MultipartFile image3, MultipartFile image4) {
        ProductEntity product = productRepository.findById(id).get();

        // ImageProduct url
        String imgProUrl = cloudinaryService.uploadFile(
                imageProduct,
                "ImageProduct",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgOneUrl = cloudinaryService.uploadFile(
                image1,
                "Image1",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgTwoUrl = cloudinaryService.uploadFile(
                image2,
                "Image2",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgThreeUrl = cloudinaryService.uploadFile(
                image3,
                "Image3",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgFourUrl = cloudinaryService.uploadFile(
                image4,
                "Image4",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        product.setImageProduct(imgProUrl);

        // Condition check if not have img (Not optimize)
        if(!imgOneUrl.equals("-1"))
            product.setImage1(imgOneUrl);
        else
            product.setImage1("");

        if(!imgTwoUrl.equals("-1"))
            product.setImage2(imgTwoUrl);
        else
            product.setImage2("");

        if(!imgThreeUrl.equals("-1"))
            product.setImage3(imgThreeUrl);
        else
            product.setImage3("");

        if(!imgFourUrl.equals("-1"))
            product.setImage4(imgFourUrl);
        else
            product.setImage4("");

        productRepository.save(product);

        return dtoHandler(product);
    }

    public ResponseEntity<?> save(ProductDTO productDTO) {
        ProductEntity product;

        if(productDTO.getId() != 0) {
            // UPDATE
            product = productRepository.findById(productDTO.getId())
                    .orElse(null);

            if(product == null) {
                return new ResponseEntity<>("Not found product id " + productDTO.getId(),
                        HttpStatus.BAD_REQUEST);
            }
            ProductMapper.toProductEntity(product, productDTO);
        }
        else {
            // CREATE
            product = ProductMapper.toProductEntity(productDTO);
            product.setSold(0L);
            product.setTotalView(0L);
            product.setStatus("Active");
        }
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findById(productDTO.getSubCategoryId())
                .orElse(null);

        if(subCategoryEntity == null) {
            return new ResponseEntity<>("Not found sub category id " + productDTO.getSubCategoryId(),
                    HttpStatus.BAD_REQUEST);
        }

        product.setSubCategoryEntity(subCategoryEntity);

        productRepository.save(product);

        return new ResponseEntity<>(ProductMapper.toProductDTO(product), HttpStatus.OK);
    }

    // Ham xu ly viec chuyen tu product sang DTO khi co truong Price
    ProductDTO dtoHandler(ProductEntity product) {
        ProductDTO productDTO = ProductMapper.toProductDTO(product);
        List<Long> priceList = typeRepository.findFirstPrice(product.getId());

        Long sumRating = commentRepository.sumProductReview(product.getId());

        // If condition de check neu sum = null thi cho danh gia tb = 0
        if(sumRating == null) {
            productDTO.setAvgRating(0);
        }
        // Tinh trung binh neu khac null
        else {
            long totalCmt = commentRepository.countAllByProductEntityIdAndStatus(
                    product.getId(), "Active");
            // Tinh trung binh va lam tron 1 so sau dau phay
            double avgRating = (double) sumRating / totalCmt;
            double roundAvgRating = (double) Math.round(avgRating * 10.0) / 10.0;
            productDTO.setAvgRating(roundAvgRating);
        }

        // List rong thi cho price = 0 neu khong se lay price tai index 0
        long price = priceList.isEmpty() ? 0 : priceList.get(0);

        productDTO.setPrice(price);

        return productDTO;
    }

    String checkTotalPage(long totalItems, long limit, long page) {
        long totalPage = (long) Math.ceil((double) totalItems / limit);

        page = Math.min(totalPage, page);

        return String.valueOf(page);
    }

    Boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    Boolean isValidPage(String page) {
        return page != null && !page.equals("") && isNumber(page) && Long.parseLong(page) >= 0;
    }

    public ResponseEntity<ListOutputResult> getAllProducts(String page, String limit) {
        ListOutputResult result = new ListOutputResult();
        long productsNumber = productRepository.countAllByStatus("Active");

        limit = (limit == null || limit.equals("")
                || !isNumber(limit) || Long.parseLong(limit) < 0) ? "12" : limit;
        page = (!isValidPage(page)) ? "1" :
                checkTotalPage(productsNumber, Long.parseLong(limit), Long.parseLong(page));

        List<ProductDTO> productDTOS = new ArrayList<>();

        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        List<ProductEntity> products = productRepository.findAllWithCatIdAndSubCatIdAndStatus(pageable);

        for (ProductEntity product : products) {
            ProductDTO productDTO = dtoHandler(product);

            productDTOS.add(productDTO);
        }

        if(products.isEmpty()) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }

        result.setList(productDTOS);
        result.setItemsNumber(productsNumber);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<?> getDetail(long id) {
        ProductEntity product = productRepository.findById(id)
                .orElse(null);

        if(product == null || product.getStatus().equals("Inactive")) {
            return new ResponseEntity<>("Cannot found product id " + id, HttpStatus.BAD_REQUEST);
        }

        // Update view
        product.setTotalView(product.getTotalView() + 1);
        productRepository.save(product);

        return new ResponseEntity<>(dtoHandler(product), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteProduct(long id) {
        ProductEntity product = productRepository.findById(id)
                .orElse(null);

        if(product == null) {
            return new ResponseEntity<>("Cannot found product id " + id, HttpStatus.BAD_REQUEST);
        }

//        productRepository.delete(product);
//        typeRepository.deleteAllByProductEntityId(id);

        if(product.getStatus().equals("Active")) {
            product.setStatus("Inactive");
        }
        else
            product.setStatus("Active");

        productRepository.save(product);

        return new ResponseEntity<>("State changed!", HttpStatus.OK);
    }

//endregion


//region-------------------------Functions for Types Crud APIs, Search Function---------------------------

    public ResponseEntity<?> getTypes(long id) {
        if(typeRepository.findAllByProductEntityId(id).size() > 0) {
            return new ResponseEntity<>(typeRepository.findAllByProductEntityId(id), HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found product id " + id, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> createTypes(long id, List<TypeEntity> types) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElse(null);

        if(productEntity == null) {
            return new ResponseEntity<>("Cannot found product id " + id, HttpStatus.BAD_REQUEST);
        }

        for(TypeEntity type : types) {
            type.setProductEntity(productEntity);
            typeRepository.save(type);
        }

        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    public ResponseEntity<?> updateAllTypes(long id, List<TypeEntity> typesList) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElse(null);

        if(productEntity == null) {
            return new ResponseEntity<>("Cannot found product id " + id, HttpStatus.BAD_REQUEST);
        }

        List<TypeEntity> updatedTypesList = typeRepository.findAllByProductEntityId(id);

//        List<Object> res = new ArrayList<>();

        for (TypeEntity updatedType : updatedTypesList) {
            TypeEntity type = typesList.get(updatedTypesList.indexOf(updatedType));
            updatedType.setPrice(type.getPrice());
            updatedType.setType(type.getType());
            updatedType.setQuantity(type.getQuantity());
            updatedType.setProductEntity(productEntity);
        }

        typeRepository.saveAll(updatedTypesList);

        return new ResponseEntity<>(updatedTypesList, HttpStatus.OK);
    }

    /*
        Search truyen 3 params: page, limit, keyword
        - Page, limit de phan trang.
        - keyword la tu khoa nhap vao de tim san pham.
        - Json respond se bao gom tu khoa vua nhap, so san pham tim duoc
          va list san pham.

     */
    List<ProductDTO> filterByPriceRange(String minPrice, String maxPrice, long defaultMaxPrice,
                                        List<ProductDTO> productDTOS) {
        if(Long.parseLong(minPrice) < Long.parseLong(maxPrice) && Long.parseLong(minPrice) >= 0
                || Long.parseLong(maxPrice) <= defaultMaxPrice - 1) {

            List<ProductDTO> priceFilter = new ArrayList<>();
            // Push the Product DTO that has valid price range to a new list
            for (ProductDTO productDTO : productDTOS) {
                if (productDTO.getPrice() > Long.parseLong(minPrice) - 1 &&
                        productDTO.getPrice() < Long.parseLong(maxPrice) + 1) {

                    priceFilter.add(productDTO);
                }
            }
            return priceFilter;
        }
        else {
            return productDTOS;
        }
    }

    List<ProductDTO> filterBySubCate(String sub, List<ProductDTO> productDTOS) {
        if(sub != null && isNumber(sub) && !sub.equals("")) {
            List<ProductDTO> subFilter = new ArrayList<>();

            for (ProductDTO productDTO : productDTOS) {
                if (Long.parseLong(sub) == productDTO.getSubCategoryId()) {
                    subFilter.add(productDTO);
                }
            }
            return subFilter;
        }
        else {
            return productDTOS;
        }
    }

    List<ProductDTO> filterByCate(String cat, List<ProductDTO> productDTOS) {
        if(cat != null && isNumber(cat) && !cat.equals("")) {
            List<ProductDTO> catFilter = new ArrayList<>();

            for (ProductDTO productDTO : productDTOS) {
                if (Long.parseLong(cat) == productDTO.getCatId()) {
                    catFilter.add(productDTO);
                }
            }
            return catFilter;
        }
        else {
            return productDTOS;
        }
    }

    public ResponseEntity<ListOutputResult> search(String page, String limit, String keyword,
                                   String minPrice, String maxPrice, String sub, String cat) {
        ListOutputResult result = new ListOutputResult();

        if(typeRepository.findMaxPrice() == null) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }

        long defaultMaxPrice = typeRepository.findMaxPrice() + 1;

        page = (!isValidPage(page)) ? "1" : page;

        limit = (limit == null || limit.equals("") ||
                !isNumber(limit) || Long.parseLong(limit) < 0) ? "12" : limit;

        minPrice = (minPrice == null || minPrice.equals("") || !isNumber(minPrice)) ? "-1" : minPrice;

        maxPrice = (maxPrice == null || maxPrice.equals("") || !isNumber(maxPrice))
                ? String.valueOf(defaultMaxPrice) : maxPrice;

        List<ProductEntity> productEntities;

        if(keyword == null) {
            productEntities = productRepository.findAllWithCatIdAndSubCatIdAndStatus();
        }
        else {
            productEntities = productRepository.findAllBySearchWithCatIdAndSubCatIdAndStatus(keyword);
        }

        List<ProductDTO> productDTOS = new ArrayList<>();

        for (ProductEntity product : productEntities) {
            ProductDTO productDTO = dtoHandler(product);

            productDTOS.add(productDTO);
        }

        // Search by Price range
        productDTOS = filterByPriceRange(minPrice, maxPrice, defaultMaxPrice, productDTOS);

        // Search by Sub category
        productDTOS = filterBySubCate(sub, productDTOS);

        // Search by Category
        productDTOS = filterByCate(cat, productDTOS);

        // Pagination code below after filter the price range
        page = checkTotalPage(productDTOS.size(), Long.parseLong(limit), Long.parseLong(page));

        if(productDTOS.isEmpty()) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }
        // Start index and end index
        int startIndex = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
        int endIndex = (Integer.parseInt(limit) < productDTOS.size())
                ? Integer.parseInt(page) * Integer.parseInt(limit) : Integer.parseInt(page) * productDTOS.size();

        if(endIndex > productDTOS.size() - 1) {
            endIndex = productDTOS.size();
        }

        result.setItemsNumber(productDTOS.size());
        result.setList(productDTOS.subList(startIndex, endIndex));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//endregion


//region--------------------Functions for Comments Crud APIs---------------------------

    void commentDTOHandler(CommentEntity comment, CommentDTO commentDTO) {
        CommentMapper.toCommentDTO(comment, commentDTO);

        UserEntity resUser = userRepository.findFirstById(comment.getUserid());
        commentDTO.setUsername(resUser.getUsername());
        commentDTO.setAvatar(resUser.getAvatar());
    }

    public ResponseEntity<?> postCommentImg(long id, MultipartFile img, String username) {
        CommentEntity comment = commentRepository.findById(id)
                .orElse(null);

        if(comment == null) {
            return new ResponseEntity<>("Not found comment id " + id, HttpStatus.BAD_REQUEST);
        }


        Optional<UserEntity> usersOptional = userRepository.findByUsername(username);

        if(!usersOptional.isPresent()) {
            return new ResponseEntity<>("Not found username " + username, HttpStatus.BAD_REQUEST);
        }

        UserEntity userLogin = usersOptional.get();

        // Check user login id and comment id
        if(!userLogin.getId().equals(comment.getUserid())) {
            return new ResponseEntity<>("Username " + username + " is not allowed to do this action!",
                    HttpStatus.BAD_REQUEST);
        }

        String imgCommentUrl = cloudinaryService.uploadFile(
                img,
                String.valueOf(comment.getId()),
                "ShopeeProject" + "/" + "Comment");

        if(!imgCommentUrl.equals("-1"))
            comment.setImage(imgCommentUrl);

        commentRepository.save(comment);

        CommentDTO commentDTO = new CommentDTO();
        commentDTOHandler(comment, commentDTO);

        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    public ResponseEntity<?> createComment(long id, CommentDTO commentDTO) {
        CommentEntity comment = new CommentEntity();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        ProductEntity productEntity = productRepository.findById(id)
                .orElse(null);

        if(productEntity == null) {
            return new ResponseEntity<>("Not found product id " + id, HttpStatus.BAD_REQUEST);
        }

        // Limit the max rating
        if(commentDTO.getRating() > 5)
            commentDTO.setRating(5);

        // Limit the min rating
        if(commentDTO.getRating() < 1)
            commentDTO.setRating(1);

        comment.setProductEntity(productEntity);
        CommentMapper.toCommentEntity(commentDTO, comment);

        // GET Login username to get id
        if (principal instanceof ApplicationUser) {
            username = ((ApplicationUser)principal).getUsername();
        }
        else {
            username = principal.toString();
        }

        UserEntity user = userRepository.findFirstByUsername(username);
        comment.setUserid(user.getId());

        commentRepository.save(comment);

        CommentDTO res = new CommentDTO();
        commentDTOHandler(comment, res);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<ListOutputResult> getAllComments(long id, String page, String limit, String rating) {
        page = (!isValidPage(page)) ? "1" : page;

        limit = (limit == null || limit.equals("") ||
                !isNumber(limit) || Long.parseLong(limit) < 0) ? "12" : limit;

        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<CommentEntity> commentEntities;
        ListOutputResult res = new ListOutputResult();

        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        // if rating param, filter the comment by rating

        // Check if rating not a number or empty string set it to null
        rating = (!isNumber(rating) || rating.equals("")) ? null : rating;

        if(rating != null) {
            if(Long.parseLong(rating) > 5)
                rating = "5";
            if(Long.parseLong(rating) < 1)
                rating = "1";

            commentEntities = commentRepository.findAllByProductEntityIdAndRating(id,
                    Long.parseLong(rating), pageable, "Active");

            res.setItemsNumber(commentRepository.countAllByProductEntityIdAndRatingAndStatus(id,
                    Long.parseLong(rating), "Active"));
        }
        // Default get comments
        else {
            commentEntities = commentRepository.findAllByProductEntityIdAndStatus(id,
                    pageable, "Active");
            res.setItemsNumber(commentRepository.countAllByProductEntityIdAndStatus(id, "Active"));
        }

        if(commentEntities.size() == 0) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }

        // Trans to DTO
        for(CommentEntity comment : commentEntities) {
            CommentDTO commentDTO = new CommentDTO();

            commentDTOHandler(comment, commentDTO);
            commentDTOS.add(commentDTO);
        }

        res.setList(commentDTOS);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<?> banComment(long id) {
        CommentEntity comment = commentRepository.findById(id).orElse(null);

        if(comment == null) {
            return new ResponseEntity<>("Not found comment id " + id,
                    HttpStatus.BAD_REQUEST);
        }

        if(comment.getStatus().equals("Active")) {
            comment.setStatus("Inactive");
        }
        else
            comment.setStatus("Active");

        commentRepository.save(comment);

        return new ResponseEntity<>("State changed!", HttpStatus.OK);
    }


//endregion

    // Loc's Function
    public ProductEntity getProductsById(long productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() ->new Exception("Product is not found"));}
}

