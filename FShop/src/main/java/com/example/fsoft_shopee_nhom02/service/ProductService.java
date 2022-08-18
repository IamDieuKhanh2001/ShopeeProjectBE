
package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUser;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.CommentDTO;
import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.dto.ListOutputResult;
import com.example.fsoft_shopee_nhom02.mapper.CommentMapper;
import com.example.fsoft_shopee_nhom02.mapper.ProductMapper;
import com.example.fsoft_shopee_nhom02.model.*;
import com.example.fsoft_shopee_nhom02.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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


    public ResponseEntity<?> saveProductImage(long id, MultipartFile imageProduct, MultipartFile image1, MultipartFile image2,
                                              MultipartFile image3, MultipartFile image4) {
        ProductEntity product = productRepository.findById(id).orElse(null);

        if(product == null) {
            return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + id,
                    HttpStatus.BAD_REQUEST);
        }

        // ImageProduct url
        String path = "ShopeeProject" + "/" + "Product" + "/";

        String imgProUrl = cloudinaryService.uploadFile(
                imageProduct,
                "ImageProduct",
                path + product.getId());

        String imgOneUrl = cloudinaryService.uploadFile(
                image1,
                "Image1",
                path + product.getId());

        String imgTwoUrl = cloudinaryService.uploadFile(
                image2,
                "Image2",
                path + product.getId());

        String imgThreeUrl = cloudinaryService.uploadFile(
                image3,
                "Image3",
                path + product.getId());

        String imgFourUrl = cloudinaryService.uploadFile(
                image4,
                "Image4",
                path + product.getId());

        // Condition check if not have img (Not optimize)
        if(!imgProUrl.equals("-1"))
            product.setImageProduct(imgProUrl);
        else if(product.getImageProduct().equals("") || product.getImageProduct().equals("-1"))
            product.setImageProduct("");

        if(!imgOneUrl.equals("-1"))
            product.setImage1(imgOneUrl);
        else if(product.getImage1().equals("") || product.getImage1().equals("-1"))
            product.setImage1("");

        if(!imgTwoUrl.equals("-1"))
            product.setImage2(imgTwoUrl);
        else if(product.getImage2().equals("") || product.getImage2().equals("-1"))
            product.setImage2("");

        if(!imgThreeUrl.equals("-1"))
            product.setImage3(imgThreeUrl);
        else if(product.getImage3().equals("") || product.getImage3().equals("-1"))
            product.setImage3("");

        if(!imgFourUrl.equals("-1"))
            product.setImage4(imgFourUrl);
        else if(product.getImage4().equals("") || product.getImage4().equals("-1"))
            product.setImage4("");

        productRepository.save(product);

        return new ResponseEntity<>(dtoHandler(product), HttpStatus.OK);
    }

    private Double calculateAvgRating(long total, long sum) {
        double avgRating = (double) sum / total;
        return Math.round(avgRating * 10.0) / 10.0;
    }

    private void getItemForRatingAndPrice(ProductEntity product) {
        Long sumRating = commentRepository.sumProductReview(product.getId());
        Long totalCmt = commentRepository.countAllByProductEntityIdAndStatus(
                product.getId(), GlobalVariable.ACTIVE_STATUS);

        Long minPrice = typeRepository.findMinPrice(product.getId());

        product.setAvgRating((product.getAvgRating() == null || sumRating == null
                || totalCmt == null) ? 0.0 : calculateAvgRating(totalCmt, sumRating));
        product.setFromPrice((product.getFromPrice() == null || minPrice == null) ? 0 : minPrice);
    }

    // Ham xu ly viec chuyen tu product sang DTO khi co truong Price
    public ProductDTO dtoHandler(ProductEntity product) {
        return ProductMapper.toProductDTO(product);
    }


    private String checkTotalPage(long totalItems, long limit, long page) {
        long totalPage = (long) Math.ceil((double) totalItems / limit);

        page = Math.min(totalPage, page);

        return String.valueOf(page);
    }

    private Boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Boolean isValidPage(String page) {
        return page != null && !page.equals("") && isNumber(page) && Long.parseLong(page) >= 0;
    }

    private Boolean isValidParam(String param) {
        return param != null && !param.equals("") && isNumber(param);
    }

    public ResponseEntity<?> save(ProductDTO productDTO) {
        ProductEntity product;

        if (productDTO.getId() != 0) {
            // UPDATE
            product = productRepository.findById(productDTO.getId())
                    .orElse(null);

            if (product == null) {
                return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + productDTO.getId(),
                        HttpStatus.BAD_REQUEST);
            }
            ProductMapper.toProductEntity(product, productDTO);
            getItemForRatingAndPrice(product);
        } else {
            // CREATE
            product = ProductMapper.toProductEntity(productDTO);
        }
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findById(productDTO.getSubCategoryId())
                .orElse(null);

        if (subCategoryEntity == null) {
            return new ResponseEntity<>("Not found sub category id " + productDTO.getSubCategoryId(),
                    HttpStatus.BAD_REQUEST);
        }

        product.setSubCategoryEntity(subCategoryEntity);

        productRepository.save(product);

        return new ResponseEntity<>(ProductMapper.toProductDTO(product), HttpStatus.OK);
    }

    public ResponseEntity<ListOutputResult> getAllProducts(String page, String limit) {
        ListOutputResult result = new ListOutputResult();

        List<ProductDTO> productDTOS = new ArrayList<>();

        limit = (limit == null || limit.equals("")
                || !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;
        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;

        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        Page<ProductEntity> products = productRepository.findAllWithCatIdAndSubCatIdAndStatus(pageable);

        for (ProductEntity product : products) {
            ProductDTO productDTO = dtoHandler(product);
            productDTOS.add(productDTO);
        }

        if (products.isEmpty()) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }

        result.setList(productDTOS);
        result.setItemsNumber(products.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<?> getDetail(long id) {
        ProductEntity product = productRepository.findById(id)
                .orElse(null);

        if (product == null || product.getStatus().equals(GlobalVariable.INACTIVE_STATUS)) {
            return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + id, HttpStatus.BAD_REQUEST);
        }

        // Update view
        product.setTotalView(product.getTotalView() + 1);
        productRepository.save(product);

        return new ResponseEntity<>(dtoHandler(product), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteProduct(long id) {
        ProductEntity product = productRepository.findById(id)
                .orElse(null);

        if (product == null) {
            return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + id, HttpStatus.BAD_REQUEST);
        }

        if (product.getStatus().equals(GlobalVariable.ACTIVE_STATUS)) {
            product.setStatus(GlobalVariable.INACTIVE_STATUS);
        } else
            product.setStatus(GlobalVariable.ACTIVE_STATUS);

        productRepository.save(product);

        return new ResponseEntity<>("State changed!", HttpStatus.OK);
    }

//endregion


//region-------------------------Functions for Types Crud APIs, Search Function---------------------------

    public ResponseEntity<?> getTypes(long id) {
        if (!typeRepository.findAllByProductEntityId(id).isEmpty()) {
            return new ResponseEntity<>(typeRepository.findAllByProductEntityId(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + id, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> createTypes(long id, List<TypeEntity> types) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElse(null);

        if (productEntity == null) {
            return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + id, HttpStatus.BAD_REQUEST);
        }

        for (TypeEntity type : types) {
            type.setProductEntity(productEntity);
            typeRepository.save(type);
        }

        // Update min price
        getItemForRatingAndPrice(productEntity);
        productRepository.save(productEntity);

        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    public ResponseEntity<?> updateAllTypes(long id, List<TypeEntity> typesList) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElse(null);

        if (productEntity == null) {
            return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + id, HttpStatus.BAD_REQUEST);
        }

        List<TypeEntity> updatedTypesList = typeRepository.findAllByProductEntityId(id);

        for (TypeEntity updatedType : updatedTypesList) {
            TypeEntity type = typesList.get(updatedTypesList.indexOf(updatedType));
            updatedType.setPrice(type.getPrice());
            updatedType.setType(type.getType());
            updatedType.setQuantity(type.getQuantity());
            updatedType.setProductEntity(productEntity);
        }

        typeRepository.saveAll(updatedTypesList);

        // Update min price
        getItemForRatingAndPrice(productEntity);
        productRepository.save(productEntity);

        return new ResponseEntity<>(updatedTypesList, HttpStatus.OK);
    }

    Boolean isValidRating(String rating) {
        return (rating != null && !rating.equals("") && isNumber(rating)
                && Long.parseLong(rating) <= 5 && Long.parseLong(rating) > 0);
    }

    private void paginationDTOSHandler(int size, String page, String limit,
                                       ListOutputResult res, List<?> dtos) {
        page = checkTotalPage(size, Long.parseLong(limit), Long.parseLong(page));

        // Start index and end index
        int startIndex = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
        int endIndex = (Integer.parseInt(limit) < size)
                ? Integer.parseInt(page) * Integer.parseInt(limit)
                : Integer.parseInt(page) * size;

        if (endIndex > size - 1) {
            endIndex = size;
        }
        res.setItemsNumber(size);
        res.setList(dtos.subList(startIndex, endIndex));
    }

    public ResponseEntity<ListOutputResult> search(String page, String limit, String keyword,
                                                   String minPrice, String maxPrice, String sub,
                                                   String cat, String rating) {
        ListOutputResult result = new ListOutputResult();

        if (typeRepository.findMaxPrice() == null) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }

        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;
        limit = (limit == null || limit.equals("") ||
                !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        keyword = (keyword == null) ? "" : keyword;
        Long minPriceParam = (isValidParam(minPrice)) ? Long.parseLong(minPrice) : null;
        Long maxPriceParam = (isValidParam(maxPrice)) ? Long.parseLong(maxPrice) : null;
        Long subParam = (isValidParam(sub)) ? Long.parseLong(sub) : null;
        Long catParam = (isValidParam(cat)) ? Long.parseLong(cat) : null;
        Integer ratingParam = (isValidRating(rating)) ? Integer.parseInt(rating) : null;

        Page<ProductEntity> productEntities;
        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        productEntities = productRepository.findAllBySearchWithCatIdAndSubCatIdAndStatus(keyword,
                subParam, catParam, minPriceParam, maxPriceParam, ratingParam, pageable);

        List<ProductDTO> productDTOS = new ArrayList<>();

        for (ProductEntity product : productEntities) {
            ProductDTO productDTO = dtoHandler(product);
            productDTOS.add(productDTO);
        }

        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }

        result.setItemsNumber(productEntities.getTotalElements());
        result.setList(productDTOS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
//endregion


//region--------------------Functions for Comments Crud APIs---------------------------

    private void commentDTOHandler(CommentEntity comment, CommentDTO commentDTO) {
        CommentMapper.toCommentDTO(comment, commentDTO);

        UserEntity resUser = userRepository.findFirstById(comment.getUserid());
        commentDTO.setUsername(resUser.getUsername());
        commentDTO.setAvatar(resUser.getAvatar());
    }

    public ResponseEntity<?> postCommentImg(long id, MultipartFile img, String username) {
        CommentEntity comment = commentRepository.findById(id)
                .orElse(null);

        if (comment == null) {
            return new ResponseEntity<>("Not found comment id " + id, HttpStatus.BAD_REQUEST);
        }

        Optional<UserEntity> usersOptional = userRepository.findByUsername(username);

        if (!usersOptional.isPresent()) {
            return new ResponseEntity<>("Not found username " + username, HttpStatus.BAD_REQUEST);
        }

        UserEntity userLogin = usersOptional.get();

        // Check user login id and comment id
        if (!userLogin.getId().equals(comment.getUserid())) {
            return new ResponseEntity<>("Username " + username + " is not allowed to do this action!",
                    HttpStatus.BAD_REQUEST);
        }

        String imgCommentUrl = cloudinaryService.uploadFile(
                img,
                String.valueOf(comment.getId()),
                "ShopeeProject" + "/" + "Comment");

        if (!imgCommentUrl.equals("-1"))
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

        if (productEntity == null) {
            return new ResponseEntity<>(GlobalVariable.NOT_FOUND_PRODUCT_MESSAGE + id, HttpStatus.BAD_REQUEST);
        }

        // Limit the max rating
        if (commentDTO.getRating() > 5)
            commentDTO.setRating(5);

        // Limit the min rating
        if (commentDTO.getRating() < 1)
            commentDTO.setRating(1);

        comment.setProductEntity(productEntity);
        CommentMapper.toCommentEntity(commentDTO, comment);

        // GET Login username to get id
        if (principal instanceof ApplicationUser) {
            username = ((ApplicationUser) principal).getUsername();
        } else {
            username = principal.toString();
        }

        UserEntity user = userRepository.findFirstByUsername(username);
        comment.setUserid(user.getId());

        commentRepository.save(comment);

        CommentDTO res = new CommentDTO();
        commentDTOHandler(comment, res);

        // update avg rating
        getItemForRatingAndPrice(productEntity);
        productRepository.save(productEntity);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private List<CommentDTO> filterByRating(String rating, List<CommentDTO> commentDTOS) {
        if (isValidRating(rating)) {

            List<CommentDTO> ratingFilter = new ArrayList<>();

            for (CommentDTO commentDTO : commentDTOS) {
                if (commentDTO.getRating() == Long.parseLong(rating)) {
                    ratingFilter.add(commentDTO);
                }
            }
            return ratingFilter;
        } else return commentDTOS;
    }

    public ResponseEntity<ListOutputResult> getAllComments(long id, String page, String limit, String rating) {
        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;

        limit = (limit == null || limit.equals("") ||
                !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<CommentEntity> commentEntities = commentRepository.
                findAllByProductEntityIdAndStatus(id, GlobalVariable.ACTIVE_STATUS);
        ListOutputResult res = new ListOutputResult();

        // Check if rating not a number or empty string set it to null
        rating = (!isNumber(rating) || rating.equals("")) ? null : rating;

        // Trans to DTO
        for (CommentEntity comment : commentEntities) {
            CommentDTO commentDTO = new CommentDTO();

            commentDTOHandler(comment, commentDTO);
            commentDTOS.add(commentDTO);
        }

        // Newest comments go first
        commentDTOS.sort(Comparator.comparing(CommentDTO::getId).reversed());

        commentDTOS = filterByRating(rating, commentDTOS);

        if (commentEntities.isEmpty() || commentDTOS.isEmpty()) {
            return new ResponseEntity<>(new ListOutputResult(0, new ArrayList<>()),
                    HttpStatus.NOT_FOUND);
        }

        // Call pagination function for DTOs
        paginationDTOSHandler(commentDTOS.size(), page, limit, res, commentDTOS);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<?> banComment(long id) {
        CommentEntity comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return new ResponseEntity<>("Not found comment id " + id,
                    HttpStatus.BAD_REQUEST);
        }

        if (comment.getStatus().equals(GlobalVariable.ACTIVE_STATUS)) {
            comment.setStatus(GlobalVariable.INACTIVE_STATUS);
        } else
            comment.setStatus(GlobalVariable.ACTIVE_STATUS);

        commentRepository.save(comment);

        return new ResponseEntity<>("State changed!", HttpStatus.OK);
    }


//endregion

    // Loc's Function
    public ProductEntity getProductsById(long productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() -> new Exception("Product is not found"));
    }

    public List<ProductEntity> getAllByProIdList(Collection<Long> idList) {
        return productRepository.findAllByIdIn(idList);
    }
}

