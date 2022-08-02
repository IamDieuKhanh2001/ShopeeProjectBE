
package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUser;
import com.example.fsoft_shopee_nhom02.dto.CommentDTO;
import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.dto.ListOutputResult;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.CommentMapper;
import com.example.fsoft_shopee_nhom02.mapper.ProductMapper;
import com.example.fsoft_shopee_nhom02.model.*;
import com.example.fsoft_shopee_nhom02.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
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

    // update view
    public void updateViewProduct(long id)
    {
        ProductEntity product = productRepository.findById(id).get();
        product.setTotalView(product.getTotalView() + 1);
        productRepository.save(product);
    }
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

    public ProductDTO save(ProductDTO productDTO) {
        ProductEntity product;

        if(productDTO.getId() != 0) {
            // UPDATE
            product = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot found product id " + productDTO.getId()));
            ProductMapper.toProductEntity(product, productDTO);
        }
        else {
            // CREATE
            product = ProductMapper.toProductEntity(productDTO);
        }
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findById(productDTO.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found category id " + productDTO.getSubCategoryId()));
        product.setSubCategoryEntity(subCategoryEntity);

        productRepository.save(product);

        return ProductMapper.toProductDTO(product);
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
            long totalCmt = commentRepository.countAllByProductEntityId(product.getId());
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

    public ListOutputResult getAllProducts(String page, String limit) {
        ListOutputResult result = new ListOutputResult();

        page = (page == null) ? "1"  : page;
        limit = (limit == null) ? "12" : limit;

        List<ProductDTO> productDTOS = new ArrayList<>();

        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        List<ProductEntity> products = productRepository.findAll(pageable).getContent();

        for (ProductEntity product : products) {
            ProductDTO productDTO = dtoHandler(product);

            productDTOS.add(productDTO);
        }

        if(products.isEmpty()) {
            throw new ResourceNotFoundException("Empty data!");
        }

        long productsNumber = productRepository.count();

        result.setList(productDTOS);
        result.setItemsNumber(productsNumber);

        return result;
    }

    public ProductDTO getDetail(long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found product id " + id));

        return dtoHandler(product);
    }

    public void deleteProduct(long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found product id " + id));

        productRepository.delete(product);

        typeRepository.deleteAllByProductEntityId(id);
    }

//endregion


//region-------------------------Functions for Types Crud APIs, Search Function---------------------------

    public List<TypeEntity> getTypes(long id) {
        return typeRepository.findAllByProductEntityId(id);
    }

    public List<TypeEntity> createTypes(long id, List<TypeEntity> types) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found product id " + id));

        for(TypeEntity type : types) {
            type.setProductEntity(productEntity);
            typeRepository.save(type);
        }

        return types;
    }

    public List<?> updateAllTypes(long id, List<TypeEntity> typesList) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found product id " + id));

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

        return updatedTypesList;
    }
    /*
        Search truyen 3 params: page, limit, keyword
        - Page, limit de phan trang.
        - keyword la tu khoa nhap vao de tim san pham.
        - Json respond se bao gom tu khoa vua nhap, so san pham tim duoc
          va list san pham.

     */
    public ListOutputResult search(String page, String limit, String keyword,
                                   String minPrice, String maxPrice, String sub) {
        ListOutputResult result = new ListOutputResult();
        long defaultMaxPrice = typeRepository.findMaxPrice() + 1;

        page = (page == null) ? "1"  : page;
        limit = (limit == null) ? "12" : limit;
        minPrice = (minPrice == null) ? "-1" : minPrice;
        maxPrice = (maxPrice == null) ? String.valueOf(defaultMaxPrice) : maxPrice;

        List<ProductEntity> productEntities;
        List<ProductDTO> productDTOS = new ArrayList<>();

        // Search by Price range
        if(Long.parseLong(minPrice) < Long.parseLong(maxPrice) && Long.parseLong(minPrice) >= 0
            || Long.parseLong(maxPrice) <= defaultMaxPrice - 1) {

//            productEntities = (keyword == null) ? productRepository.findAll()
//                : productRepository.findAllBySearchQuery(keyword);
            if(keyword == null) {
                if(sub != null) {
                    productEntities = productRepository.findAllBySubCategoryEntityId(Long.parseLong(sub));
                }
                else
                    productEntities = productRepository.findAll();
            }
            else {
                if(sub != null) {
                    productEntities = productRepository.findAllBySearchAndSubCate(keyword, Long.parseLong(sub));
                }
                else
                    productEntities = productRepository.findAllBySearchQuery(keyword);
            }

            List<ProductDTO> priceFilterDTOS = new ArrayList<>();

            for (ProductEntity product : productEntities) {
                ProductDTO productDTO = dtoHandler(product);

                productDTOS.add(productDTO);
            }

            // Push the Product DTO that has valid price range to a new list
            for(ProductDTO productDTO : productDTOS) {
                if (productDTO.getPrice() > Long.parseLong(minPrice) - 1 &&
                        productDTO.getPrice() < Long.parseLong(maxPrice) + 1) {

                    priceFilterDTOS.add(productDTO);
                }
            }

            // Pagination code below after filter the price range
            int totalPage = (int) Math.ceil((double) priceFilterDTOS.size() / Integer.parseInt(limit));

            if(priceFilterDTOS.isEmpty() || totalPage < Integer.parseInt(page)) {
                throw new ResourceNotFoundException("No result!");
            }

            // Start index and end index
            int startIndex = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
            int endIndex = (Integer.parseInt(limit) < priceFilterDTOS.size())
                        ? Integer.parseInt(page) * Integer.parseInt(limit) : Integer.parseInt(page) * priceFilterDTOS.size();

            if(endIndex > priceFilterDTOS.size() - 1) {
                endIndex = priceFilterDTOS.size();
            }

            result.setItemsNumber(priceFilterDTOS.size());
            result.setList(priceFilterDTOS.subList(startIndex, endIndex));
        }
        // Default search if price range equal to null or not valid min max
        else {
            Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));
//            productEntities = (keyword == null) ? productRepository.findAll(pageable).getContent()
//                   : productRepository.findAllBySearchQuery(keyword, pageable);
//            long productsNumber = (keyword == null) ? productRepository.count()
//                    : productRepository.countAllBySearchQuery(keyword);

            long productsNumber;

            if(keyword == null) {
                if(sub != null) {
                    productEntities = productRepository.findAllBySubCategoryEntityId(Long.parseLong(sub), pageable);
                    productsNumber = productRepository.countAllBySubCate(Long.parseLong(sub));
                }
                else {
                    productEntities = productRepository.findAll(pageable).getContent();
                    productsNumber = productRepository.count();
                }
            }
            else {
                if(sub != null) {
                    productEntities = productRepository.findAllBySearchAndSubCate(keyword,
                            Long.parseLong(sub), pageable);
                    productsNumber = productRepository.countAllBySearchAndSubCate(keyword, Long.parseLong(sub));
                }
                else {
                    productEntities = productRepository.findAllBySearchQuery(keyword, pageable);
                    productsNumber = productRepository.countAllBySearchQuery(keyword);
                }
            }

            if(productEntities.isEmpty()) {
                throw new ResourceNotFoundException("No result!");
            }

            for (ProductEntity product : productEntities) {
                ProductDTO productDTO = dtoHandler(product);

                productDTOS.add(productDTO);
            }
            result.setItemsNumber(productsNumber);
            result.setList(productDTOS);
        }

        return result;
    }

//endregion


//region--------------------Functions for Comments Crud APIs---------------------------

    void commentDTOHandler(CommentEntity comment, CommentDTO commentDTO) {
        CommentMapper.toCommentDTO(comment, commentDTO);

        UserEntity resUser = userRepository.findFirstById(comment.getUserid());
        commentDTO.setUsername(resUser.getUsername());
        commentDTO.setAvatar(resUser.getAvatar());
    }

    public CommentDTO postCommentImg(long id, MultipartFile img, String username) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found comment id " + id));

        Optional<UserEntity> usersOptional = userRepository.findByUsername(username);

        if(!usersOptional.isPresent()) {
            throw new IllegalStateException("Username " + username + " not found");
        }

        UserEntity userLogin = usersOptional.get();

        // Check user login id and comment id
        if(!userLogin.getId().equals(comment.getUserid())) {
            throw new IllegalStateException("Username " + username + " is not allowed to do this action!");
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

        return commentDTO;
    }

    public CommentDTO createComment(long id, CommentDTO commentDTO) {
        CommentEntity comment = new CommentEntity();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found product id " + id));

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

        return res;
    }

    public ListOutputResult getAllComments(long id, String page, String limit, String rating) {
        page = (page == null) ? "1"  : page;
        limit = (limit == null) ? "12" : limit;

        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<CommentEntity> commentEntities;
        ListOutputResult res = new ListOutputResult();

        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        // if rating param, filter the comment by rating
        if(rating != null) {
            if(Long.parseLong(rating) > 5)
                rating = "5";
            if(Long.parseLong(rating) < 1)
                rating = "1";

            commentEntities = commentRepository.
                    findAllByProductEntityIdAndRating(id, Long.parseLong(rating), pageable);

            res.setItemsNumber(commentRepository.countAllByProductEntityIdAndRating(id, Long.parseLong(rating)));
        }
        // Default get comments
        else {
            commentEntities = commentRepository.findAllByProductEntityId(id, pageable);
            res.setItemsNumber(commentRepository.countAllByProductEntityId(id));
        }

        // Trans to DTO
        for(CommentEntity comment : commentEntities) {
            CommentDTO commentDTO = new CommentDTO();

            commentDTOHandler(comment, commentDTO);
            commentDTOS.add(commentDTO);
        }

        res.setList(commentDTOS);

        return res;
    }

//endregion

    // Loc's Function
    public ProductEntity getProductsById(long productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() ->new Exception("Product is not found"));}
}

